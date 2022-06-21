package com.bespectacled.customstars.config;

import java.util.List;

import com.bespectacled.customstars.color.StarColorType;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "customstars")
public class CustomStarsConfig implements ConfigData {

    @ConfigEntry.Category(value = "starsBasic")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public float baseSize = 0.15f;

    @ConfigEntry.Category(value = "starsBasic")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public float maxSizeMultiplier = 0.1f;

    @ConfigEntry.Category(value = "starsBasic")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public int starCount = 1500;
    
    @ConfigEntry.Category(value = "starsBasic")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public float starBrightness = 1.0f;
    
    @ConfigEntry.Category(value = "starsNoise")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean starNoise = false;
    
    @ConfigEntry.Category(value = "starsNoise")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public long starNoiseSeed = 10842L;
    
    @ConfigEntry.Category(value = "starsNoise")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public double starNoiseThreshold = 0.5;
    
    @ConfigEntry.Category(value = "starsNoise")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 100, min = 0)
    public int starNoisePercentage = 50;
    
    @ConfigEntry.Category(value = "starsColor")
    public StarColorType starColorType = StarColorType.SINGLE;
    
    @ConfigEntry.Category(value = "starsColor")
    @ConfigEntry.Gui.CollapsibleObject
    public CustomStarColor starColorSingle = new CustomStarColor(255, 255, 255, 1.0f);

    @ConfigEntry.Category(value = "starsColor")
    public List<CustomStarColor> starColorCustom = List.of(
        new CustomStarColor(255, 0, 0, 1.0f),
        new CustomStarColor(0, 255, 0, 1.0f),
        new CustomStarColor(0, 0, 255, 1.0f)
    );

    @ConfigEntry.Category(value = "skyColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 255)
    public int skyRed = 0;
    
    @ConfigEntry.Category(value = "skyColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 255)
    public int skyGreen = 0;
    
    @ConfigEntry.Category(value = "skyColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 255)
    public int skyBlue = 0;
    
    @ConfigEntry.Category(value = "endColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public float endSize = 1.0f;
    
    @ConfigEntry.Category(value = "endColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 255)
    public int endRed = 40;

    @ConfigEntry.Category(value = "endColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 255)
    public int endGreen = 40;

    @ConfigEntry.Category(value = "endColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 255)
    public int endBlue = 40;
    
    @ConfigEntry.Category(value = "endColor")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public float endAlpha = 1.0f;

    public static class CustomStarColor {
        @ConfigEntry.BoundedDiscrete(max = 255)
        public int red;
        
        @ConfigEntry.BoundedDiscrete(max = 255)
        public int green;
        
        @ConfigEntry.BoundedDiscrete(max = 255)
        public int blue;
        
        public float alpha;
        
        public CustomStarColor() {
            this(255, 255, 255, 1.0f);
        }
        
        public CustomStarColor(int red, int green, int blue, float alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
        
        @Override
        public String toString() {
            return String.format("%d/%d/%d/%f", this.red, this.green, this.blue, this.alpha);
        }
    }
}
