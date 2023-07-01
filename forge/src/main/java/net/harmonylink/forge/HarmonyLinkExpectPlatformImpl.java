package net.harmonylink.forge;

import net.harmonylink.HarmonyLinkExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class HarmonyLinkExpectPlatformImpl {
    /**
     * This is our actual method to {@link HarmonyLinkExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
