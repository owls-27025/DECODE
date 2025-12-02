package org.firstinspires.ftc.teamcode.mechanisms.drivetrain;

import static org.firstinspires.ftc.teamcode.helpers.HelperFunctions.easeInOutSine;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.helpers.Configuration;
import org.firstinspires.ftc.teamcode.helpers.Controls;
import org.firstinspires.ftc.teamcode.helpers.BaseSubsystem;
import org.firstinspires.ftc.teamcode.Robot;

public class Drivetrain extends BaseSubsystem {

    public DcMotor FL;
    public DcMotor FR;
    public DcMotor BL;
    public DcMotor BR;
    public IMU imu;

    private final Robot robot;

    private double currentSpeed = 1.0;

    public Drivetrain(HardwareMap hardwareMap,
                      Telemetry telemetry,
                      Configuration config,
                      Robot robot) {
        super(
                hardwareMap,
                telemetry,
                config.FR.itemActive &&
                        config.FL.itemActive &&
                        config.BR.itemActive &&
                        config.BL.itemActive
        );

        this.robot = robot;

        ifActive(() -> {
            FR = hardwareMap.get(DcMotor.class, config.FR.itemName);
            FL = hardwareMap.get(DcMotor.class, config.FL.itemName);
            BR = hardwareMap.get(DcMotor.class, config.BR.itemName);
            BL = hardwareMap.get(DcMotor.class, config.BL.itemName);

            FR.setDirection(DcMotor.Direction.REVERSE);
            BL.setDirection(DcMotor.Direction.REVERSE);

            FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            imu = hardwareMap.get(IMU.class, "imu");
            RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP
            );
            imu.initialize(new IMU.Parameters(orientation));
        });
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public double[] fieldCentricDrive(double x, double y) {
        if (!active || imu == null) {
            return new double[]{y, x};
        }

        double theta = Math.atan2(y, x);
        double r = Math.hypot(x, y);

        theta = AngleUnit.normalizeRadians(
                theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)
        );

        double newY = r * Math.sin(theta);
        double newX = r * Math.cos(theta);

        return new double[]{newY, newX};
    }

    public void resetIMU() {
        if (!active || imu == null) return;
        imu.resetYaw();
    }

    public void setDrivePowers(double fl, double bl, double fr, double br) {
        if (!active) return;
        FL.setPower(fl);
        BL.setPower(bl);
        FR.setPower(fr);
        BR.setPower(br);
    }

    public void drive(Controls controls) {
        if (!active) return;

        // gamepad1 sticks
        Gamepad g1 = controls.driver1;

        // speed control
        if (controls.slowMode) {
            currentSpeed = robot.getSlowDriveSpeed();
        } else {
            currentSpeed = robot.getDriveSpeed();
        }

        // IMU reset
        if (controls.resetIMU) {
            resetIMU();
        }

        double y, x, rx;
        if (!robot.getRightStick()) {
            y  = easeInOutSine(-g1.left_stick_y);
            x  = easeInOutSine(g1.left_stick_x);
            rx = easeInOutSine(g1.right_stick_x);
        } else {
            y  = easeInOutSine(-g1.right_stick_y);
            x  = easeInOutSine(g1.right_stick_x);
            rx = easeInOutSine(g1.left_stick_x);
        }

        if (robot.getFieldCentric()) {
            double[] fc = fieldCentricDrive(x, y);
            y = fc[0];
            x = fc[1];
        }

        double fl = (y + x + rx) * currentSpeed;
        double bl = (y - x + rx) * currentSpeed;
        double fr = (y - x - rx) * currentSpeed;
        double br = (y + x - rx) * currentSpeed;

        setDrivePowers(fl, bl, fr, br);
    }
}
