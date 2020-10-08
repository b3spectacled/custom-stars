package com.bespectacled.customstars.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bespectacled.customstars.CustomStars;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.Level;

public final class CustomStarsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Paths.get("config", "customstars.json");
    
    //public boolean render_old_stars;
    public static final int CURRENT_VERSION = 0;
    public final int VERSION;
    
    public final float base_size;
    public final float max_size_multiplier;
    public final int star_count;
    
    public CustomStarsConfig() {
        this.VERSION = CURRENT_VERSION;
        
        this.base_size = 0.15f;
        this.max_size_multiplier = 0.1f;
        this.star_count = 1500;
    }
    
    public static CustomStarsConfig loadConfig() {
        return readConfig();
    }
    
    private static CustomStarsConfig createConfig() {
        CustomStarsConfig config = new CustomStarsConfig();
        
        try {
            if (!Files.exists(PATH))
                Files.createDirectories(PATH.getParent());
            
            FileWriter writer = new FileWriter(PATH.toFile());
            writer.write(GSON.toJson(config));
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return config;
    }
    
    private static CustomStarsConfig readConfig() {
        CustomStarsConfig config = null;
        
        try {
            BufferedReader bufferedReader = new BufferedReader(
                new FileReader(PATH.toFile())
            );
            
            config = GSON.fromJson(bufferedReader, CustomStarsConfig.class);
        }
        catch (IOException e) {
            CustomStars.LOGGER.log(Level.WARN, "Config file not found, creating...");
            createConfig();
        }
        
        if (config == null || config.VERSION != CURRENT_VERSION) {
            CustomStars.LOGGER.log(Level.WARN, "Missing or outdated config, recreating...");
            config = createConfig();
        }
        
        return config;
    }

}