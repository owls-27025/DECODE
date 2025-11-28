package org.firstinspires.ftc.teamcode.mechanisms.light;

import com.qualcomm.robotcore.hardware.Servo;

public class Light {
    public static Servo light;

    public static void red() {
        light.setPosition(0.277);
    }

    public static void yellow() {
        light.setPosition(0.388);
    }

    public static void green() {
        light.setPosition(0.5);
    }
}
