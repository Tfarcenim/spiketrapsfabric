package tfar.spiketraps;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import tfar.extratags.api.tagtypes.EnchantmentTags;

public class SpikeBlockItem extends BlockItem {
  public SpikeBlockItem(Block blockIn, Settings builder) {
    super(blockIn, builder);
  }

  public static final Tag<Enchantment> whitelist = new EnchantmentTags.CachingTag(
          new Identifier(SpikeTraps.MODID,"whitelist"));

  @Override
  public int getEnchantability() {
    return Block.getBlockFromItem(this) == SpikeTraps.Objects.diamond_spike ? ToolMaterials.DIAMOND.getEnchantability() : 0;
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return Block.getBlockFromItem(stack.getItem()) == SpikeTraps.Objects.diamond_spike;
  }

}
