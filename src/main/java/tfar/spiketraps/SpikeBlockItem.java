package tfar.spiketraps;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;

import java.util.Set;

public class SpikeBlockItem extends BlockItem {
  public SpikeBlockItem(Block blockIn, Settings builder) {
    super(blockIn, builder);
  }

  public static final Set<Enchantment> whitelist = Sets.newHashSet(
          Enchantments.SHARPNESS, Enchantments.LOOTING);

  @Override
  public int getEnchantability() {
    return Block.getBlockFromItem(this) == SpikeTraps.Objects.diamond_spike ? ToolMaterials.DIAMOND.getEnchantability() : 0;
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return Block.getBlockFromItem(stack.getItem()) == SpikeTraps.Objects.diamond_spike;
  }

}
