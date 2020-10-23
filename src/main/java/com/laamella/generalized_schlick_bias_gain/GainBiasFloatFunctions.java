package com.laamella.generalized_schlick_bias_gain;

/**
 * From the paper "A Convenient Generalization of Schlick’s Bias and Gain Functions", Jonathan T. Barron.
 */
public class GainBiasFloatFunctions {
    public static float biasAndGain(float x, float s, float t) {
        if (x < t)
            return (t * x) / (x + s * (t - x));
        return ((1 - t) * (x - 1)) / (1 - x - s * (t - x)) + 1;
    }

    public static float bias(float x, float bias) {
        return x / ((((1 / bias) - 2) * (1 - x)) + 1);
    }

    public static float gain(float x, float gain) {
        if (x < 0.5)
            return bias(x * 2, gain) / 2;
        return (bias(x * 2 - 1, 1 - gain) + 1) / 2;
    }
}
