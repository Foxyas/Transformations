package net.foxyas.transformations.util;

public class Utils {

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
