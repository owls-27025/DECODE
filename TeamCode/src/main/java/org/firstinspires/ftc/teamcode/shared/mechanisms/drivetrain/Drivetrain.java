package org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Robot;


public class Drivetrain {
    private final DcMotor FL;
    private final DcMotor FR;
    private final DcMotor BL;
    private final DcMotor BR;
    private final IMU imu;
    private final GoBildaPinpointDriver odo;

    public Drivetrain(Robot.Configuration configuration) {
        FR = configuration.registerItem(DcMotor.class, Robot.Configuration.FR);
        FL = configuration.registerItem(DcMotor.class, Robot.Configuration.FL);
        BR = configuration.registerItem(DcMotor.class, Robot.Configuration.BR);
        BL = configuration.registerItem(DcMotor.class, Robot.Configuration.BL);

        if (FL != null) FL.setDirection(DcMotor.Direction.REVERSE);
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

        imu = Robot.Configuration.registerItem(IMU.class, Robot.Configuration.imu);
        if (imu != null) {
            RevHubOrientationOnRobot Orientation = new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP);
            imu.initialize(new IMU.Parameters(Orientation));
        }

        odo = Robot.Configuration.registerItem(GoBildaPinpointDriver.class, Robot.Configuration.odometry);
    }

    public double[] fieldCentricDrive(double x, double y) {
        double[] out = new double[2];

        if (imu == null) {
            out[0] = y;
            out[1] = x;
            return out;
        }

        double heading = imu.getRobotYawPitchRollAngles()
                .getYaw(AngleUnit.RADIANS);

        double cos = Math.cos(heading);
        double sin = Math.sin(heading);


        double fieldX = x * cos - y * sin;
        double fieldY = x * sin + y * cos;

        out[0] = fieldY;
        out[1] = fieldX;
        return out;
    }


    public void resetIMU() {
        if (imu != null) imu.resetYaw();
    }

    public double getOdometryHeading() {
        return odo.getHeading(AngleUnit.DEGREES);
    }

    public int getOdometryX() {
        return odo.getEncoderX();
    }

    public int getOdometryY() {
        return odo.getEncoderY();
    }

    public void drive(OwlsGamepad gp) {
        // speed control
        Robot.Globals.currentSpeed = gp.held(OwlsGamepad.Button.LB) ? Robot.Globals.slowDriveSpeed : Robot.Globals.driveSpeed;

        // reset imu
        if (gp.pressed(OwlsGamepad.Button.START)) {
            resetIMU();
        }

        double y, x, rx;
        if (!Robot.Globals.isRightStick) {
            y = Robot.Globals.easeInOutSine(-gp.leftStickY());
            x = Robot.Globals.easeInOutSine(gp.leftStickX());
            rx = Robot.Globals.easeInOutSine(gp.rightStickX());
        } else {
            y = Robot.Globals.easeInOutSine(-gp.rightStickY());
            x = Robot.Globals.easeInOutSine(gp.rightStickX());
            rx = Robot.Globals.easeInOutSine(gp.leftStickX());
        }

        if (Robot.Globals.isFieldCentric) {
            double[] fc = fieldCentricDrive(x, y);
            y = fc[0];
            x = fc[1];
        }

        setPower(FL, (y + x + rx) * Robot.Globals.currentSpeed);
        setPower(BL, (y - x + rx) * Robot.Globals.currentSpeed);
        setPower(FR, (y - x - rx) * Robot.Globals.currentSpeed);
        setPower(BR, (y + x - rx) * Robot.Globals.currentSpeed);
    }

    private static void setPower(DcMotor m, double p) {
        if (m != null) m.setPower(p);
    }

    public int getFLPos() { return FL == null ? 0 : FL.getCurrentPosition(); }
    public int getFRPos() { return FR == null ? 0 : FR.getCurrentPosition(); }
    public int getBLPos() { return BL == null ? 0 : BL.getCurrentPosition(); }
    public int getBRPos() { return BR == null ? 0 : BR.getCurrentPosition(); }

    public Pose2D getPose() {
        return odo.getPosition();
    }

    public void update() {
        odo.update();
    }

    public double getIMUHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
}
