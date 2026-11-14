package dev.ghostclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class GhostScreen extends Screen {
    private static final int PANEL = 0xE8101010;
    private static final int PANEL_EDGE = 0xFF777777;
    private static final int PRIMARY = 0xFFF2F2F2;
    private static final int MUTED = 0xFFB8B8B8;

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
        }).dimensions(cx - 88, cy - 5, 176, 20).build());

        addDrawableChild(ButtonWidget.builder(fa("بستن"), button -> close())
                .dimensions(cx - 88, cy + 22, 176, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int left = width / 2 - 112;
        int top = height / 2 - 66;
        int right = left + 224;
        int bottom = top + 128;

        // تم سیاه‌وسفید با ظاهر ساده و بدون انیمیشن‌های تزئینی
        context.fill(left, top, right, bottom, PANEL);
        context.fill(left, top, right, top + 1, PANEL_EDGE);
        context.fill(left, bottom - 1, right, bottom, PANEL_EDGE);
        context.fill(left, top, left + 1, bottom, PANEL_EDGE);
        context.fill(right - 1, top, right, bottom, PANEL_EDGE);

        context.drawCenteredTextWithShadow(textRenderer, "GHOST CLIENT",
                width / 2, top + 11, PRIMARY);
        context.drawCenteredTextWithShadow(textRenderer,
                GhostText.rtl("تم: سیاه‌وسفید  |  انیمیشن: حداقلی"),
                width / 2, top + 29, MUTED);
        context.drawCenteredTextWithShadow(textRenderer,
                GhostText.rtl("تنظیمات کلاینت"),
                width / 2, top + 49, PRIMARY);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
