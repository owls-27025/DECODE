package org.firstinspires.ftc.teamcode.mechanisms.light;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configuration;

import java.util.Objects;

public class Light {
    public static Servo light;
    public static String color;

    public static void init(HardwareMap hardwareMap) {
        if (Configuration.light.itemActive) {
            light = hardwareMap.get(Servo.class, Configuration.light.itemName);
        }
    }

    // red
    public static void red() {
        light.setPosition(0.3);
        color = "Red";
    }
    // yellow
    public static void yellow() {
        light.setPosition(0.388);
        color = "Yellow";
    }
    // green
    public static void green() {
        light.setPosition(0.5);
        color = "Green";
    }

    public static void cycle() {
        if (Objects.equals(color, "Red")) {
            yellow();
        } else if (Objects.equals(color, "Yellow")) {
            green();
        } else if (Objects.equals(color, "Green")) {
            red();
        } else {
            red();
        }
    }
}
