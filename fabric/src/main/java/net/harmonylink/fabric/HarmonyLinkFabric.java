package net.harmonylink.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.harmonylink.HarmonyLink;

public class HarmonyLinkFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HarmonyLink HL = HarmonyLink.init();

        ClientLifecycleEvents.CLIENT_STARTED.register(HL::initializeSettings);
        ClientTickEvents.END_CLIENT_TICK.register(HL::OnTick);
    }
}
