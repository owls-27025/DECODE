package org.firstinspires.ftc.teamcode.shared.helpers;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings({"unused", "SameParameterValue"})
public class OwlsController {
    public enum Button {
        A, B, X, Y,
        LB, RB,
        LT, RT,
        START, BACK,
        LS, RS,
        DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT
    }

    public static final class TouchpadState {
        public boolean available = false;
        public final Finger f1 = new Finger();
        public final Finger f2 = new Finger();
    }

    public static final class Finger {
        public boolean present = false;
        public boolean isTouching = false;
        public float x = 0f;
        public float y = 0f;
    }

    private final Gamepad gp;

    private final Map<Button, Boolean> current = new EnumMap<>(Button.class);
    private final Map<Button, Boolean> previous = new EnumMap<>(Button.class);

    private double prevLT = 0.0;
    private double prevRT = 0.0;

    private boolean inhibitButtons = false;

    private double triggerDigitalThreshold = 0.5;

    public OwlsController(Gamepad gamepad) {
        this.gp = gamepad;

        for (Button b : Button.values()) {
            current.put(b, false);
            previous.put(b, false);
        }
    }

    @SuppressWarnings("unused")
    public void setTriggerDigitalThreshold(double threshold) {
        triggerDigitalThreshold = clamp01(threshold);
    }

    public void update() {
        for (Button b : Button.values()) {
            previous.put(b, current.get(b));
        }

        prevLT = gp.left_trigger;
        prevRT = gp.right_trigger;

        mapButtons();

        if (heldRaw(Button.START) && (heldRaw(Button.A) || heldRaw(Button.B))) {
            inhibitButtons = true;
        }

        if (!heldRaw(Button.A) && !heldRaw(Button.B)) {
            inhibitButtons = false;
        }
    }

    public boolean pressed(Button b) {
        return Boolean.TRUE.equals(current.get(b)) && Boolean.FALSE.equals(previous.get(b)) && !inhibitButtons;
    }

    public boolean held(Button b) {
        return Boolean.TRUE.equals(current.get(b)) && !inhibitButtons;
    }

    @SuppressWarnings("unused")
    public boolean released(Button b) {
        return Boolean.FALSE.equals(current.get(b)) && Boolean.TRUE.equals(previous.get(b));
    }

    public boolean heldRaw(Button b) {
        Boolean v = current.get(b);
        return v != null && v;
    }

    @SuppressWarnings("unused")
    public boolean inhibited() {
        return inhibitButtons;
    }

    public double leftTrigger() {
        return clamp01(gp.left_trigger);
    }

    public double rightTrigger() {
        return clamp01(gp.right_trigger);
    }

    public boolean leftTriggerPressed(double threshold) {
        threshold = clamp01(threshold);
        double now = leftTrigger();
        return (now >= threshold && prevLT < threshold) && !inhibitButtons;
    }

    @SuppressWarnings("unused")
    public boolean leftTriggerReleased(double threshold) {
        threshold = clamp01(threshold);
        double now = leftTrigger();
        return (now < threshold && prevLT >= threshold);
    }

    public boolean rightTriggerPressed(double threshold) {
        threshold = clamp01(threshold);
        double now = rightTrigger();
        return (now >= threshold && prevRT < threshold) && !inhibitButtons;
    }

    @SuppressWarnings("unused")
    public boolean rightTriggerReleased(double threshold) {
        threshold = clamp01(threshold);
        double now = rightTrigger();
        return (now < threshold && prevRT >= threshold);
    }

    public boolean rumble(int milliseconds) {
        try {
            Method m = gp.getClass().getMethod("rumble", int.class);
            m.invoke(gp, milliseconds);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    @SuppressWarnings("unused")
    public boolean rumbleBlips(int blips) {
        try {
            Method m = gp.getClass().getMethod("rumbleBlips", int.class);
            m.invoke(gp, blips);
            return true;
        } catch (Throwable ignored) {
            boolean ok = false;
            for (int i = 0; i < blips; i++) ok |= rumble(120);
            return ok;
        }
    }

    public boolean stopRumble() {
        try {
            Method m = gp.getClass().getMethod("stopRumble");
            m.invoke(gp);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public boolean setLedColor(double r, double g, double b, int durationMs) {
        r = clamp01(r); g = clamp01(g); b = clamp01(b);

        try {
            Method m = gp.getClass().getMethod("setLedColor",
                    double.class, double.class, double.class, int.class);
            m.invoke(gp, r, g, b, durationMs);
            return true;
        } catch (Throwable ignored) { }

        try {
            Method m = gp.getClass().getMethod("setLedColor",
                    double.class, double.class, double.class);
            m.invoke(gp, r, g, b);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public TouchpadState readTouchpad() {
        TouchpadState out = new TouchpadState();

        Object f1 = getGamepadField("touchpad_finger_1");
        Object f2 = getGamepadField("touchpad_finger_2");

        if (f1 != null) fillFinger(out.f1, f1);
        if (f2 != null) fillFinger(out.f2, f2);

        out.available = (f1 != null || f2 != null);
        return out;
    }

    private void mapButtons() {
        current.put(Button.A, gp.a || getBoolFieldSafe(gp, "cross"));
        current.put(Button.B, gp.b || getBoolFieldSafe(gp, "circle"));
        current.put(Button.X, gp.x || getBoolFieldSafe(gp, "square"));
        current.put(Button.Y, gp.y || getBoolFieldSafe(gp, "triangle"));

        current.put(Button.LB, gp.left_bumper);
        current.put(Button.RB, gp.right_bumper);

        current.put(Button.LT, gp.left_trigger >= triggerDigitalThreshold);
        current.put(Button.RT, gp.right_trigger >= triggerDigitalThreshold);

        current.put(Button.LS, gp.left_stick_button);
        current.put(Button.RS, gp.right_stick_button);

        current.put(Button.START, gp.start);
        current.put(Button.BACK, gp.back);

        current.put(Button.DPAD_UP, gp.dpad_up);
        current.put(Button.DPAD_DOWN, gp.dpad_down);
        current.put(Button.DPAD_LEFT, gp.dpad_left);
        current.put(Button.DPAD_RIGHT, gp.dpad_right);
    }

    private static boolean getBoolFieldSafe(Object obj, String fieldName) {
        try {
            Field f = obj.getClass().getField(fieldName);
            return f.getBoolean(obj);
        } catch (Throwable ignored) {
            return false;
        }
    }

    private Object getGamepadField(String name) {
        try {
            Field f = gp.getClass().getField(name);
            return f.get(gp);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static void fillFinger(Finger dst, Object fingerObj) {
        dst.present = true;
        dst.isTouching = getBoolOnObj(fingerObj, "isTouching", false);
        dst.x = getFloatOnObj(fingerObj, "x", 0f);
        dst.y = getFloatOnObj(fingerObj, "y", 0f);
    }

    private static boolean getBoolOnObj(Object obj, String name, boolean def) {
        try {
            Field f = obj.getClass().getField(name);
            return f.getBoolean(obj);
        } catch (Throwable ignored) {
            return def;
        }
    }

    private static float getFloatOnObj(Object obj, String name, float def) {
        try {
            Field f = obj.getClass().getField(name);
            return f.getFloat(obj);
        } catch (Throwable ignored) {
            return def;
        }
    }

    private static double clamp01(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }

    public Gamepad raw() { return gp; }

    public double leftStickX()  { return gp.left_stick_x; }
    public double leftStickY()  { return gp.left_stick_y; }
    public double rightStickX() { return gp.right_stick_x; }
    public double rightStickY() { return gp.right_stick_y; }
}
