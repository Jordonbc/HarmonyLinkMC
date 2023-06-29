package net.harmonylink;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.harmonylink.API.BatteryInfo;
import net.harmonylink.API.ChargingStatus;
import net.harmonylink.API.DockInfo;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HarmonyLink {
    public static final String MOD_ID = "harmonylink";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static HLSettings HLSETTINGS;
    public static GraphicsSettings batterySettings;
    public static GraphicsSettings chargingSettings;
    public static GraphicsSettings dockedSettings;

    private static Boolean isConnected;
    private BatteryInfo batteryInfo;
    private DockInfo dockInfo;
    private int tickCount = 0;

    public static HarmonyLink init() {
        HarmonyLink HL = new HarmonyLink();

        HLSETTINGS = new HLSettings("HarmonyLink.json");
        isConnected = false;

        LOGGER.info(HarmonyLinkExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
        return HL;
    }

    public void OnTick(Minecraft Client) {
        if (++tickCount >= 20) {  // Increase the tick count and check if we've reached 20 yet (1-second timer)
            tickCount = 0; // Reset the tick count for next time

            HttpClient httpclient = HttpClient.newHttpClient();

            HttpRequest checkConnectionRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:9000/v1/are_you_there"))
                    .build();

            httpclient.sendAsync(checkConnectionRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAcceptAsync(this::handleConnectedResponse)
                    .exceptionally(this::handleConnectedFailure);

            if (getIsConnected() && !Client.isPaused() && Client.level != null) {  // We don't want to execute the code if the game is paused

                HttpRequest batteryRequest = HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:9000/v1/battery_info"))
                        .build();

                httpclient.sendAsync(batteryRequest, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAcceptAsync(this::handleBatteryResponse);


                HttpRequest dockedRequest = HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:9000/v1/dock_info"))
                        .build();

                httpclient.sendAsync(dockedRequest, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAcceptAsync(this::handleDockResponse);
            }
        }
    }

    public void initializeSettings(Minecraft client) {
        batterySettings = new GraphicsSettings("Battery.json");
        chargingSettings = new GraphicsSettings("Charging.json");
        dockedSettings = new GraphicsSettings("Docked.json");
    }

    private void handleConnectedResponse(String Response)
    {
        LOGGER.info("Received String: {}", Response);
        if (Response != null && !Response.equals(""))
        {
            isConnected = Response.equals("yes");
        }else {
            isConnected = false;
        }

        LOGGER.info("setting isConnected: {}", isConnected);
    }

    public Void handleConnectedFailure(Throwable ex) {
        // Handle the connection failure exception
        //System.err.println("Failed to connect: " + ex.getMessage());
        // Perform any additional error handling or fallback actions
        isConnected = false;
        return null;
    }

    private void handleBatteryResponse(String jsonResponse)
    {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        LOGGER.info("JSON Response: {}", this.batteryInfo);

        if (this.batteryInfo != gson.fromJson(jsonResponse, BatteryInfo.class))
        {
            BatteryInfo newBatteryInfo = gson.fromJson(jsonResponse, BatteryInfo.class);
            if (newBatteryInfo.hasBattery)
            {
                if (newBatteryInfo.chargingStatus == ChargingStatus.Battery)
                {
                    batterySettings.ApplySettings();
                }

                else if (newBatteryInfo.chargingStatus == ChargingStatus.Charging)
                {
                    chargingSettings.ApplySettings();
                }
            }

            this.batteryInfo = newBatteryInfo;
        }
    }

    private void handleDockResponse(String jsonResponse)
    {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        LOGGER.info("JSON Response: {}", this.dockInfo);

        if (this.dockInfo != gson.fromJson(jsonResponse, DockInfo.class))
        {
            DockInfo newDockInfo = gson.fromJson(jsonResponse, DockInfo.class);

            if (newDockInfo.isDocked)
            {
                dockedSettings.ApplySettings();
            }

            this.dockInfo = newDockInfo;
        }
    }

    public static Boolean getIsConnected()
    {
        return isConnected;
    }
}
