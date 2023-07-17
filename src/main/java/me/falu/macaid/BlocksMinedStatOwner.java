package me.falu.macaid;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.Stats;

public class BlocksMinedStatOwner {
    public static final BlocksMinedStatOwner INSTANCE = new BlocksMinedStatOwner();
    private int blocksMined = 0;

    public BlocksMinedStatOwner() {
        this.update();
    }

    public void update() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getServer() == null) { return; }
        ServerPlayerEntity player = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());
        if (player == null) { return; }
        StatHandler stats = player.getStatHandler();
        this.blocksMined = 0;
        for (Block block : Registries.BLOCK) {
            int amount = stats.getStat(Stats.MINED, block);
            this.blocksMined += amount;
        }
    }

    public int getBlocksMined() {
        return this.blocksMined;
    }
}
