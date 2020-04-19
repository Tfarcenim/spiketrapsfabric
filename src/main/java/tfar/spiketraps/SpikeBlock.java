package tfar.spiketraps;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import tfar.spiketraps.fakeplayer.FakePlayer;
import tfar.spiketraps.fakeplayer.FakePlayerFactory;
import tfar.spiketraps.mixin.LivingEntityAccessor;

import javax.annotation.Nullable;
import java.util.*;

public class SpikeBlock extends Block implements BlockEntityProvider {

  public final float damage;

  public SpikeBlock(Settings p_i48440_1_, float damage) {
    super(p_i48440_1_);
    this.setDefaultState(this.getDefaultState().with(PROPERTY_FACING, Direction.NORTH));
    this.damage = damage;
  }

  public static final VoxelShape NORTH_AABB = Block.createCuboidShape(0, 0, 9, 16, 16, 16);
  public static final VoxelShape SOUTH_AABB = Block.createCuboidShape(0, 0, 0, 16, 16, 7);
  public static final VoxelShape WEST_AABB = Block.createCuboidShape(9, 0, 0, 16, 16, 16);
  public static final VoxelShape UP_AABB = Block.createCuboidShape(0, 0, 0, 16, 7, 16);
  public static final VoxelShape EAST_AABB = Block.createCuboidShape(0, 0, 0, 7, 16, 16);
  public static final VoxelShape DOWN_AABB = Block.createCuboidShape(0, 9, 0, 16, 16, 16);

  public static final DirectionProperty PROPERTY_FACING = Properties.FACING;
  private static GameProfile PROFILE = new GameProfile(UUID.fromString("a42ac406-c797-4e0e-b147-f01ac5551be5"), "[SpikeTraps]");

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context) {
    Direction dir = state.get(PROPERTY_FACING);
    switch (dir) {
      case NORTH:
        return NORTH_AABB;
      case SOUTH:
        return SOUTH_AABB;
      case WEST:
        return WEST_AABB;
      case UP:
        return UP_AABB;
      case EAST:
        return EAST_AABB;
      case DOWN:
      default:
        return DOWN_AABB;
    }
  }

  public BlockState rotate(BlockState state, BlockRotation rot) {
    return state.with(PROPERTY_FACING, rot.rotate(state.get(PROPERTY_FACING)));
  }

  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.rotate(mirror.getRotation(state.get(PROPERTY_FACING)));
  }


  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(PROPERTY_FACING);
  }

  public BlockState getPlacementState(ItemPlacementContext context) {
    return this.getDefaultState().with(PROPERTY_FACING, context.getSide());
  }

  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (world.isClient || !(entity instanceof LivingEntity)) return;
    //diamond spike
    if (this == SpikeTraps.Objects.diamond_spike) {
      FakePlayer fakePlayer = FakePlayerFactory.get((ServerWorld) world, PROFILE);
      SpikeBlockEntity blockEntity = (SpikeBlockEntity) world.getBlockEntity(pos);
      Map<Enchantment, Integer> ench = blockEntity.enchs;
      ItemStack stick = new ItemStack(SpikeTraps.Objects.fake_sword);
      EnchantmentHelper.set(ench, stick);
      fakePlayer.setStackInHand(Hand.MAIN_HAND, stick);
      fakePlayer.attack(entity);
      ((LivingEntity) entity).setAttacker(null);
    } else {
      if (state.getBlock() == SpikeTraps.Objects.gold_spike)
        ((LivingEntityAccessor)entity).setPlayerHitTimer(100);
      if (state.getBlock() == SpikeTraps.Objects.wood_spike && ((LivingEntity) entity).getHealth() <= 1) return;
      if (state.getBlock() == SpikeTraps.Objects.wood_spike && ((LivingEntity) entity).getHealth() <= 4) {
        entity.damage(DamageSource.CACTUS, ((LivingEntity) entity).getHealth() - 1);
        return;
      }
      entity.damage(DamageSource.CACTUS, this.damage);
    }
  }

  @Override
  public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    BlockEntity te = world.getBlockEntity(pos);
    if (te instanceof SpikeBlockEntity && !world.isClient && placer != null) {
      ((SpikeBlockEntity) te).setEnchs(EnchantmentHelper.getEnchantments(stack));
    }
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockView view) {
    return this == SpikeTraps.Objects.diamond_spike ? new SpikeBlockEntity() : null;
  }
}

