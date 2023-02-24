package mod.bespectacled.customstars.color;

import java.util.Random;

import mod.bespectacled.customstars.CustomStars;
import mod.bespectacled.customstars.config.CustomStarsConfig;
import mod.bespectacled.customstars.config.CustomStarsConfig.ColorRGBA;

public class StarColorPicker {
    private static final CustomStarsConfig STARS_CONFIG = CustomStars.STARS_CONFIG;

    public static ColorRGBA nextColor(Random random) {
        int starColorCustomSize = STARS_CONFIG.starColorCustom.size();
        
        return switch(STARS_CONFIG.starColorType) {
            case SINGLE -> STARS_CONFIG.starColorSingle;
            case CUSTOM -> STARS_CONFIG.starColorCustom.get(random.nextInt(starColorCustomSize));
            case RANDOM -> new ColorRGBA(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextFloat());
        };
    }
}
