package com.bespectacled.customstars.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "customstars")
public class CustomStarsConfig implements ConfigData {

    @ConfigEntry.Category(value = "starsBasic")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public float baseSize = 0.15f;

    @ConfigEntry.Category(value = "starsBasic")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public float maxSizeMultiplier = 0.1f;

    @ConfigEntry.Category(value = "starsBasic")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int starCount = 1500;

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
}
