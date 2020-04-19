package tfar.spiketraps;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class SpikeBlockEntity extends BlockEntity {

  public Map<Enchantment,Integer> enchs;

  public SpikeBlockEntity() {
    super(SpikeTraps.Objects.spike_block_entity);
  }

  @Override
  public void fromTag(CompoundTag nbt) {
    super.fromTag(nbt);
    deserialize(nbt.getList("enchants",9));
  }
  @Override
  public CompoundTag toTag(CompoundTag nbt) {
    super.toTag(nbt);
    nbt.put("enchants", serialize());
    return nbt;
  }

  public ListTag serialize(){
    ListTag enchsTag = new ListTag();
    enchs.forEach((enchantment, integer) -> {
      CompoundTag compoundTag = new CompoundTag();
      compoundTag.putString("id", String.valueOf(Registry.ENCHANTMENT.getId(enchantment)));
      compoundTag.putInt("lvl", integer);
      enchsTag.add(compoundTag);
    });
    return enchsTag;
  }

  public void setEnchs(Map<Enchantment,Integer> enchs){
    this.enchs = enchs;
  }

  public void deserialize(ListTag tag) {
    enchs = EnchantmentHelper.getEnchantments(tag);
  }
}