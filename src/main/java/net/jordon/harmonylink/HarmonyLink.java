package net.jordon.harmonylink;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.logging.LogUtils;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;

import net.minecraftforge.client.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;



@Mod(HarmonyLink.MOD_ID)
public class HarmonyLink
{
    private SystemInfo systemInfo;


    public static final String MOD_ID = "harmonylink";
    private static final Logger LOGGER = LogUtils.getLogger();

    private int tickCount = 0;

    public HarmonyLink()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.player.level.isClientSide) // We only want to do this on the client side
        {
            if (++tickCount >= 20) // Increase the tick count and check if we've reached 20 yet (1-second timer)
            {
                tickCount = 0; // Reset the tick count for next time

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:9000/all_info"))
                        .build();

                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(this::handleResponse);
            }
        }
    }


    private void handleResponse(String jsonResponse)
    {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        LOGGER.info("JSON Response: {}", this.systemInfo);

        if (this.systemInfo != gson.fromJson(jsonResponse, SystemInfo.class))
        {
            SystemInfo newSystemInfo = gson.fromJson(jsonResponse, SystemInfo.class);
            if (newSystemInfo.battery_info.hasBattery)
            {
                if (newSystemInfo.battery_info.chargingStatus == ChargingStatus.Battery)
                { setRenderDistance(4); Minecraft.getInstance().options.simulationDistance().set(4); Minecraft.getInstance().options.graphicsMode().set(GraphicsStatus.FAST); Minecraft.getInstance().options.biomeBlendRadius().set(0); }

                else if (newSystemInfo.battery_info.chargingStatus == ChargingStatus.Charging)
                { setRenderDistance(12); Minecraft.getInstance().options.simulationDistance().set(12); Minecraft.getInstance().options.graphicsMode().set(GraphicsStatus.FANCY); Minecraft.getInstance().options.biomeBlendRadius().set(2); }
            }

            this.systemInfo = newSystemInfo;
        }
    }

    private void setRenderDistance(int distance)
    {
        // Check if the game is running on the client side
        if(Minecraft.getInstance().player != null && Minecraft.getInstance().player.level.isClientSide)
        {
            LOGGER.info("Setting render distance to {}", distance);
            Minecraft.getInstance().options.renderDistance().set(distance);
        }
        else
        {
            LOGGER.warn("Attempted to set render distance from server side, this is not supported.");
        }
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
