package mod.bespectacled.customstars.config;

import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import mod.bespectacled.customstars.color.StarColorType;
import mod.bespectacled.customstars.moon.MoonDeadzoneShape;

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
    @ConfigEntry.Gui.Tooltip(count = 2)
    public double starNoiseFuzzWeight = 0.2;
    
    @ConfigEntry.Category(value = "starsNoise")
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.BoundedDiscrete(max = 100, min = 0)
    public int starNoisePercentage = 50;
    
    @ConfigEntry.Category(value = "starsColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public float starBrightness = 1.0f;
    
    @ConfigEntry.Category(value = "starsColor")
    public StarColorType starColorType = StarColorType.SINGLE;
    
    @ConfigEntry.Category(value = "starsColor")
    @ConfigEntry.Gui.CollapsibleObject
    public ColorRGBA starColorSingle = new ColorRGBA(255, 255, 255, 1.0f);

    @ConfigEntry.Category(value = "starsColor")
    public List<ColorRGBA> starColorCustom = List.of(
        new ColorRGBA(255, 0, 0, 1.0f),
        new ColorRGBA(0, 255, 0, 1.0f),
        new ColorRGBA(0, 0, 255, 1.0f)
    );
    
    @ConfigEntry.Category(value = "moon")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean moonDeadzone = false;
    
    @ConfigEntry.Category(value = "moon")
    public MoonDeadzoneShape moonDeadzoneShape = MoonDeadzoneShape.SQUARE;
    
    @ConfigEntry.Category(value = "moon")
    public double moonDeadzoneSize = 5.0;
    
    @ConfigEntry.Category(value = "moon")
    @ConfigEntry.Gui.TransitiveObject
    public ColorRGBA moonColor = new ColorRGBA(255, 255, 255, 1.0f);
    
    @ConfigEntry.Category(value = "skyColor")
    @ConfigEntry.Gui.TransitiveObject
    public ColorRGBA skyColor = new ColorRGBA(0, 0, 0, 1.0f);
    
    @ConfigEntry.Category(value = "endColor")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public float endSize = 1.0f;
    
    @ConfigEntry.Category(value = "endColor")
    @ConfigEntry.Gui.TransitiveObject
    public ColorRGBA endColor = new ColorRGBA(40, 40, 40, 1.0f);

    public static class ColorRGBA {
        @ConfigEntry.BoundedDiscrete(max = 255)
        public int red;
        
        @ConfigEntry.BoundedDiscrete(max = 255)
        public int green;
        
        @ConfigEntry.BoundedDiscrete(max = 255)
        public int blue;
        
        public float alpha;
        
        public ColorRGBA() {
            this(255, 255, 255, 1.0f);
        }
        
        public ColorRGBA(int red, int green, int blue, float alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
        
        public float normalR() {
            return this.red / 255f;
        }
        
        public float normalG() {
            return this.green / 255f;
        }
        
        public float normalB() {
            return this.blue / 255f;
        }

        @Override
        public String toString() {
            return String.format("%d/%d/%d/%f", this.red, this.green, this.blue, this.alpha);
        }
    }
}
