package org.firstinspires.ftc.teamcode.shared.mechanisms.light;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;

public class Light {
    public enum LightColor {
        RED(0.300),
        YELLOW(0.388),
        GREEN(0.500),
        BLUE(0.611);

        public final double servoPos;

        LightColor(double pos) {
            this.servoPos = pos;
        }

        public LightColor next() {
            LightColor[] vals = values();
            return vals[(ordinal() + 1) % vals.length];
        }
    }

    private final Servo light;
    private LightColor current = LightColor.RED;

    public Light(Robot robot) {
        light = robot.registerItem(Servo.class, robot.config.light);
        apply();
    }

    public void set(LightColor color) {
        if (light == null) return;
        current = color;
        apply();
    }

    public void red()    { set(LightColor.RED); }
    public void yellow() { set(LightColor.YELLOW); }
    public void green()  { set(LightColor.GREEN); }
    public void blue()   { set(LightColor.BLUE); }

    public void cycle() {
        set(current.next());
    }

    public LightColor get() {
        return current;
    }

    private void apply() {
        if (light != null) {
            light.setPosition(current.servoPos);
        }
    }
}
