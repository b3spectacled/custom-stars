package com.bespectacled.customstars.mixin;

import java.util.Random;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.bespectacled.customstars.CustomStars;
import com.bespectacled.customstars.config.CustomStarsConfig;
import com.bespectacled.customstars.noise.OctaveSimplexNoise;
import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.world.gen.ChunkRandom;

@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {

    @Unique private CustomStarsConfig STARS_CONFIG = CustomStars.STARS_CONFIG;
    
    @Shadow private VertexBuffer starsBuffer;
    
    @Inject(method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V", at = @At("HEAD"), cancellable = true)
    private void injectRenderStarsWithNoise(BufferBuilder builder, CallbackInfo info) {
        if (STARS_CONFIG.starNoise) {
            this.renderStarsWithNoise(
                builder, 
                STARS_CONFIG.starCount, 
                STARS_CONFIG.baseSize, 
                STARS_CONFIG.maxSizeMultiplier,
                STARS_CONFIG.starNoiseSeed,
                STARS_CONFIG.starNoisePercentage,
                STARS_CONFIG.starNoiseThreshold
            );
            info.cancel();
        }
    }
 
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

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 3))
    private void modifyStarColor(float r, float g, float b, float a) {
        float red = r * STARS_CONFIG.red / 255F;
        float green = g * STARS_CONFIG.green / 255F;
        float blue = b * STARS_CONFIG.blue / 255F;
        float alpha = a * STARS_CONFIG.alpha;

        RenderSystem.setShaderColor(red, green, blue, alpha);
    }
    
    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLjava/lang/Runnable;)V", at = @At("HEAD"))
    private void reloadStars(CallbackInfo info) {
        if (CustomStars.STARS_RERENDER_OBSERVER.update()) {
            CustomStars.LOGGER.log(Level.INFO, "[Custom Stars] Star settings modified, reloading buffer...");
            
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder builder = tess.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionShader);

            this.starsBuffer = new VertexBuffer();
            ((MixinWorldRendererInvoker)this).rerenderStars(builder);
           
            builder.end();
            this.starsBuffer.upload(builder);
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
    
    @Unique
    private void renderStarsWithNoise(
        BufferBuilder buffer,
        int starCount, 
        float baseSize,
        float sizeModifier, 
        long seed, 
        int noisePercentage,
        double noiseThreshold
    ) {
        Random random = new Random(10842L);
        OctaveSimplexNoise fieldSampler = new OctaveSimplexNoise(new ChunkRandom(seed), 3);
        
        // Cap noise threshold
        if (noiseThreshold > 1.0)
            noiseThreshold = 1.0;
        
        // Make a portion of the stars noise-based,
        // the rest, vanilla randomized.
        int noiseStarCount = (int)Math.floor(starCount * noisePercentage / 100D);
        
        double[] ipts = new double[starCount];
        double[] jpts = new double[starCount];
        double[] kpts = new double[starCount];
        
        int stars = 0;
        while (stars < noiseStarCount) {
            double i = random.nextFloat() * 2.0f - 1.0f;
            double j = random.nextFloat() * 2.0f - 1.0f;
            double k = random.nextFloat() * 2.0f - 1.0f;
            
            double weight = fieldSampler.sample(i, j, k); 
            
            if (weight + random.nextDouble() * 0.2 > noiseThreshold) {
                ipts[stars] = i;
                jpts[stars] = j;
                kpts[stars] = k;

                stars++;
            }
        }
        
        while (stars < starCount) {
            ipts[stars] = random.nextFloat() * 2.0f - 1.0f;
            jpts[stars] = random.nextFloat() * 2.0f - 1.0f;
            kpts[stars] = random.nextFloat() * 2.0f - 1.0f;

            stars++;
        }
        
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        
        for (int i = 0; i < starCount; ++i) {
            double double5 = ipts[i];
            double double7 = jpts[i];
            double double9 = kpts[i];
            
            double double11 = baseSize + random.nextFloat() * sizeModifier;
            double double13 = double5 * double5 + double7 * double7 + double9 * double9;
            if (double13 < 1.0 && double13 > 0.01) {
                double13 = 1.0 / Math.sqrt(double13);
                double5 *= double13;
                double7 *= double13;
                double9 *= double13;
                double double15 = double5 * 100.0;
                double double17 = double7 * 100.0;
                double double19 = double9 * 100.0;
                double double21 = Math.atan2(double5, double9);
                double double23 = Math.sin(double21);
                double double25 = Math.cos(double21);
                double double27 = Math.atan2(Math.sqrt(double5 * double5 + double9 * double9), double7);
                double double29 = Math.sin(double27);
                double double31 = Math.cos(double27);
                double double33 = random.nextDouble() * 3.141592653589793 * 2.0;
                double double35 = Math.sin(double33);
                double double37 = Math.cos(double33);
                for (int v = 0; v < 4; ++v) {
                    double double42 = ((v & 0x2) - 1) * double11;
                    double double44 = ((v + 1 & 0x2) - 1) * double11;
                    double double48 = double42 * double37 - double44 * double35;
                    double double52;
                    double52 = double44 * double37 + double42 * double35;
                    double double54 = double48 * double29 + 0.0 * double31;
                    double double56 = 0.0 * double29 - double48 * double31;
                    double double58 = double56 * double23 - double52 * double25;
                    double double60 = double54;
                    double double62 = double52 * double23 + double56 * double25;
                    buffer.vertex(double15 + double58, double17 + double60, double19 + double62).next();
                }
            }
        }
    }
}
