package me.falu.macaid.mixin;

import me.falu.macaid.gui.MilestoneRenderer;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    private void resizePopup(CallbackInfo ci) {
        MilestoneRenderer.INSTANCE.resize();
    }
}
