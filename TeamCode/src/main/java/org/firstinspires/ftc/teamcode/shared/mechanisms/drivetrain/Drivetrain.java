package org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Robot;

public class Drivetrain {
    private final DcMotor FL;
    private final DcMotor FR;
    private final DcMotor BL;
    private final DcMotor BR;
    private final IMU imu;
    private final Robot robot;

    public Drivetrain(Robot robot) {
        this.robot = robot;

        FR = robot.registerItem(DcMotor.class, robot.config.FR);
        FL = robot.registerItem(DcMotor.class, robot.config.FL);
        BR = robot.registerItem(DcMotor.class, robot.config.BR);
        BL = robot.registerItem(DcMotor.class, robot.config.BL);

        if (FR != null) FR.setDirection(DcMotor.Direction.REVERSE);
        if (BL != null) BL.setDirection(DcMotor.Direction.REVERSE);

        if (FR != null) FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (FL != null) FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (BR != null) BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (BL != null) BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if (FR != null) FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (BR != null) BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (FL != null) FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (BL != null) BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if (FR != null) FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (BR != null) BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (FL != null) FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (BL != null) BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu = robot.registerItem(IMU.class, robot.config.imu);
        if (imu != null) {
            RevHubOrientationOnRobot Orientation = new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP);
            imu.initialize(new IMU.Parameters(Orientation));
        }
    }

    public double[] fieldCentricDrive(double x, double y) {
        double[] out = new double[2];

        double theta = Math.atan2(y, x);
        double r = Math.hypot(x, y);

        if (imu != null) {
            theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        }

        out[0] = r * Math.sin(theta); // y
        out[1] = r * Math.cos(theta); // x
        return out;
    }

    public void resetIMU() {
        if (imu != null) imu.resetYaw();
    }

    public void drive(Gamepad gp) {
        // speed control
        robot.currentSpeed = gp.left_bumper ? robot.slowDriveSpeed : robot.driveSpeed;

        // reset imu
        if (gp.startWasPressed()) {
            resetIMU();
        }

        double y, x, rx;
        if (!robot.isRightStick) {
            y = Robot.easeInOutSine(-gp.left_stick_y);
            x = Robot.easeInOutSine(gp.left_stick_x);
            rx = Robot.easeInOutSine(gp.right_stick_x);
        } else {
            y = Robot.easeInOutSine(-gp.right_stick_y);
            x = Robot.easeInOutSine(gp.right_stick_x);
            rx = Robot.easeInOutSine(gp.left_stick_x);
        }

        if (robot.isFieldCentric) {
            double[] fc = fieldCentricDrive(x, y);
            y = fc[0];
            x = fc[1];
        }

        setPower(FL, (y + x + rx) * robot.currentSpeed);
        setPower(BL, (y - x + rx) * robot.currentSpeed);
        setPower(FR, (y - x - rx) * robot.currentSpeed);
        setPower(BR, (y + x - rx) * robot.currentSpeed);
    }

    private static void setPower(DcMotor m, double p) {
        if (m != null) m.setPower(p);
    }

    // --- Testing helpers ---
    public int getFLPos() { return FL == null ? 0 : FL.getCurrentPosition(); }
    public int getFRPos() { return FR == null ? 0 : FR.getCurrentPosition(); }
    public int getBLPos() { return BL == null ? 0 : BL.getCurrentPosition(); }
    public int getBRPos() { return BR == null ? 0 : BR.getCurrentPosition(); }
}
