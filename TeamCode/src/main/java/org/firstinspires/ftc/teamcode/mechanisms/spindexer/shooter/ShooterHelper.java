package org.firstinspires.ftc.teamcode.mechanisms.spindexer.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ShooterHelper {
    public static DcMotorEx shooterMotor;
    public static Servo shooterAngle;

    public static void init(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, "Shooter");
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterAngle = hardwareMap.get(Servo.class, "Shooter Angle");
    }

    public static void shoot() {
        shooterMotor.setVelocity(1000); // placeholder, once limelight implemented calculate velocity
        shooterAngle.setPosition(0); // placeholder, once limelight implemented calculate angle
    }

    public static void shoot(double velocity, double angle) {
        shooterMotor.setVelocity(velocity);
        shooterAngle.setPosition(angle); // placeholder
    }

    public static void shoot(double velocity) {
        shooterMotor.setVelocity(velocity);
    }
}
