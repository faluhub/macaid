package me.falu.macaid.mixin;

import me.falu.macaid.BlocksMinedStatOwner;
import me.falu.macaid.gui.MilestoneRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow public abstract TextRenderer getTextRenderer();
    @Shadow private int scaledHeight;
    @Unique private final List<Integer> celebrated = new ArrayList<>();

    @Inject(method = "render", at = @At("TAIL"))
    private void renderBlocksMined(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MilestoneRenderer.INSTANCE.render(matrices);
        int mined = BlocksMinedStatOwner.INSTANCE.getBlocksMined();
        String minedString = String.valueOf(mined);
        this.getTextRenderer().drawWithShadow(
                matrices,
                minedString,
                0.0F,
                this.scaledHeight - this.getTextRenderer().fontHeight,
                0xFFFFFF
        );
        if (!this.celebrated.contains(mined)) {
            if (mined % 1000 != 0) { return; }
            MilestoneRenderer.INSTANCE.init(minedString);
            this.celebrated.add(mined);
        }
    }
}
