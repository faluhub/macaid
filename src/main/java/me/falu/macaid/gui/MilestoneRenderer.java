package me.falu.macaid.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class MilestoneRenderer {
    public static final MilestoneRenderer INSTANCE = new MilestoneRenderer();
    private final MinecraftClient client;
    private final TextRenderer textRenderer;
    public int width;
    public int height;
    private long startTime = -1;
    private String milestoneText = "";

    private MilestoneRenderer() {
        this.client = MinecraftClient.getInstance();
        this.textRenderer = this.client.textRenderer;
        this.resize();
    }

    public void resize() {
        this.width = this.client.getWindow().getScaledWidth();
        this.height = this.client.getWindow().getScaledHeight();
    }

    public void init(String milestoneText) {
        if (this.startTime > 0) { return; }
        this.startTime = Util.getMeasuringTimeMs();
        this.milestoneText = milestoneText;
        this.resize();
        this.client.execute(() -> {
            if (this.client.player != null) {
                this.client.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            }
        });
    }

    public void render(MatrixStack matrices) {
        long diff = Util.getMeasuringTimeMs() - this.startTime;
        if (this.startTime < 0 || diff < 0 || this.milestoneText.isEmpty()) { return; }

        long scaleTime = 300;
        long idleTime = 5000;
        long fadeTime = 2000;
        if (diff > scaleTime + idleTime + fadeTime) {
            this.startTime = -1;
            return;
        }

        float scalePercentage = 1.0F;
        if (diff < scaleTime) {
            scalePercentage = Math.min(1.0F, (float) diff / scaleTime);
            scalePercentage = (float) this.popupEase(scalePercentage);
        }

        int white = 0xFFFFFF;
        int yellow = 0xFFFF55;
        if (diff > scaleTime + idleTime) {
            float alphaPercentage = 1.0F - Math.min(1.0F, (float) (diff - scaleTime - idleTime) / fadeTime);
            int alpha = MathHelper.clamp((int) (alphaPercentage * 255.0F), 0, 255);
            white = ColorHelper.Argb.getArgb(alpha, 255, 255, 255);
            yellow = ColorHelper.Argb.getArgb(alpha, 255, 255, 85);
        }

        matrices.push();
        float scale = 5.0F * scalePercentage;
        matrices.scale(scale, scale, 1.0F);
        this.textRenderer.draw(
                matrices,
                this.milestoneText,
                (this.width / 2.0F - this.textRenderer.getWidth(this.milestoneText) * scale / 2.0F) / scale,
                (this.height / 4.0F - this.textRenderer.fontHeight * scale / 2.0F) / scale,
                yellow
        );
        matrices.pop();

        matrices.push();
        scale = 1.2F * scalePercentage;
        matrices.scale(scale, scale, 1.0F);
        String descriptionText = "New Milestone!";
        this.textRenderer.draw(
                matrices,
                descriptionText,
                (this.width / 2.0F - this.textRenderer.getWidth(descriptionText) * scale / 2.0F) / scale,
                (this.height / 2.0F - this.textRenderer.fontHeight * scale / 2.0F - 30) / scale,
                white
        );
        matrices.pop();
    }

    private double popupEase(float x) {
        float c1 = 1.70158F;
        float c3 = c1 + 1.0F;
        return 1.0F + c3 * Math.pow(x - 1.0F, 3.0F) + c1 * Math.pow(x - 1.0F, 2.0F);
    }
}
