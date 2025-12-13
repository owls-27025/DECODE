package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;

/**
 * Controls: controller-normalization + edge detection + inhibit logic + safe PS extras.
 *
 * - Canonical buttons: works with Xbox + PlayStation (Cross/Circle/Square/Triangle)
 * - pressed/held/released (digital)
 * - analog triggers (0.0 -> 1.0) + trigger edge detection
 * - automatic inhibit when Start + (A or B)
 * - safe rumble / LED color / touchpad (never crashes on Xbox or older SDKs)
 *
 * Call update() ONCE per loop before reading inputs.
 */
public class Controls {

    // ----------------------------
    // Canonical buttons (logical)
    // ----------------------------
    public enum Button {
        A, B, X, Y,
        LB, RB,
        LT, RT,           // digital (thresholded)
        START, BACK,
        LS, RS,
        DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT
    }

    // Touchpad data (safe; may be unavailable)
    public static final class TouchpadState {
        public boolean available = false;
        public final Finger f1 = new Finger();
        public final Finger f2 = new Finger();
    }

    public static final class Finger {
        public boolean present = false;
        public boolean isTouching = false;
        public float x = 0f; // usually 0..1 if available
        public float y = 0f; // usually 0..1 if available
    }

    private final Gamepad gp;

    private final Map<Button, Boolean> current = new EnumMap<Button, Boolean>(Button.class);
    private final Map<Button, Boolean> previous = new EnumMap<Button, Boolean>(Button.class);

    // analog triggers for edge detection
    private double prevLT = 0.0;
    private double prevRT = 0.0;

    // inhibit: start + (a|b)
    private boolean inhibitButtons = false;

    // Trigger thresholds for digital LT/RT mapping
    private double triggerDigitalThreshold = 0.5;

    public Controls(Gamepad gamepad) {
        this.gp = gamepad;

        for (Button b : Button.values()) {
            current.put(b, false);
            previous.put(b, false);
        }
    }

    /** Optionally adjust the digital LT/RT threshold (default 0.5). */
    public void setTriggerDigitalThreshold(double threshold) {
        triggerDigitalThreshold = clamp01(threshold);
    }

    /** Call ONCE per loop, before reading pressed/held/released. */
    public void update() {
        // shift digital button states
        for (Button b : Button.values()) {
            previous.put(b, current.get(b));
        }

        // store previous analog trigger values (for edge detection)
        prevLT = gp.left_trigger;
        prevRT = gp.right_trigger;

        // read physical -> canonical
        mapButtons();

        // inhibit: start + (a|b)
        if (heldRaw(Button.START) && (heldRaw(Button.A) || heldRaw(Button.B))) {
            inhibitButtons = true;
        }

        // release inhibit once A and B are lifted (matches your old behavior)
        if (!heldRaw(Button.A) && !heldRaw(Button.B)) {
            inhibitButtons = false;
        }
    }

    // ----------------------------
    // Digital button API
    // ----------------------------
    public boolean pressed(Button b) {
        return current.get(b) && !previous.get(b) && !inhibitButtons;
    }

    public boolean held(Button b) {
        return current.get(b) && !inhibitButtons;
    }

    public boolean released(Button b) {
        return !current.get(b) && previous.get(b);
    }

    /** Raw (ignores inhibit). Useful for combos that control inhibit itself. */
    public boolean heldRaw(Button b) {
        Boolean v = current.get(b);
        return v != null && v;
    }

    public boolean inhibited() {
        return inhibitButtons;
    }

    // ----------------------------
    // Analog triggers (0.0 -> 1.0)
    // ----------------------------
    public double leftTrigger() {
        return clamp01(gp.left_trigger);
    }

    public double rightTrigger() {
        return clamp01(gp.right_trigger);
    }

    /** Edge: crosses up through threshold this frame. */
    public boolean leftTriggerPressed(double threshold) {
        threshold = clamp01(threshold);
        double now = leftTrigger();
        return (now >= threshold && prevLT < threshold) && !inhibitButtons;
    }

    /** Edge: crosses down through threshold this frame. */
    public boolean leftTriggerReleased(double threshold) {
        threshold = clamp01(threshold);
        double now = leftTrigger();
        return (now < threshold && prevLT >= threshold);
    }

    /** Edge: crosses up through threshold this frame. */
    public boolean rightTriggerPressed(double threshold) {
        threshold = clamp01(threshold);
        double now = rightTrigger();
        return (now >= threshold && prevRT < threshold) && !inhibitButtons;
    }

    /** Edge: crosses down through threshold this frame. */
    public boolean rightTriggerReleased(double threshold) {
        threshold = clamp01(threshold);
        double now = rightTrigger();
        return (now < threshold && prevRT >= threshold);
    }

    // ----------------------------
    // PS/Xbox extras (safe)
    // ----------------------------

    /** Safe rumble on any controller. Returns true if the SDK supported the call. */
    public boolean rumble(int milliseconds) {
        try {
            Method m = gp.getClass().getMethod("rumble", int.class);
            m.invoke(gp, milliseconds);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /** Safe blips. Falls back to short rumbles if rumbleBlips isn't available. */
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

    /**
     * Safe lightbar/LED color. Works on PS controllers if SDK exposes setLedColor,
     * safely no-ops on Xbox.
     *
     * r,g,b are 0..1. durationMs may be ignored on some SDKs.
     */
    public boolean setLedColor(double r, double g, double b, int durationMs) {
        r = clamp01(r); g = clamp01(g); b = clamp01(b);

        // Signature: setLedColor(double,double,double,int)
        try {
            Method m = gp.getClass().getMethod("setLedColor",
                    double.class, double.class, double.class, int.class);
            m.invoke(gp, r, g, b, durationMs);
            return true;
        } catch (Throwable ignored) { }

        // Signature: setLedColor(double,double,double)
        try {
            Method m = gp.getClass().getMethod("setLedColor",
                    double.class, double.class, double.class);
            m.invoke(gp, r, g, b);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /** Safe touchpad read (PS). On Xbox/older SDK, returns available=false. */
    public TouchpadState readTouchpad() {
        TouchpadState out = new TouchpadState();

        Object f1 = getGamepadField("touchpad_finger_1");
        Object f2 = getGamepadField("touchpad_finger_2");

        if (f1 != null) fillFinger(out.f1, f1);
        if (f2 != null) fillFinger(out.f2, f2);

        out.available = (f1 != null || f2 != null);
        return out;
    }

    // ----------------------------
    // Internal mapping
    // ----------------------------
    private void mapButtons() {
        // Face buttons: unify Xbox + PlayStation names if present.
        // Some SDK versions expose cross/circle/square/triangle; others may not.
        current.put(Button.A, gp.a || getBoolFieldSafe(gp, "cross"));
        current.put(Button.B, gp.b || getBoolFieldSafe(gp, "circle"));
        current.put(Button.X, gp.x || getBoolFieldSafe(gp, "square"));
        current.put(Button.Y, gp.y || getBoolFieldSafe(gp, "triangle"));

        current.put(Button.LB, gp.left_bumper);
        current.put(Button.RB, gp.right_bumper);

        // Digital trigger buttons based on threshold
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

    // ----------------------------
    // Reflection helpers (safe)
    // ----------------------------
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
}
