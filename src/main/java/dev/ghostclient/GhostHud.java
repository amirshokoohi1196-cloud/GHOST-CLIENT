package dev.ghostclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

public final class GhostHud {
    private GhostHud() {}

    public static void render(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        int x = 8, y = 8, width = 142, height = 50;
        context.fill(x, y, x + width, y + height, 0xB8171022);
        context.fill(x, y, x + width, y + 2, 0xFFBD83FF);
        context.drawTextWithShadow(mc.textRenderer, "GHOST CLIENT", x + 6, y + 6, 0xFFBD83FF);
        BlockPos pos = mc.player.getBlockPos();
        context.drawTextWithShadow(mc.textRenderer,
                "XYZ: " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ(),
                x + 6, y + 20, 0xFFF4EDFF);
        context.drawTextWithShadow(mc.textRenderer, "FPS: " + mc.getCurrentFps(),
                x + 6, y + 34, 0xFFF4EDFF);
    }
}
