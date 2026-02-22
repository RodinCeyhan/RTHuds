package com.rtc.client.gui;

import static net.minecraft.client.gui.screens.worldselection.CreateWorldScreen.TAB_HEADER_BACKGROUND;

import java.util.Set;

import com.rtc.client.hud.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import static com.rtc.client.gui.SettingsOptions.*;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SettingsScreen extends Screen {
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private final Screen lastScreen;

    @Nullable
    private TabNavigationBar tabNavigationBar;

    private Button resetButton;

    private int currentTabIndex = 0;

    public SettingsScreen(Screen lastScreen) {
        super(Component.literal("RTHuds"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        this.tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width)
                .addTabs(new GeneralTab(), new AppearanceTab(), new ColorTab(), new ImpressionTab())
                .build();
        this.addRenderableWidget(this.tabNavigationBar);

        LinearLayout linearlayout =
                this.layout.addToFooter(LinearLayout.horizontal().spacing(8));

        this.resetButton = Button.builder(
                Component.translatable("rthuds.settings.reset.general"),
                button -> handleReset()
        ).build();

        linearlayout.addChild(this.resetButton);

        linearlayout.addChild(
                Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                        .build()
        );

        this.layout.visitWidgets(widget -> {
            widget.setTabOrderGroup(1);
            this.addRenderableWidget(widget);
        });

        this.tabNavigationBar.selectTab(this.currentTabIndex, false);
        this.repositionElements();
    }

    private void handleReset() {
        var current = this.tabManager.getCurrentTab();

        if (current instanceof GeneralTab) {
            resetGeneralSettings();
            this.currentTabIndex = 0;
        } else if (current instanceof AppearanceTab) {
            resetAppearanceSettings();
            this.currentTabIndex = 1;
        } else if (current instanceof ColorTab) {
            resetColorSettings();
            this.currentTabIndex = 2;
        } else if (current instanceof ImpressionTab) {
            resetImpressionSettings();
            this.currentTabIndex = 3;
        }

        this.rebuildWidgets();
    }

    private void resetGeneralSettings() {
        SHOW_HUD.set(true);
        SHOW_ARMOR_HUD.set(true);
        SHOW_DEBUG_SCREEN.set(true);
        SHOW_HIDE_GUI.set(true);
    }

    private void resetAppearanceSettings() {
        INFO_HUD_LAYOUT.set(ModConfig.Layout.LAYOUT_1);
        ARMORHUD_LOCATION.set(ModConfig.ArmorHUDLocation.LEFTCENTER);
        INFOHUD_TEXT_SHADOW.set(true);
        ARMORHUD_TEXT_SHADOW.set(true);
        ARMORHUD_HEALTHTEXT.set(ModConfig.ArmorDurabilityEnum.REMAINING);
        SHOW_DURABILITYBAR.set(true);
        SHOW_EMPTYSLOTS.set(true);
        SHOW_HANDITEMS.set(true);
        DECIMAL_PLACES.set(6);
        ARMOR_SCALE.set(0.50);
        ARMOR_BACKGROUND_STYLE.set(1);
        INFO_BACKGROUND_STYLE.set(1);
        INFO_HUD_X.set(0);
        INFO_HUD_Y.set(0);
    }

    private void resetColorSettings() {
        COORDINATES_COLOR.set(ModConfig.HudColor.WHITE);
        YAW_COLOR.set(ModConfig.HudColor.WHITE);
        PITCH_COLOR.set(ModConfig.HudColor.WHITE);
        DIRECTION_COLOR.set(ModConfig.HudColor.WHITE);
        FPS_COLOR.set(ModConfig.HudColor.WHITE);
        NETHERCOORDS_COLOR.set(ModConfig.HudColor.WHITE);
        ARMOR_HEALTH_COLOR.set(ModConfig.HudColor.WHITE);
    }

    private void resetImpressionSettings() {
        SHOW_COORDINATES.set(true);
        SHOW_YAWPITCH.set(true);
        SHOW_DIRECTION.set(true);
        SHOW_FPS.set(true);
        SHOW_NETHER_COORDINATES.set(true);
    }

    @Override
    public void repositionElements() {
        if (this.tabNavigationBar != null) {
            this.tabNavigationBar.setWidth(this.width);
            this.tabNavigationBar.arrangeElements();
            var bottom = this.tabNavigationBar.getRectangle().bottom();
            var screenrectangle = new ScreenRectangle(0, bottom, this.width, this.height - this.layout.getFooterHeight() - bottom);
            this.tabManager.setTabArea(screenrectangle);
            this.layout.setHeaderHeight(bottom);
            this.layout.arrangeElements();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int p_283640_, int p_281243_, float p_282743_) {
        super.render(guiGraphics, p_283640_, p_281243_, p_282743_);
        guiGraphics.blit(Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
    }

    @Override
    protected void renderMenuBackground(GuiGraphics guiGraphics) {
        guiGraphics.blit(TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
        this.renderMenuBackground(guiGraphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
    }

    public void addToAppearanceTab(GridLayout.RowHelper rowHelper) {
        rowHelper.addChild(createWidget(INFO_HUD_LAYOUT));
        rowHelper.addChild(createWidget(ARMORHUD_LOCATION));
        rowHelper.addChild(createWidget(INFOHUD_TEXT_SHADOW));
        rowHelper.addChild(createWidget(ARMORHUD_TEXT_SHADOW));
        rowHelper.addChild(createWidget(ARMORHUD_HEALTHTEXT));
        rowHelper.addChild(createWidget(SHOW_DURABILITYBAR));
        rowHelper.addChild(createWidget(SHOW_EMPTYSLOTS));
        rowHelper.addChild(createWidget(SHOW_HANDITEMS));
        rowHelper.addChild(createWidget(DECIMAL_PLACES));
        rowHelper.addChild(createWidget(ARMOR_SCALE));
        rowHelper.addChild(createWidget(ARMOR_BACKGROUND_STYLE));
        rowHelper.addChild(createWidget(INFO_BACKGROUND_STYLE));
        rowHelper.addChild(createWidget(INFO_HUD_X));
        rowHelper.addChild(createWidget(INFO_HUD_Y));

    }

    public void addToColorTab(GridLayout.RowHelper rowHelper) {
        rowHelper.addChild(createWidget(COORDINATES_COLOR));
        rowHelper.addChild(createWidget(YAW_COLOR));
        rowHelper.addChild(createWidget(PITCH_COLOR));
        rowHelper.addChild(createWidget(DIRECTION_COLOR));
        rowHelper.addChild(createWidget(FPS_COLOR));
        rowHelper.addChild(createWidget(NETHERCOORDS_COLOR));
        rowHelper.addChild(createWidget(ARMOR_HEALTH_COLOR));
    }

    public void addToImpressionTab(GridLayout.RowHelper rowHelper) {
        rowHelper.addChild(createWidget(SHOW_COORDINATES));
        rowHelper.addChild(createWidget(SHOW_YAWPITCH));
        rowHelper.addChild(createWidget(SHOW_DIRECTION));
        rowHelper.addChild(createWidget(SHOW_FPS));
        rowHelper.addChild(createWidget(SHOW_NETHER_COORDINATES));
    }

    public static AbstractWidget createWidget(OptionInstance<?> option) {
        return option.createButton(Minecraft.getInstance().options);
    }

    public static AbstractWidget createWidget(OptionInstance<?> option, Set<OptionInstance.OptionInstanceSliderButton<?>> set) {
        AbstractWidget widget = createWidget(option);

        if (widget instanceof OptionInstance.OptionInstanceSliderButton<?> sliderButton) {
            set.add(sliderButton);
        }

        return widget;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.lastScreen);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class GeneralTab extends GridLayoutTab {
        GeneralTab() {
            super(Component.translatable("rthuds.settings.generaltitle"));
            var rowHelper = this.layout.columnSpacing(3).rowSpacing(3).createRowHelper(2);
            rowHelper.addChild(createWidget(SHOW_HUD));
            rowHelper.addChild(createWidget(SHOW_ARMOR_HUD));
            rowHelper.addChild(createWidget(SHOW_DEBUG_SCREEN));
            rowHelper.addChild(createWidget(SHOW_HIDE_GUI));
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class AppearanceTab extends GridLayoutTab {
        AppearanceTab() {
            super(Component.translatable("rthuds.config.category.appearance"));
            var rowHelper = this.layout.columnSpacing(3).rowSpacing(3).createRowHelper(2);
            addToAppearanceTab(rowHelper);
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class ColorTab extends GridLayoutTab {
        ColorTab() {
            super(Component.translatable("rthuds.settings.colors"));
            var rowHelper = this.layout.columnSpacing(3).rowSpacing(3).createRowHelper(2);
            addToColorTab(rowHelper);
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class ImpressionTab extends GridLayoutTab {
        ImpressionTab() {
            super(Component.translatable("rthuds.config.category.impression"));
            var rowHelper = this.layout.columnSpacing(3).rowSpacing(3).createRowHelper(2);
            addToImpressionTab(rowHelper);
        }
    }
}
