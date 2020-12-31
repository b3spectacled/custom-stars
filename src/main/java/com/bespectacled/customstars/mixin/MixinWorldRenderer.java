package com.bespectacled.customstars.mixin;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.bespectacled.customstars.CustomStars;
import com.bespectacled.customstars.config.CustomStarsConfig;
import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;

@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {

    @Unique
    private CustomStarsConfig STARS_CONFIG = CustomStars.STARS_CONFIG;
    
    @Unique
    private float prevStarBaseSize = STARS_CONFIG.baseSize;
    
    @Unique
    private float prevSizeModifier = STARS_CONFIG.maxSizeMultiplier;
    
    @Unique
    private float prevStarCount = STARS_CONFIG.starCount;
    
    @Shadow
    private VertexBuffer starsBuffer;
    
    @Shadow
    private VertexFormat skyVertexFormat;
 
    @ModifyConstant(method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V", constant = @Constant(floatValue = 0.15f))
    private float modifyStarBaseSize(float baseSize) {
        return STARS_CONFIG.baseSize;
    }

    @ModifyConstant(method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V", constant = @Constant(floatValue = 0.1f))
    private float modifyMaxSizeMultiplier(float sizeModifier) {
        return STARS_CONFIG.maxSizeMultiplier;
    }

    @ModifyConstant(method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V", constant = @Constant(intValue = 1500))
    private int modifyStarCount(int starCount) {
        return STARS_CONFIG.starCount;
    }

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;color4f(FFFF)V", ordinal = 1))
    private void modifyStarColor(float r, float g, float b, float a) {
        float red = r * STARS_CONFIG.red / 255F;
        float green = g * STARS_CONFIG.green / 255F;
        float blue = b * STARS_CONFIG.blue / 255F;
        float alpha = a * STARS_CONFIG.alpha;

        RenderSystem.color4f(red, green, blue, alpha);
    }
    
    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("HEAD"))
    private void reloadStars(CallbackInfo info) {
        if (this.prevStarBaseSize != STARS_CONFIG.baseSize ||
            this.prevSizeModifier != STARS_CONFIG.maxSizeMultiplier ||
            this.prevStarCount != STARS_CONFIG.starCount) {
            
            CustomStars.LOGGER.log(Level.INFO, "Star settings modified, reloading buffer...");
            
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder builder = tess.getBuffer();

            this.starsBuffer = new VertexBuffer(this.skyVertexFormat);
            ((MixinWorldRendererInvoker)this).rerenderStars(builder);
           
            builder.end();
            this.starsBuffer.upload(builder);
            
            this.prevStarBaseSize = STARS_CONFIG.baseSize;
            this.prevSizeModifier = STARS_CONFIG.maxSizeMultiplier;
            this.prevStarCount = STARS_CONFIG.starCount;
        }
    }
    
    /* End Sky */
    @Redirect(method = "renderEndSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;"))
    private VertexConsumer modifyEndSkyColor(VertexConsumer self, int r, int g, int b, int a) {
        return self.color(STARS_CONFIG.endRed, STARS_CONFIG.endGreen, STARS_CONFIG.endBlue, (int)(a * STARS_CONFIG.endAlpha));
    }
    
    @ModifyConstant(method = "renderEndSky", constant = @Constant(floatValue = 16.0f))
    private float modifyTextureSize(float size) {
        return size * STARS_CONFIG.endSize;
    }
}
