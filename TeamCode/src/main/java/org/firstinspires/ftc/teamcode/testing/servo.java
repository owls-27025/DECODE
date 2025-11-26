package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

@TeleOp
public class servo extends OpMode {
    Servo servo;

    @Override
    public void init() {
        if (Configuration.spindexerServo.itemActive) {
            servo = hardwareMap.get(Servo.class, Configuration.spindexerServo.itemName);
        }
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            servo.setPosition(1);
        } else if (gamepad1.b) {
            servo.setPosition(0.5);
        } else if (gamepad1.x) {
            servo.setPosition(0);
        }
    }
}