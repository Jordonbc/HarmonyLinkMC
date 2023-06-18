package net.jordon.harmonylink;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.GsonHelper;
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
            if (++tickCount >= 20*2) // Increase the tick count and check if we've reached 20 yet
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

        SystemInfo systemInfo = gson.fromJson(jsonResponse, SystemInfo.class);

        this.systemInfo = systemInfo;

        LOGGER.info("JSON Response: {}", this.systemInfo);

        if (this.systemInfo.battery_info.hasBattery)
        {
            if (this.systemInfo.battery_info.chargingStatus == ChargingStatus.BATTERY)
            {
                setRenderDistance(4);
            }
            else if (this.systemInfo.battery_info.chargingStatus == ChargingStatus.CHARGING)
            {
                setRenderDistance(12);
            }
        }
    }

    private void setRenderDistance(int distance)
    {
        // Check if the game is running on the client side
        if(FMLLoader.getDist() == Dist.CLIENT)
        {
            //Minecraft.getInstance().;
            // Get the current Minecraft instance and adjust the render distance
            //Minecraft.getInstance().options.renderDistance = distance;
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
