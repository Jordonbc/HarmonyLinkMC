package net.harmonylink.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import net.harmonylink.HarmonyLink;

/**
 * This is the Settings Menu screen class.
 * It extends the Screen class of Minecraft.
 */
public class SettingsMenu extends Screen {
    private final Screen lastScreen;  // The parent screen
    private final Component title;  // Title text of the settings menu

    /**
     * SettingsMenu constructor.
     * @param parent The parent screen.
     */
    public SettingsMenu(Screen parent) {
        super(Component.literal("Settings Menu"));
        this.lastScreen = parent;
        this.title = Component.literal("HarmonyLink Settings");
    }

    /**
     * Overriding the init method from the Screen class to initialize the widgets.
     */
    @Override
    protected void init() {
        super.init();
        initWidgets();  // Initialize the settings menu widgets
    }

    /**
     * Method to initialize the widgets of the settings menu.
     */
    private void initWidgets() {
        int buttonWidth = 150;

        // Initialize back button
        Button backButton = Button.builder(Component.literal("Back"), button -> Minecraft.getInstance().setScreen(lastScreen))
                .size(30, 20)
                .pos(0, 0)
                .build();

        this.addRenderableWidget(backButton);

        // Initialize grid widget
        GridLayout gridWidget = new GridLayout();

        gridWidget.columnSpacing(20);
        gridWidget.rowSpacing(5);

        // Add various buttons to the grid

        gridWidget.addChild(Button.builder(Component.literal("Docked Settings: " + HarmonyLink.HLSETTINGS.EnableDocked.get()), button -> {
            Boolean isDocked = HarmonyLink.HLSETTINGS.EnableDocked.get();
            if (isDocked) {
                HarmonyLink.HLSETTINGS.EnableDocked.set(false);
            }else {
                HarmonyLink.HLSETTINGS.EnableDocked.set(true);
            }
            //button.setMessage(Text.of("Docked Settings: " + HarmonyLinkClient.HLSETTINGS.EnableDocked.getValue()));
            HarmonyLink.HLSETTINGS.saveSettingsToFile();
            clearWidgets();
            initWidgets();
        }).build(), 0, 0);

        gridWidget.addChild(Button.builder(Component.literal("Battery"), button -> {
            Minecraft.getInstance().setScreen(new OptionsMenu(this, "Battery", HarmonyLink.batterySettings));
        }).build(), 1, 0);

        gridWidget.addChild(Button.builder(Component.literal("Charging"), button -> {
            Minecraft.getInstance().setScreen(new OptionsMenu(this, "Charging", HarmonyLink.chargingSettings));
        }).build(), 0, 1);

        if (HarmonyLink.HLSETTINGS.EnableDocked.get()) {
            gridWidget.addChild(Button.builder(Component.literal("Docked"), button -> {
                Minecraft.getInstance().setScreen(new OptionsMenu(this, "Docked", HarmonyLink.dockedSettings));
            }).build(), 1, 1);
        }

        // Position and add the grid to the screen
        gridWidget.arrangeElements();
        int gridWidth = gridWidget.getWidth();
        int gridX = (width - gridWidth) / 2; // Calculate the x-position to center the grid
        gridWidget.setPosition(gridX, 40);

        // Add the child widgets directly to the screen
        gridWidget.visitChildren(child -> {
            if (child instanceof GuiEventListener && child instanceof Renderable && child instanceof NarratableEntry) {
                this.addRenderableWidget((GuiEventListener & Renderable & NarratableEntry) child);
            }
        });
    }

    /**
     * Resize method is called when the window size changes.
     * It calls the super method and re-initializes the widgets.
     * @param client The Minecraft client instance.
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
    @Override
    public void resize(Minecraft client, int width, int height) {
        super.resize(client, width, height);
        initWidgets();
    }

    /**
     * Overriding the render method from the Screen class to draw this screen.
     * @param poseStack The MatrixStack instance.
     * @param mouseX The x-coordinate of the mouse cursor.
     * @param mouseY The y-coordinate of the mouse cursor.
     * @param delta The amount of time since the last frame.
     */
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack); // Renders the default screen background
        super.render(poseStack, mouseX, mouseY, delta);

        int titleX = width / 2;
        int titleY = 20;

        drawCenteredString(poseStack, this.font, title, titleX, titleY, 0xFFFFFF);

        //drawCenteredTextWithShadow(matrices, textRenderer, title, titleX, titleY, 0xFFFFFF);
    }
}
