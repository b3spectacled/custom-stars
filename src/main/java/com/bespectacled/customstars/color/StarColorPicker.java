package com.bespectacled.customstars.color;

import java.util.Random;

import com.bespectacled.customstars.CustomStars;
import com.bespectacled.customstars.config.CustomStarsConfig;
import com.bespectacled.customstars.config.CustomStarsConfig.CustomStarColor;

public class StarColorPicker {
    private static final CustomStarsConfig STARS_CONFIG = CustomStars.STARS_CONFIG;
    private static final Random RANDOM = new Random();
    
    public static CustomStarColor nextColor() {
        int starColorCustomSize = STARS_CONFIG.starColorCustom.size();
        
        return switch(STARS_CONFIG.starColorType) {
            case SINGLE -> STARS_CONFIG.starColorSingle;
            case CUSTOM -> STARS_CONFIG.starColorCustom.get(RANDOM.nextInt(starColorCustomSize));
            case RANDOM -> new CustomStarColor(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextFloat());
        };
    }
}
