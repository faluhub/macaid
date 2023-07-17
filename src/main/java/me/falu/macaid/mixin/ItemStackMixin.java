package me.falu.macaid.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getRepairCost", at = @At("RETURN"), cancellable = true)
    private void cheapRepair(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }
}
