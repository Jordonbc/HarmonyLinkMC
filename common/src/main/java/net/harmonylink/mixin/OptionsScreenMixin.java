package net.harmonylink.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.harmonylink.screen.SettingsMenu;
import net.harmonylink.HarmonyLink;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Shadow @Final private Screen lastScreen;
    @Shadow @Final private Options options;

    protected int tickCount = 0;

    protected OptionsScreenMixin(Component component) {
        super(component);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo info) {
        System.out.println("Hello from example architectury common mixin!");

        if (this.lastScreen != null && !(this.lastScreen instanceof SettingsMenu))
        {
            if (HarmonyLink.getIsConnected())
            {
                this.addRenderableWidget(Button.builder(Component.literal("HL"), button ->  {
                    Minecraft.getInstance().setScreen(new SettingsMenu(this));
                }).size(25, 25).build());
            }
        }

    }
}