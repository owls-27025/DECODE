package org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter;

import static org.firstinspires.ftc.teamcode.helpers.Globals.motif;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.helpers.Globals;

import java.util.Arrays;

public class ShooterHelper {
    public static DcMotorEx shooterMotor;

    public static void init(HardwareMap hardwareMap) {
        if (Configuration.shooter.itemActive) {
            shooterMotor = hardwareMap.get(DcMotorEx.class, Configuration.shooter.itemName);
            shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            shooterMotor.setDirection(DcMotor.Direction.REVERSE);
        }
    }

    public static void shoot() {
        shooterMotor.setVelocity(1000); // placeholder, once limelight implemented calculate velocity
    }

    public static void shoot(double velocity) {
        shooterMotor.setVelocity(velocity);
    }

    public static int calculateMotifOffset(Globals.Colors colors) {
        int diff = motif.index - colors.index;

        if (diff == 2) diff = -1;
        if (diff == -2) diff = 1;

        return diff;
    }
}
