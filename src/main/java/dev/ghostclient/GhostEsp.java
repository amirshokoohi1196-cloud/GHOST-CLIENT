package dev.ghostclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class GhostEsp {
    public static boolean enabled = false;
    public static boolean players = true;
    public static boolean hostile = true;
    public static boolean passive = true;
    private static final Set<Entity> changed = Collections.newSetFromMap(new IdentityHashMap<>());
    private GhostEsp() {}

    public static void tick(MinecraftClient client) {
        if (client.world == null || client.player == null || !enabled) {
            clear();
            return;
        }
        Set<Entity> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        for (Entity entity : client.world.getEntities()) {
            if (entity == client.player || !entity.isAlive()) continue;
            boolean target = (players && entity instanceof PlayerEntity)
                    || (hostile && entity instanceof HostileEntity)
                    || (passive && entity instanceof PassiveEntity);
            if (!target) continue;
            seen.add(entity);
            if (!entity.isGlowing()) {
                entity.setGlowing(true);
                changed.add(entity);
            }
        }
        changed.removeIf(entity -> {
            if (!entity.isRemoved() && seen.contains(entity)) return false;
            entity.setGlowing(false);
            return true;
        });
    }

    public static void clear() {
        for (Entity entity : changed) if (!entity.isRemoved()) entity.setGlowing(false);
        changed.clear();
    }
}
