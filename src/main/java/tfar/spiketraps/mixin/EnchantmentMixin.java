package tfar.spiketraps.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.spiketraps.SpikeBlockItem;
import tfar.spiketraps.SpikeTraps;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Inject(method = "isAcceptableItem",at = @At("HEAD"),cancellable = true)
	private void yes(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
		Enchantment enchantment = (Enchantment)(Object)this;
		if (stack.getItem() == SpikeTraps.Objects.diamond_spike.asItem() && SpikeBlockItem.whitelist.contains(enchantment))cir.setReturnValue(true);
	}
}
