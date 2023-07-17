package me.falu.macaid.mixin;

import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElytraItem.class)
public class ElytraItemMixin {
    @Inject(method = "canRepair", at = @At("RETURN"), cancellable = true)
    private void cheaperRepair(ItemStack stack, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(ingredient.isOf(Items.RAW_COPPER));
    }
}
