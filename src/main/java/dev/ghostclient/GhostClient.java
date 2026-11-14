package dev.ghostclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class GhostClient implements ClientModInitializer {
    public static boolean hudEnabled = true;
    private KeyBinding menuKey;
    private KeyBinding hudKey;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostclient.menu", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, "category.ghostclient.main"));
        hudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostclient.hud", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H, "category.ghostclient.main"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) client.setScreen(new GhostScreen());
            while (hudKey.wasPressed()) hudEnabled = !hudEnabled;
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (hudEnabled && client.player != null) GhostHud.render(drawContext);
        });
    }
}
