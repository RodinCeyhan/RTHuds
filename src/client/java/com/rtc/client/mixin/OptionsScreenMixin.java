package com.rtc.client.mixin;

import com.rtc.client.gui.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.rtc.client.RTHudsClient.MOD_ID;

@SuppressWarnings({"SpellCheckingInspection", "IfStatementWithIdenticalBranches", "unused", "FieldCanBeLocal", "SuspiciousNameCombination"})
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    @Shadow
    @Final
    private HeaderAndFooterLayout layout;

    @Unique
    Minecraft client = Minecraft.getInstance();

    @Unique
    SpriteIconButton rthudssettingsbtn = SpriteIconButton.builder(
                    Component.translatable("rthuds"),
                    (buttonWidget) -> client.setScreen(ConfigScreen.create(client.screen)), true)
            .sprite(Identifier.fromNamespaceAndPath(MOD_ID, "icon/" + MOD_ID), 9, 9).size(20, 20).build();

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
            if (element instanceof Button b) {
                buttons.add(b);
            }
        }

        if (buttons.isEmpty()) return;

        Button telemetryButton = null;
        Button resourcePackButton = null;
        for (Button b : buttons) {
            if (b.getMessage().equals(Component.translatable("options.telemetry"))) {
                telemetryButton = b;
                break;
            }
        }
        for (Button b : buttons) {
            if (b.getMessage().equals(Component.translatable("options.resourcepack"))) {
                resourcePackButton = b;
                break;
            }
        }

        if (telemetryButton == null) {
            if (resourcePackButton == null) return;

            int newHeight = resourcePackButton.getHeight();
            this.addRenderableWidget(rthudssettingsbtn);
            rthuds$updateButtonPosition();
        } else {
            int newHeight = telemetryButton.getHeight();
            this.addRenderableWidget(rthudssettingsbtn);
            rthuds$updateButtonPosition();
        }
    }

    @Inject(method = "repositionElements", at = @At("TAIL"))
    private void onResize(CallbackInfo ci) {
        rthuds$updateButtonPosition();
    }

    @Unique
    private void rthuds$updateButtonPosition() {
        if (this.rthudssettingsbtn == null) return;

        List<Button> buttons = new ArrayList<>();
        for (var element : this.children()) {
            if (element instanceof Button b) {
                buttons.add(b);
            }
        }

        Button telemetryButton = null;
        Button resourcePackButton = null;
        for (Button b : buttons) {
            if (b.getMessage().equals(Component.translatable("options.telemetry"))) {
                telemetryButton = b;
                break;
            }
        }
        for (Button b : buttons) {
            if (b.getMessage().equals(Component.translatable("options.resourcepack"))) {
                resourcePackButton = b;
                break;
            }
        }

        if (telemetryButton == null) {
            if (resourcePackButton == null) return;

            int targetX = resourcePackButton.getX();
            int tolerance = 6;

            List<Button> sameColumn = new ArrayList<>();
            for (Button b : buttons) {
                if (Math.abs(b.getX() - targetX) <= tolerance) {
                    sameColumn.add(b);
                }
            }

            sameColumn.sort(Comparator.comparingInt(Button::getY));

            int gap = 4;
            int index = sameColumn.indexOf(resourcePackButton);
            if (index >= 0 && index < sameColumn.size() - 1) {
                Button below = sameColumn.get(index + 1);
                gap = below.getY() - (resourcePackButton.getY() + resourcePackButton.getHeight());
            }

            int newHeight = resourcePackButton.getHeight();
            int newX = resourcePackButton.getX() - gap - 20;
            int newY = resourcePackButton.getY();

            this.rthudssettingsbtn.setPosition(newX, newY);
            this.rthudssettingsbtn.setWidth(newHeight);
            this.rthudssettingsbtn.setHeight(newHeight);

        } else {
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

            this.rthudssettingsbtn.setPosition(newX, newY);
            this.rthudssettingsbtn.setWidth(newHeight);
            this.rthudssettingsbtn.setHeight(newHeight);
        }
    }
}