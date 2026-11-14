package dev.ghostclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;

public final class GhostClient implements ClientModInitializer {
    public static boolean hudEnabled = true;
    private KeyBinding menuKey;
    private KeyBinding hudKey;
    private KeyBinding espKey;
    private KeyBinding blockEspKey;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostclient.menu", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, "category.ghostclient.main"));
        hudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostclient.hud", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H, "category.ghostclient.main"));
        espKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostclient.esp", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G, "category.ghostclient.main"));
        blockEspKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostclient.block_esp", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B, "category.ghostclient.main"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) client.setScreen(new GhostScreen());
            while (hudKey.wasPressed()) hudEnabled = !hudEnabled;
            while (espKey.wasPressed()) GhostEsp.enabled = !GhostEsp.enabled;
            while (blockEspKey.wasPressed()) BlockEsp.enabled = !BlockEsp.enabled;
            GhostEsp.tick(client);
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (hudEnabled && client.player != null) GhostHud.render(drawContext);
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (!BlockEsp.enabled || client.world == null || client.player == null
                    || context.matrixStack() == null || context.consumers() == null) return;
            var matrices = context.matrixStack();
            var camera = context.camera().getPos();
            matrices.push();
            matrices.translate(-camera.x, -camera.y, -camera.z);
            VertexConsumer lines = context.consumers().getBuffer(RenderLayer.getLines());
            for (var pos : BlockEsp.findNearby(client)) {
                Box box = new Box(pos).expand(0.002);
                WorldRenderer.drawBox(matrices, lines, box, 0.72f, 0.35f, 1.0f, 0.95f);
            }
            matrices.pop();
        });
    }
}
