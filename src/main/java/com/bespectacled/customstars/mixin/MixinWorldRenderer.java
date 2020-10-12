package com.bespectacled.customstars.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.bespectacled.customstars.CustomStars;
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
    
    @Unique
    private CustomStarsConfig CONFIG = CustomStars.CONFIG; 
    
	@ModifyConstant(
        method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V",
        constant = @Constant(floatValue = 0.15f)
    )
    private float modifyStarBaseSize(float baseSize) {
        return CONFIG.baseSize;
    }
	
	@ModifyConstant(
        method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V",
        constant = @Constant(floatValue = 0.1f)
    )
    private float modifyMaxSizeMultiplier(float sizeModifier) {
        return CONFIG.maxSizeMultiplier;
    }
	
	@ModifyConstant(
        method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V",
        constant = @Constant(intValue = 1500)
    )
	private int modifyStarCount(int starCount) {
	    return CONFIG.starCount;
	}
	
	@Redirect(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;color4f(FFFF)V", ordinal = 1)
    )
    private void modifyStarColor(float r, float g, float b, float a) {
	    float red = r * CONFIG.red / 255F;
	    float green = g * CONFIG.green / 255F;
	    float blue = b * CONFIG.blue / 255F;
	    float alpha = a * CONFIG.alpha;
	    
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
