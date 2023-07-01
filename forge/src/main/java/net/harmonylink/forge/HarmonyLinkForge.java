package net.harmonylink.forge;

import dev.architectury.platform.forge.EventBuses;
import net.harmonylink.HarmonyLink;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HarmonyLink.MOD_ID)
public class HarmonyLinkForge {
    private final HarmonyLink harmonyLink;
    public HarmonyLinkForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(HarmonyLink.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        harmonyLink = HarmonyLink.init();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLClientSetupEvent event) {
        harmonyLink.initializeSettings(Minecraft.getInstance());
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            harmonyLink.OnTick(Minecraft.getInstance());
        }
    }
}
