package org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    public Drivetrain(Robot.Config config) {
        FR = config.registerItem(DcMotor.class, Robot.Config.FR);
        FL = config.registerItem(DcMotor.class, Robot.Config.FL);
        BR = config.registerItem(DcMotor.class, Robot.Config.BR);
        BL = config.registerItem(DcMotor.class, Robot.Config.BL);

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

        imu = config.registerItem(IMU.class, Robot.Config.imu);
        if (imu != null) {
            RevHubOrientationOnRobot Orientation = new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP);
            imu.initialize(new IMU.Parameters(Orientation));
        }

        odo = config.registerItem(GoBildaPinpointDriver.class, Robot.Config.odometry);
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

    public void update() {
        odo.update();
    }

    public double getIMUHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
}
