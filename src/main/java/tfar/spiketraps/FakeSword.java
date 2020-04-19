package tfar.spiketraps;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.SwordItem;

public class FakeSword extends SwordItem {
  public FakeSword() {
    super(ToolMaterials.DIAMOND, (int) ToolMaterials.DIAMOND.getAttackDamage(), 1000, new Settings());
  }

  @Override
  public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    target.damage(DamageSource.player((PlayerEntity) attacker), getAttackDamage() + EnchantmentHelper.getAttackDamage(stack, target.getGroup()));
    return true;
  }

  @Override
  public boolean isDamageable() {
    return false;
  }
}
