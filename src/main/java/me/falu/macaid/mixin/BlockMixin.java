package me.falu.macaid.mixin;

import me.falu.macaid.BlocksMinedStatOwner;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "afterBreak", at = @At("TAIL"))
    private void updateStat(CallbackInfo ci) {
        BlocksMinedStatOwner.INSTANCE.update();
    }

    @Redirect(method = "afterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V"))
    private void noExhaustion(PlayerEntity instance, float exhaustion) {}

    @Inject(method = "dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void moveDropToPlayer(World world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            if (world instanceof ServerWorld) {
                Vec3d p = client.player.getPos().subtract(Vec3d.ofCenter(pos));
                float f = EntityType.ITEM.getWidth() / 2.0F;
                double d = (pos.getX() + 0.5F) + MathHelper.clamp(p.x, -0.25D, 0.25D);
                double e = (pos.getY() + 0.5F) + MathHelper.clamp(p.y, -0.25D, 0.25D) - f;
                double h = (pos.getZ() + 0.5F) + MathHelper.clamp(p.z, -0.25D, 0.25D);
                double l = MathHelper.clamp(p.x, 0.0D, 1.0D) * 0.2D - 0.1D;
                double m = 0.2D;
                double n = MathHelper.clamp(p.z, 0.0D, 1.0D) * 0.2D - 0.1D;
                ItemEntity entity = new ItemEntity(world, d, e, h, stack);
                entity.setVelocity(l, m, n);
                entity.setToDefaultPickupDelay();
                world.spawnEntity(entity);
                ci.cancel();
            }
        }
    }
}
