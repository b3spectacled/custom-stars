package mod.bespectacled.customstars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mod.bespectacled.customstars.config.CustomStarsConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.ActionResult;

public class CustomStars implements ModInitializer {
    public static final String MOD_ID = "custom_stars";
    public static final String MOD_NAME = "Custom Stars";
    
    public static final CustomStarsConfig STARS_CONFIG = AutoConfig.register(CustomStarsConfig.class, GsonConfigSerializer::new).getConfig();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static boolean reloadStars = false;

    public static void log(Level level, String message) {
        LOGGER.atLevel(level).log("[" + MOD_NAME + "] {}", message);
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
