package com.rtc.client.mixin;

import com.rtc.client.gui.SettingsScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unused")
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    @Unique
    private static final ResourceLocation SETTINGS = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/settings_9x8.png");

    @Unique
    public Button RTHButton = null;

    @Mutable
    @Unique
    @Final
    private Screen parent;

    protected OptionsScreenMixin(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addCustomButtonBelowTelemetry(CallbackInfo ci) {

        List<Button> buttons = new ArrayList<>();
        for (var element : this.children()) {
            if (element instanceof Button) {
                buttons.add((Button) element);
            }
        }

        if (buttons.isEmpty()) return;

        Button telemetryButton = null;
        for (Button b : buttons) {
            if (b.getMessage().equals(Component.translatable("options.telemetry"))) {
                telemetryButton = b;
                break;
            }
        }

        if (telemetryButton == null) return;

        int targetX = telemetryButton.getX();
        int tolerance = 6;
        List<Button> sameColumn = new ArrayList<>();
        for (Button b : buttons) {
            if (Math.abs(b.getX() - targetX) <= tolerance) {
                sameColumn.add(b);
            }
        }
        sameColumn.sort(Comparator.comparingInt(Button::getY));

        int gap = 4;
        int index = sameColumn.indexOf(telemetryButton);
        if (index >= 0 && index < sameColumn.size() - 1) {
            Button below = sameColumn.get(index + 1);
            gap = below.getY() - (telemetryButton.getY() + telemetryButton.getHeight());
        }

        int newHeight = telemetryButton.getHeight();
        int newX = telemetryButton.getX() - gap - 20;
        int newY = telemetryButton.getY();

        this.RTHButton = Button.builder(
                        Component.literal(""),
                        (button) -> Minecraft.getInstance().setScreen(new SettingsScreen(this.parent))
                ).bounds(newX, newY, newHeight, newHeight)
                .tooltip(Tooltip.create(Component.translatable("rthuds.settings.title")))
                .build();

        this.addRenderableWidget(this.RTHButton);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int textureWidth = 9;
        int textureHeight = 9;

        int centerX = this.RTHButton.getX() + (this.RTHButton.getWidth() / 2);
        int centerY = this.RTHButton.getY() + (this.RTHButton.getHeight() / 2);

        int drawX = centerX - (textureWidth / 2);
        int drawY = centerY - (textureHeight / 2);
        guiGraphics.blit(SETTINGS, drawX, drawY, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
}
