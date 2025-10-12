package org.firstinspires.ftc.teamcode.mechanisms;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ColorSensorHelper {
    private static final float GREEN_HUE_CENTER = 149f; // green
    private static final float PURPLE_HUE_CENTER = 266f; // purple
    private static final float HUE_TOL_DEG = 25f; // accuracy
    private static final float MIN_SAT = 0.35f; // ignore gray
    private static final float MIN_VAL = 0.15f; // ignore very dark / too far
    private static final int   STABLE_N = 3; // frames required to confirm

    public static ColorSensor sensor1;

    private static int greenStreak = 0;
    private static int purpleStreak = 0;
    private static int neitherStreak = 0;
    private static String stableColor = "…";

    public static void init(HardwareMap hw) {
        sensor1 = hw.get(ColorSensor.class, "sensor1");
    }

    public static void init(HardwareMap hw, String name1, String name2) {
        sensor1 = hw.get(ColorSensor.class, name1);
    }

    public static String getColor() {
        Sample s1 = readSample(sensor1);

        float h, s, v;

        h = s1.h; s = s1.s; v = s1.v;

        String guess;
        float dGreen  = hueDist(h, GREEN_HUE_CENTER);
        float dPurple = hueDist(h, PURPLE_HUE_CENTER);

        if (dGreen <= HUE_TOL_DEG && dGreen < dPurple) {
            guess = "Green";
        } else if (dPurple <= HUE_TOL_DEG && dPurple < dGreen) {
            guess = "Purple";
        } else {
            guess = "Neither";
        }

        updateDebounce(guess);
        return stableColor;
    }

    // ---- helpers ----
    private static void updateDebounce(String guess) {
        switch (guess) {
            case "Green":
                greenStreak++; purpleStreak = 0; neitherStreak = 0; break;
            case "Purple":
                purpleStreak++; greenStreak = 0; neitherStreak = 0; break;
            default:
                neitherStreak++; greenStreak = 0; purpleStreak = 0; break;
        }

        if (greenStreak  >= STABLE_N) stableColor = "Green";
        else if (purpleStreak >= STABLE_N) stableColor = "Purple";
        else if (neitherStreak >= STABLE_N) stableColor = "Neither";
    }

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
}
