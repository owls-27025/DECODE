package org.firstinspires.ftc.teamcode.mechanisms.drivetrain;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Config;

public class Drivetrain {
    public static DcMotor FL;
    public static DcMotor FR;
    public static DcMotor BL;
    public static DcMotor BR;
    private static IMU imu;

    public static void init(HardwareMap hardwareMap) {
        FR = hardwareMap.get(DcMotor.class, Config.FR);
        FL = hardwareMap.get(DcMotor.class, Config.FL);
        BR = hardwareMap.get(DcMotor.class, Config.BR);
        BL = hardwareMap.get(DcMotor.class, Config.BL);

        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);

        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot Orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD); // placeholder

        imu.initialize(new IMU.Parameters(Orientation));
    }

    public static double[] fieldCentricDrive(double x, double y) {
        double[] newVariables = new double[2];

        double theta = Math.atan2(y, x);
        double r = Math.hypot(x, y);

        theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        newVariables[0] = r * Math.sin(theta); // y
        newVariables[1] = r * Math.cos(theta); // x
        // switch sin/cos if no work

        return newVariables;
    }

    public static void resetIMU() {
        imu.resetYaw();
    }
}
