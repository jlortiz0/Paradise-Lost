package net.id.aether.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.jetbrains.annotations.Nullable;

import static net.id.aether.Aether.locate;

public record SlideDensityFunction(@Nullable GenerationShapeConfig settings, DensityFunction input) implements DensityFunction {
    public static final Codec<SlideDensityFunction> CODEC = DensityFunction.FUNCTION_CODEC.fieldOf("argument")
            .xmap(densityFunction -> new SlideDensityFunction(null, densityFunction),
                    SlideDensityFunction::input)
            .codec();
    public static final CodecHolder<SlideDensityFunction> CODEC_HOLDER = CodecHolder.of(CODEC);

    @Override
    public double sample(DensityFunction.NoisePos pos) {
        return this.method_40518(pos, this.input().sample(pos));
    }

    @Override
    public void method_40470(double[] ds, DensityFunction.class_6911 arg) {
        this.input().method_40470(ds, arg);
        for (int i = 0; i < ds.length; ++i) {
            ds[i] = this.method_40518(arg.method_40477(i), ds[i]);
        }
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public double method_40518(DensityFunction.NoisePos noisePos, double d) {
        if (this.settings == null) {
            return d;
        }

        double f = noisePos.blockY() / this.settings.verticalBlockSize() - MathHelper.floorDiv(this.settings.minimumY(), this.settings.verticalBlockSize());
        d = method_38414(-0.154, 28.0, 2.2, d, (this.settings.height() / this.settings.verticalBlockSize()) - f);
        d = method_38414(-0.375, 32.0, 1.3, d, f);
        return d;
    }

    private static double method_38414(double target, double size, double offset, double d, double e) {
        double f = (e - offset) / size;
        return MathHelper.clampedLerp(target, d, f);
    }

    @Override
    public DensityFunction apply(DensityFunction.DensityFunctionVisitor visitor) {
        return visitor.apply(new SlideDensityFunction(this.settings, this.input.apply(visitor)));
    }

    @Override
    public double minValue() {
        if (this.settings == null) {
            return this.input.minValue();
        }
        return Math.min(this.input.minValue(), -0.375);
    }

    @Override
    public double maxValue() {
        if (this.settings == null) {
            return this.input.maxValue();
        }
        return Math.max(this.input.maxValue(), -0.154);
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodec() {
        return CODEC_HOLDER;
    }

    public static void init() {
        Registry.register(Registry.DENSITY_FUNCTION_TYPE, locate("slide"), CODEC);
    }
}
