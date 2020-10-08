package com.bespectacled.customstars.mixin;

import org.spongepowered.asm.mixin.Mixin;
import com.bespectacled.customstars.config.CustomStarsConfig;

import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

import net.minecraft.client.render.WorldRenderer;


@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
    
    private static float BASE_SIZE = CustomStarsConfig.loadConfig().base_size;
    private static float MAX_SIZE_MULT = CustomStarsConfig.loadConfig().max_size_multiplier;
    private static int STAR_COUNT = CustomStarsConfig.loadConfig().star_count;
    
	@ModifyConstant(
        method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V",
        constant = @Constant(floatValue = 0.15f)
    )
    private float modifyStarBaseSize(float baseSize) {
        return BASE_SIZE;
    }
	
	@ModifyConstant(
        method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V",
        constant = @Constant(floatValue = 0.1f)
    )
    private float modifyMaxSizeMultiplier(float sizeModifier) {
        return MAX_SIZE_MULT;
    }
	
	@ModifyConstant(
        method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V",
        constant = @Constant(intValue = 1500)
    )
	private int modifyStarCount(int starCount) {
	    return STAR_COUNT;
	}
	
}
