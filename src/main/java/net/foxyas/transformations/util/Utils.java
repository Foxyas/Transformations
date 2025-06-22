package net.foxyas.transformations.util;

import net.minecraft.resources.ResourceLocation;

public class Utils {

    public static final ResourceLocation NULL_LOC = ResourceLocation.fromNamespaceAndPath("null", "null");

    public static float rotlerpRad(float angle, float maxAngle, float mul) {
        float f = (mul - maxAngle) % (float) (Math.PI * 2);
        if (f < (float) -Math.PI) {
            f += (float) (Math.PI * 2);
        }

        if (f >= (float) Math.PI) {
            f -= (float) (Math.PI * 2);
        }

        return maxAngle + angle * f;
    }

    public static float quadraticArmUpdate(float limbSwing) {
        return -65.0F * limbSwing + limbSwing * limbSwing;
    }
}
