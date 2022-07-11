package com.bespectacled.customstars.mixin;

import java.util.Random;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.bespectacled.customstars.CustomStars;
import com.bespectacled.customstars.color.StarColorPicker;
import com.bespectacled.customstars.config.CustomStarsConfig;
import com.bespectacled.customstars.config.CustomStarsConfig.CustomStarColor;
import com.bespectacled.customstars.noise.OctaveSimplexNoise;
import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;

@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
    @Unique private static final CustomStarsConfig STARS_CONFIG = CustomStars.STARS_CONFIG;
    
    @Shadow private VertexBuffer starsBuffer;

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 3))
    private void modifyStarColor(float r, float g, float b, float a) {
        RenderSystem.setShaderColor(r, g, b, a * STARS_CONFIG.starBrightness);
    }
    
    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getPositionShader()Lnet/minecraft/client/render/Shader;"))
    private Shader modifyStarDraw(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera arg3, boolean bl, Runnable runnable) {
        return GameRenderer.getPositionColorShader();
    }
    
    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At("HEAD"))
    private void reloadStars(CallbackInfo info) {
        if (CustomStars.shouldReloadStars()) {
            CustomStars.log(Level.INFO, "Settings modified, reloading buffer...");
            
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder builder = tess.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            this.starsBuffer = new VertexBuffer();
            BufferBuilder.BuiltBuffer builtBuffer = this.renderCustomStars(builder);

            this.starsBuffer.bind();
            this.starsBuffer.upload(builtBuffer);
            VertexBuffer.unbind();
        }
    }
    
    @Inject(method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)Lnet/minecraft/client/render/BufferBuilder$BuiltBuffer;", at = @At("HEAD"), cancellable = true)
    private void injectRenderStars(BufferBuilder builder, CallbackInfoReturnable<BufferBuilder.BuiltBuffer> info) {
        BufferBuilder.BuiltBuffer builtBuffer = this.renderCustomStars(builder);
            
        info.setReturnValue(builtBuffer);
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
    private BufferBuilder.BuiltBuffer renderCustomStars(BufferBuilder buffer) {
        int starCount = STARS_CONFIG.starCount;
        float baseSize = STARS_CONFIG.baseSize;
        float sizeModifier = STARS_CONFIG.maxSizeMultiplier;
        long seed = STARS_CONFIG.starNoiseSeed;
        int noisePercentage = STARS_CONFIG.starNoisePercentage;
        double noiseThreshold = STARS_CONFIG.starNoiseThreshold;    
        
        Random random = new Random(10842L);
        
        double[] ipts = new double[starCount];
        double[] jpts = new double[starCount];
        double[] kpts = new double[starCount];
        
        Xoroshiro128PlusPlusRandom fieldRandom = new Xoroshiro128PlusPlusRandom(seed);
        OctaveSimplexNoise fieldSampler = new OctaveSimplexNoise(fieldRandom, 3);
        
        // Cap noise threshold
        if (noiseThreshold > 1.0)
            noiseThreshold = 1.0;
        
        int stars = 0;
        
        if (STARS_CONFIG.starNoise) {
            // Make a portion of the stars noise-based,
            // the rest, vanilla randomized.
            int noiseStarCount = (int)Math.floor(starCount * noisePercentage / 100D);
            
            while (stars < noiseStarCount) {
                double i = fieldRandom.nextFloat() * 2.0f - 1.0f;
                double j = fieldRandom.nextFloat() * 2.0f - 1.0f;
                double k = fieldRandom.nextFloat() * 2.0f - 1.0f;
                
                // Range approx -1.5 to 1.5
                double weight = fieldSampler.sample(i, j, k);

                double fuzz = (fieldRandom.nextDouble() - 0.5) * 3.0;
                double fuzzWeight = STARS_CONFIG.starNoiseFuzzWeight;
                
                if (weight + fuzz * fuzzWeight > noiseThreshold) {
                    ipts[stars] = i;
                    jpts[stars] = j;
                    kpts[stars] = k;

                    stars++;
                }
            }
        }
        while (stars < starCount) {
            ipts[stars] = random.nextFloat() * 2.0f - 1.0f;
            jpts[stars] = random.nextFloat() * 2.0f - 1.0f;
            kpts[stars] = random.nextFloat() * 2.0f - 1.0f;

            stars++;
        }
        
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        
        double[] xCoords = new double[4];
        double[] yCoords = new double[4];
        double[] zCoords = new double[4];
        
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
                
                CustomStarColor starColor = StarColorPicker.nextColor(random);
                float r = starColor.red / 255F;
                float g = starColor.green / 255F;
                float b = starColor.blue / 255F;
                float a = starColor.alpha;

                boolean inMoon = false;
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
                    
                    double x = double15 + double58;
                    double y = double17 + double60;
                    double z = double19 + double62;

                    if (inMoon(x, z)) {
                        inMoon = true;
                        break;
                    }
                    
                    xCoords[v] = x;
                    yCoords[v] = y;
                    zCoords[v] = z;
                }

                if (!inMoon) {
                    for (int v = 0; v < 4; ++v) {
                        double x = xCoords[v];
                        double y = yCoords[v];
                        double z = zCoords[v];
                        
                        buffer.vertex(x, y, z).color(r, g, b, a).next();
                    }
                }
            }
        }
        
        return buffer.end();
    }
    
    @Unique
    private boolean inMoon(double x, double z) {
        double size = STARS_CONFIG.moonDeadzoneSize;
        
        if (!STARS_CONFIG.moonDeadzone)
            return false;
       
        return STARS_CONFIG.moonDeadzoneShape.test(x, z, size);
    }
}
