package dev.ghostclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class GhostScreen extends Screen {
    public GhostScreen() { super(Text.literal("GHOST CLIENT")); }

    private static Text fa(String value) {
        return Text.literal(GhostText.rtl(value));
    }

    private static Text hudLabel() {
        return fa("نمایش HUD: " + (GhostClient.hudEnabled ? "روشن" : "خاموش"));
    }

    @Override
    protected void init() {
        int cx = width / 2, cy = height / 2;
        addDrawableChild(ButtonWidget.builder(hudLabel(), button -> {
            GhostClient.hudEnabled = !GhostClient.hudEnabled;
            button.setMessage(hudLabel());
        }).dimensions(cx - 90, cy - 8, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(fa("بستن"),
                button -> close()).dimensions(cx - 90, cy + 20, 180, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int l = width / 2 - 115, t = height / 2 - 65;
        context.fill(l, t, l + 230, t + 125, 0xE8171022);
        context.fill(l, t, l + 230, t + 2, 0xFFBD83FF);
        context.drawCenteredTextWithShadow(textRenderer, "GHOST CLIENT",
                width / 2, t + 12, 0xFFBD83FF);
        context.drawCenteredTextWithShadow(textRenderer,
                GhostText.rtl("کلاینت سبک فارسی | نسخه ۰.۱.۲"),
                width / 2, t + 30, 0xFFF4EDFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
