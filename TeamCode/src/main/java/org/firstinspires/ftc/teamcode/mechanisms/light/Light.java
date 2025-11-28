package org.firstinspires.ftc.teamcode.mechanisms.light;

import com.qualcomm.robotcore.hardware.Servo;

public class Light {
    public Servo light;

    public void red() {
        light.setPosition(0.277);
    }

    public void yellow() {
        light.setPosition(0.388);
    }

    public void green() {
        light.setPosition(0.5);
    }
}
