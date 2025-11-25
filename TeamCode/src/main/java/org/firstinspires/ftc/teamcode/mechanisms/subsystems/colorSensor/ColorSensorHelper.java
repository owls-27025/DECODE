package org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor;

import android.graphics.Color;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Configuration;

public class ColorSensorHelper {
    private static final float GREEN_HUE_CENTER = 130f; // green
    private static final float PURPLE_HUE_CENTER = 200f; // purple
    private static final float HUE_TOL_DEG = 20f; // accuracy
    private static final float MIN_SAT = 0.35f; // ignore gray
    private static final float MIN_VAL = 0.15f; // ignore very dark / too far
    private static final int   STABLE_N = 3; // frames required to confirm

    public static Rev2mDistanceSensor colorSensor;


    public static void init(HardwareMap hw) {
        if (Configuration.distance.itemActive) {
            colorSensor = hw.get(Rev2mDistanceSensor.class, Configuration.distance.itemName);
        }
    }

    public static String getColor() {
//        Sample s1 = readSample(colorSensor);
//
//        float h, s, v;
//
//        h = s1.h; s = s1.s; v = s1.v;
//
        String guess = "Neither";
//
//        if (h > 155 && h < 200) {
//            guess = "Green";
//        } else if (h < 155) {
//            guess = "Neither";
//        } else {
//            guess = "Purple";
//        }
        return guess;
    }

    // ---- helpers ----
    private static float hueDist(float a, float b) {
        float d = Math.abs(a - b) % 360f;
        return (d > 180f) ? 360f - d : d;
    }

    private static int clamp255(int x) { return Math.max(0, Math.min(255, x)); }

    private static int[] scaleTo255(int r, int g, int b) {
        int max = Math.max(r, Math.max(g, b));
        if (max <= 255 && max > 0) {
            return new int[] { clamp255(r), clamp255(g), clamp255(b) };
        } else if (max == 0) {
            return new int[] {0, 0, 0};
        } else {
            double scale = 255.0 / max;
            return new int[] {
                    clamp255((int)Math.round(r * scale)),
                    clamp255((int)Math.round(g * scale)),
                    clamp255((int)Math.round(b * scale))
            };
        }
    }

    private static int safeAlpha(ColorSensor s) {
        try {
            return s.alpha();
        } catch (Throwable t) {
            return Math.max(s.red(), Math.max(s.green(), s.blue()));
        }
    }

    public static Sample readSample(ColorSensor s) {
        int r = s.red();
        int g = s.green();
        int b = s.blue();
        int a = safeAlpha(s);

        int[] rgb255 = scaleTo255(r, g, b);
        float[] hsv = new float[3];
        Color.RGBToHSV(rgb255[0], rgb255[1], rgb255[2], hsv);

        Sample out = new Sample();
        out.r = r; out.g = g; out.b = b; out.a = a;
        out.r255 = rgb255[0]; out.g255 = rgb255[1]; out.b255 = rgb255[2];
        out.h = hsv[0]; out.s = hsv[1]; out.v = hsv[2];
        return out;
    }

    public static final class Sample {
        int r, g, b, a;
        int r255, g255, b255;
        public float h;
        float s;
        float v;
    }

    public static boolean isBall() {
        return colorSensor.getDistance(DistanceUnit.MM) < 130;
    }
}
