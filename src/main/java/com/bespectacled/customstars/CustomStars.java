package com.bespectacled.customstars;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.customstars.config.CustomStarsConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;

import com.bespectacled.customstars.client.GoVote;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class CustomStars implements ModInitializer {
    public static final String ID = "custom_stars";
    public static final Logger LOGGER = LogManager.getLogger("CustomStars");
    public static final CustomStarsConfig CONFIG = AutoConfig.register(CustomStarsConfig.class, GsonConfigSerializer::new).getConfig();
    
    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing Custom Stars...");
        
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            GoVote.init();
        }
        
        LOGGER.log(Level.INFO, "Initialized Custom Stars!");
    }
}
