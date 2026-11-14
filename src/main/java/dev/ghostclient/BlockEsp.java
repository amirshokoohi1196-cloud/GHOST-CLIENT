package dev.ghostclient;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import java.util.LinkedHashSet;
import java.util.Set;

public final class BlockEsp {
    public static boolean enabled = false;
    public static int radius = 12;
    public static final Set<Identifier> selected = new LinkedHashSet<>();
    private BlockEsp() {}

    public static boolean isSelected(Block block) {
        return selected.contains(Registries.BLOCK.getId(block));
    }

    public static Set<Identifier> allBlockIds() {
        return Registries.BLOCK.getIds();
    }

    public static void toggle(Identifier id) {
        if (!selected.add(id)) selected.remove(id);
    }

    public static Iterable<BlockPos> findNearby(MinecraftClient client) {
        if (client.world == null || client.player == null || selected.isEmpty()) return Set.of();
        BlockPos center = client.player.getBlockPos();
        Set<BlockPos> found = new LinkedHashSet<>();
        int r = Math.max(2, Math.min(radius, 24));
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    pos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    if (isSelected(client.world.getBlockState(pos).getBlock())) found.add(pos.toImmutable());
                }
            }
        }
        return found;
    }
}
