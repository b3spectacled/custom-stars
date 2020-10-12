package com.bespectacled.customstars.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

public class CustomStarsModMenu implements ModMenuApi {
    @Override
    public String getModId() {
        return "customstars";
    }

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return screen -> AutoConfig.getConfigScreen(CustomStarsConfig.class, screen).get();
    }
}
