package com.bespectacled.customstars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.bespectacled.customstars.config.CustomStarsConfig;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.ActionResult;

public class CustomStars implements ModInitializer {
    public static final String MOD_ID = "custom_stars";
    public static final String MOD_NAME = "Custom Stars";
    
    public static final CustomStarsConfig STARS_CONFIG = AutoConfig.register(CustomStarsConfig.class, GsonConfigSerializer::new).getConfig();
    
    private static final Logger LOGGER = LoggerFactory.getLogger("CustomStars");
    
    private static boolean reloadStars = false;

    public static void log(Level level, String message) {
        switch(level) {
            case DEBUG: LOGGER.debug(message);
            case ERROR: LOGGER.error(message);
            case WARN: LOGGER.warn(message);
            default: LOGGER.info(message);
        }
    }
    
    public static boolean shouldReloadStars() {
        if (reloadStars) {
            reloadStars = false;
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing Custom Stars...");
        
        // Register config save listener, for star rerendering.
        AutoConfig.getConfigHolder(CustomStarsConfig.class).registerSaveListener((manager, data) -> {
            reloadStars = true;
            
            return ActionResult.SUCCESS;
        });
    }
}
