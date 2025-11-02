package org.firstinspires.ftc.teamcode.mechanisms.spindexer.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Config;

public class ShooterHelper {
    public static DcMotorEx shooterMotor;

    public static void init(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, Config.shooter);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    public static void shoot() {
        shooterMotor.setVelocity(1000); // placeholder, once limelight implemented calculate velocity
    }

    public static void shoot(double velocity) {
        shooterMotor.setVelocity(velocity);
    }
}
