package com.bespectacled.customstars;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.customstars.config.CustomStarsConfig;
import com.bespectacled.customstars.config.CustomStarsConfigObserver;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class CustomStars implements ModInitializer {
    public static final String MOD_ID = "custom_stars";
    public static final String MOD_NAME = "Custom Stars";
    
    public static final CustomStarsConfig STARS_CONFIG = AutoConfig.register(CustomStarsConfig.class, GsonConfigSerializer::new).getConfig();
    public static final CustomStarsConfigObserver STARS_RERENDER_OBSERVER = new CustomStarsConfigObserver();
    
    private static final Logger LOGGER = LogManager.getLogger("CustomStars");

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] {}", message);
    }
    
    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing Custom Stars...");
        
        // Register config option observables, for star rerendering
        STARS_RERENDER_OBSERVER.registerObservable("baseSize", () -> STARS_CONFIG.baseSize);
        STARS_RERENDER_OBSERVER.registerObservable("sizeModifier", () -> STARS_CONFIG.maxSizeMultiplier);
        STARS_RERENDER_OBSERVER.registerObservable("starCount", () -> STARS_CONFIG.starCount);
        STARS_RERENDER_OBSERVER.registerObservable("starNoise", () -> STARS_CONFIG.starNoise);
        STARS_RERENDER_OBSERVER.registerObservable("starNoiseSeed", () -> STARS_CONFIG.starNoiseSeed);
        STARS_RERENDER_OBSERVER.registerObservable("starNoisePercentage", () -> STARS_CONFIG.starNoisePercentage);
        STARS_RERENDER_OBSERVER.registerObservable("starNoiseThreshold", () -> STARS_CONFIG.starNoiseThreshold);
    }
}
