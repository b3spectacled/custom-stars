package com.bespectacled.customstars.mixin;

import org.spongepowered.asm.mixin.Mixin;
import com.bespectacled.customstars.config.CustomStarsConfig;
import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;


@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
    
    private static float BASE_SIZE = CustomStarsConfig.loadConfig().base_size;
    private static float MAX_SIZE_MULT = CustomStarsConfig.loadConfig().max_size_multiplier;
    private static int STAR_COUNT = CustomStarsConfig.loadConfig().star_count;
    private static int RED = CustomStarsConfig.loadConfig().red;
    private static int GREEN = CustomStarsConfig.loadConfig().green;
    private static int BLUE = CustomStarsConfig.loadConfig().blue;
    private static float ALPHA = CustomStarsConfig.loadConfig().alpha;
    
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
	
	@Redirect(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;color4f(FFFF)V", ordinal = 1)
    )
    private void modifyStarColor(float r, float g, float b, float a) {
	    float red = r * RED / 255F;
	    float green = g * GREEN / 255F;
	    float blue = b * BLUE / 255F;
	    float alpha = a * ALPHA;
	    
	    RenderSystem.color4f(red, green, blue, alpha);
    }
	
	/*
	@Redirect(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;method_23787(F)F")
    )
    private float modifyStarTime(ClientWorld self, float tickDelta) {
       return self.method_23787(tickDelta) * (1.0f - self.getRainGradient(tickDelta));
    }
    */
}
