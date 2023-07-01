package net.harmonylink.fabric;

import net.harmonylink.HarmonyLinkExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class HarmonyLinkExpectPlatformImpl {
    /**
     * This is our actual method to {@link HarmonyLinkExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
