package me.falu.macaid.mixin;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToolMaterials.class)
public class ToolMaterialsMixin {
    @Inject(method = "getRepairIngredient", at = @At("RETURN"), cancellable = true)
    private void cheapRepair(CallbackInfoReturnable<Ingredient> cir) {
        cir.setReturnValue(Ingredient.ofItems(Items.RAW_COPPER));
    }
}
