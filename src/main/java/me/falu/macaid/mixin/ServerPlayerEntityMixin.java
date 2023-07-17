package me.falu.macaid.mixin;

import me.falu.macaid.BlocksMinedStatOwner;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onSpawn", at = @At("TAIL"))
    private void updateBlocks(CallbackInfo ci) {
        BlocksMinedStatOwner.INSTANCE.update();
    }
}
