package net.harmonylink.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.harmonylink.HarmonyLink;
import net.harmonylink.RangedValue;
import net.harmonylink.GraphicsSettings;
import net.harmonylink.screen.widgets.HLSliderWidget;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OptionsMenu extends Screen {
    private final Screen lastScreen;
    private final Component title;

    private GraphicsSettings settings;

    public OptionsMenu(Screen parent, String title, String fileName) {
        super(Component.literal("Options Menu"));
        this.lastScreen = parent;
        this.title = Component.literal(title + " Options");
    }

    public OptionsMenu(Screen parent, String title, GraphicsSettings settings) {
        super(Component.literal("Options Menu"));
        this.lastScreen = parent;
        this.settings = settings;
        this.title = Component.literal(title + " Options");
    }

    @Override
    protected void init() {
        super.init();
        initWidgets();
    }

    private void initWidgets() {
        int buttonWidth = 150;

        HarmonyLink.LOGGER.info("Initialising Widgets");

        Button backButton = Button.builder(Component.literal("Back"), button -> Minecraft.getInstance().setScreen(lastScreen))
                .size(30, 20)
                .pos(0, 0)
                .build();
        this.addRenderableWidget(backButton);

        GridLayout gridWidget = new GridLayout();

        gridWidget.columnSpacing(20);
        gridWidget.rowSpacing(5);

        gridWidget.addChild(Button.builder(Component.literal("Graphics: " + settings.GraphicsMode.get().toString()), button -> {
            GraphicsStatus currentMode = settings.GraphicsMode.get();
            switch (currentMode) {
                case FABULOUS -> settings.GraphicsMode.set(GraphicsStatus.FAST);
                case FANCY -> settings.GraphicsMode.set(GraphicsStatus.FABULOUS);
                case FAST -> settings.GraphicsMode.set(GraphicsStatus.FANCY);
            }

            String newMode = settings.GraphicsMode.get().toString();
            newMode = newMode.substring(0, 1).toUpperCase() + newMode.substring(1).toLowerCase();

            button.setMessage(Component.literal("Graphics: " + newMode));
            settings.saveAndApply();
            })
        .build(), 0, 0);


        HLSliderWidget renderDistanceSlider = new HLSliderWidget(0, 0, 150, 20, Component.literal("Render Distance: " + settings.renderDistance.get()),
                new RangedValue(2, 32, settings.renderDistance.get().doubleValue()),
                (slider, value) -> {
                    int intValue = (int) Math.round(value);
                    HarmonyLink.LOGGER.info("Value: {}", intValue);
                    slider.setMessage(Component.literal("Render Distance: " + intValue));
                    settings.renderDistance.set(intValue);
                    settings.saveAndApply();
                }
        );


        gridWidget.addChild(renderDistanceSlider, 1, 0);

        HLSliderWidget simulationDistanceSlider = new HLSliderWidget(0, 0, 150, 20, Component.literal("Simulation Distance: " + settings.simulationDistance.get()),
                new RangedValue(5, 32, settings.simulationDistance.get().doubleValue()),
                (slider, value) -> {
                    int intValue = (int) Math.round(value);
                    HarmonyLink.LOGGER.info("Value: {}", intValue);
                    slider.setMessage(Component.literal("Simulation Distance: " + intValue));
                    settings.simulationDistance.set(intValue);
                    settings.saveAndApply();
                }
        );
        gridWidget.addChild(simulationDistanceSlider, 0, 1);

        HLSliderWidget biomeBlendRadiusSlider = new HLSliderWidget(0, 0, 150, 20, Component.literal("Biomes Blend Range: " + settings.BiomeBlendRadius.get()),
                new RangedValue(0, 7, settings.BiomeBlendRadius.get().doubleValue()),
                (slider, value) -> {
                    int intValue = (int) Math.round(value);
                    HarmonyLink.LOGGER.info("Value: {}", intValue);
                    if (intValue == 0) {
                        slider.setMessage(Component.literal("Biomes Blend Range: OFF"));
                    } else {
                        slider.setMessage(Component.literal("Biomes Blend Range: " + intValue));
                    }

                    settings.BiomeBlendRadius.set(intValue);
                    settings.saveAndApply();
                }
        );
        gridWidget.addChild(biomeBlendRadiusSlider, 1, 1);

        HLSliderWidget guiSlider = new HLSliderWidget(0, 0, 150, 20, Component.literal("GUI Scale: " + settings.guiScale.get()),
                new RangedValue(0, 2, settings.guiScale.get().doubleValue()),
                (slider, value) -> {
                    int intValue = (int) Math.round(value);
                    HarmonyLink.LOGGER.info("Value: {}", intValue);
                    if (intValue == 0) {
                        slider.setMessage(Component.literal("GUI Scale: AUTO"));
                    } else {
                        slider.setMessage(Component.literal("GUI Scale: " + intValue));
                    }
                    settings.guiScale.set(intValue);
                    settings.saveAndApply();
                }
        );
        gridWidget.addChild(guiSlider, 2, 1);


        // Position and add the grid to the screen
        gridWidget.arrangeElements();
        int gridWidth = gridWidget.getWidth();
        int gridX = (width - gridWidth) / 2; // Calculate the x-position to center the grid
        gridWidget.setPosition(gridX, 40);

        // Remove this line: this.addRenderableWidget(gridWidget);

        // Add the child widgets directly to the screen
        gridWidget.visitChildren(child -> {
            if (child instanceof GuiEventListener && child instanceof Renderable && child instanceof NarratableEntry) {
                this.addRenderableWidget((GuiEventListener & Renderable & NarratableEntry) child);
            }
        });

        HarmonyLink.LOGGER.info("Widgets Initialised!");
    }

    @Override
    public void resize(Minecraft client, int width, int height) {
        super.resize(client, width, height);
        initWidgets();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack); // Renders the default screen background
        super.render(poseStack, mouseX, mouseY, delta);

        int titleX = width / 2;
        int titleY = 20;

        drawCenteredString(poseStack, this.font, title, titleX, titleY, 0xFFFFFF);
    }
}
