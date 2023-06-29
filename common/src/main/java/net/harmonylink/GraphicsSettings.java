package net.harmonylink;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GraphicsSettings {
    public transient File file;

    @SerializedName("renderDistance")
    public HLSimpleOption<Integer> renderDistance;
    @SerializedName("simulationDistance")
    public HLSimpleOption<Integer> simulationDistance;
    @SerializedName("GraphicsMode")
    public HLSimpleOption<GraphicsStatus> GraphicsMode;
    @SerializedName("BiomeBlendRadius")
    public HLSimpleOption<Integer> BiomeBlendRadius;
    @SerializedName("guiScale")
    public HLSimpleOption<Integer> guiScale;

    public GraphicsSettings(String FileName) {

        file = new File(HarmonyLinkExpectPlatform.getConfigDirectory() + "/HarmonyLink/" + FileName);

        this.renderDistance = new HLSimpleOption<>(Minecraft.getInstance().options.renderDistance().get());
        this.simulationDistance = new HLSimpleOption<>(Minecraft.getInstance().options.simulationDistance().get());
        this.GraphicsMode = new HLSimpleOption<>(Minecraft.getInstance().options.graphicsMode().get());
        this.BiomeBlendRadius = new HLSimpleOption<>(Minecraft.getInstance().options.biomeBlendRadius().get());
        this.guiScale = new HLSimpleOption<>(Minecraft.getInstance().options.guiScale().get());

        createFileIfNotExists(file);
        loadOptions(file);
    }

    public void loadOptions(File configFile) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(configFile)) {
            GraphicsSettings loadedSettings = gson.fromJson(reader, GraphicsSettings.class);

            if (loadedSettings == null) {
                HarmonyLink.LOGGER.error("Error loading options: Unable to deserialize settings from JSON.");
                return;
            }

            // Copy values from loadedSettings to the current instance
            this.renderDistance = loadedSettings.renderDistance;
            this.simulationDistance = loadedSettings.simulationDistance;
            this.GraphicsMode = loadedSettings.GraphicsMode;
            this.BiomeBlendRadius = loadedSettings.BiomeBlendRadius;
            this.guiScale = loadedSettings.guiScale;

            HarmonyLink.LOGGER.info("Options loaded successfully.");

        } catch (IOException e) {
            HarmonyLink.LOGGER.error("Error loading options: " + e.getMessage());
        }
    }



    public void saveSettingsToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this, writer);
            HarmonyLink.LOGGER.info("Options saved successfully.");
        } catch (IOException e) {
            HarmonyLink.LOGGER.error("Error saving options: " + e.getMessage());
        }
    }

    public void saveAndApply() {
        saveSettingsToFile();
        ApplySettings();
    }

    private void createFileIfNotExists(File filePath) {
        File parentDirectory = filePath.getParentFile();
        if (!parentDirectory.exists()) {
            if (!parentDirectory.mkdirs()) {
                throw new RuntimeException("Error creating directories: " + parentDirectory.getAbsolutePath());
            }
        }

        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error creating file: " + filePath.getAbsolutePath(), e);
            }
        }
    }

    public void ApplySettings() {
        Minecraft.getInstance().options.renderDistance().set(renderDistance.get());
        Minecraft.getInstance().options.simulationDistance().set(simulationDistance.get());
        Minecraft.getInstance().options.biomeBlendRadius().set(BiomeBlendRadius.get());
        Minecraft.getInstance().options.graphicsMode().set(GraphicsMode.get());
        Minecraft.getInstance().options.guiScale().set(guiScale.get());
    }

}
