package com.rtc.client.gui;

import static net.minecraft.client.gui.screens.worldselection.CreateWorldScreen.TAB_HEADER_BACKGROUND;

import java.util.Set;

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

@SuppressWarnings("unused")
public class SettingsScreen extends Screen {
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private final Screen lastScreen;

    @Nullable
    private TabNavigationBar tabNavigationBar;

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
        linearlayout.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                .build());
        this.layout.visitWidgets(widget -> {
            widget.setTabOrderGroup(1);
            this.addRenderableWidget(widget);
        });
        this.tabNavigationBar.selectTab(0, false);
        this.repositionElements();
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
        rowHelper.addChild(createWidget(SettingsOptions.INFOHUD_LOCATION));
        rowHelper.addChild(createWidget(SettingsOptions.ARMORHUD_LOCATION));
        rowHelper.addChild(createWidget(SettingsOptions.INFOHUD_TEXT_SHADOW));
        rowHelper.addChild(createWidget(SettingsOptions.ARMORHUD_TEXT_SHADOW));
        rowHelper.addChild(createWidget(SettingsOptions.BACKGROUND_STYLE));
        rowHelper.addChild(createWidget(SettingsOptions.DECIMAL_PLACES));
        rowHelper.addChild(createWidget(SettingsOptions.SHOW_DURABILITYBAR));
        rowHelper.addChild(createWidget(SettingsOptions.SHOW_EMPTYSLOTS));
        rowHelper.addChild(createWidget(SettingsOptions.SHOW_HANDITEMS));
        rowHelper.addChild(createWidget(SettingsOptions.ARMOR_SCALE));
        rowHelper.addChild(createWidget(SettingsOptions.ARMORHUD_HEALTHTEXT));
        rowHelper.addChild(createWidget(SettingsOptions.INFO_HUD_LAYOUT));

    }

    public void addToColorTab(GridLayout.RowHelper rowHelper) {
        rowHelper.addChild(createWidget(SettingsOptions.COORDINATES_COLOR));
        rowHelper.addChild(createWidget(SettingsOptions.YAW_COLOR));
        rowHelper.addChild(createWidget(SettingsOptions.PITCH_COLOR));
        rowHelper.addChild(createWidget(SettingsOptions.DIRECTION_COLOR));
        rowHelper.addChild(createWidget(SettingsOptions.FPS_COLOR));
        rowHelper.addChild(createWidget(SettingsOptions.NETHERCOORDS_COLOR));
        rowHelper.addChild(createWidget(SettingsOptions.ARMOR_HEALTH_COLOR));
    }

    public void addToImpressionTab(GridLayout.RowHelper rowHelper) {
        rowHelper.addChild(createWidget(SettingsOptions.SHOW_COORDINATES));
        rowHelper.addChild(createWidget(SettingsOptions.SHOW_YAWPITCH));
        rowHelper.addChild(createWidget(SettingsOptions.SHOW_DIRECTION));
        rowHelper.addChild(createWidget(SettingsOptions.SHOW_FPS));
        rowHelper.addChild(createWidget(SettingsOptions.SHOW_NETHER_COORDINATES));
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
            rowHelper.addChild(createWidget(SettingsOptions.SHOW_DEBUG_SCREEN));
            rowHelper.addChild(createWidget(SettingsOptions.SHOW_HIDE_GUI));
            rowHelper.addChild(createWidget(SettingsOptions.SHOW_HUD));
            rowHelper.addChild(createWidget(SettingsOptions.SHOW_ARMOR_HUD));
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
