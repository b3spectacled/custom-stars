package com.bespectacled.customstars.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@SuppressWarnings("deprecation")
@Environment(EnvType.CLIENT)
public class CustomStarsModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return screen -> AutoConfig.getConfigScreen(CustomStarsConfig.class, screen).get();
    }
}
