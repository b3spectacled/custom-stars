package com.bespectacled.customstars.config;

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
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 255)
    public int red = 255;

    @ConfigEntry.Category(value = "starsColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 255)
    public int green = 255;

    @ConfigEntry.Category(value = "starsColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 255)
    public int blue = 255;

    @ConfigEntry.Category(value = "starsColor")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public float alpha = 1.0f;
    
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

}
