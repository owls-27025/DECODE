/*

package org.firstinspires.ftc.teamcode.opmodes.V1.auto.backups;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;

@Disabled
@Autonomous(name = "autotest")
public class AutoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Drivetrain.init(hardwareMap);
        Subsystems.init(hardwareMap, telemetry);
        waitForStart();

        forward(-1000);
        Subsystems.shootAuto(3);
        turn(-45);
        while(true){
            // do nothing
        }
//        strafe(400);
//        Subsystems.intakeAuto(200);
//        Subsystems.intakeAuto(200);
//        Subsystems.intakeAuto(200);
    }

    public void forward(int ticks) {
        boolean completed = false;

        while (!completed) {
            Drivetrain.BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Drivetrain.BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Drivetrain.FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Drivetrain.FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            Drivetrain.BL.setTargetPosition(ticks);
            Drivetrain.BR.setTargetPosition(ticks);
            Drivetrain.FL.setTargetPosition(ticks);
            Drivetrain.FR.setTargetPosition(ticks);

            Drivetrain.BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Drivetrain.BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Drivetrain.FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Drivetrain.FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            Drivetrain.BL.setPower(0.35);
            Drivetrain.BR.setPower(0.35);
            Drivetrain.FL.setPower(0.35);
            Drivetrain.FR.setPower(0.35);

            while (Drivetrain.BL.isBusy() && Drivetrain.BR.isBusy() && Drivetrain.FL.isBusy() && Drivetrain.FR.isBusy()) {
                // wait
            }

            Drivetrain.BL.setPower(0);
            Drivetrain.BR.setPower(0);
            Drivetrain.FL.setPower(0);
            Drivetrain.FR.setPower(0);

            completed = true;
        }


    }

    public void strafe(int ticks) {
        boolean completed = false;

        Drivetrain.BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Drivetrain.BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Drivetrain.FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Drivetrain.FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Drivetrain.BL.setTargetPosition(-ticks);
        Drivetrain.BR.setTargetPosition(ticks);
        Drivetrain.FL.setTargetPosition(ticks);
        Drivetrain.FR.setTargetPosition(-ticks);

        Drivetrain.BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Drivetrain.BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Drivetrain.FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Drivetrain.FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Drivetrain.BL.setPower(0.35);
        Drivetrain.BR.setPower(0.35);
        Drivetrain.FL.setPower(0.35);
        Drivetrain.FR.setPower(0.35);

        while (Drivetrain.BL.isBusy() && Drivetrain.BR.isBusy() && Drivetrain.FL.isBusy() && Drivetrain.FR.isBusy()) {
            // wait
        }

        Drivetrain.BL.setPower(0);
        Drivetrain.BR.setPower(0);
        Drivetrain.FL.setPower(0);
        Drivetrain.FR.setPower(0);

    }

    public void turn(int degrees) {
//        boolean completed = false;

        //while (!completed) {
        Drivetrain.BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Drivetrain.BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Drivetrain.FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Drivetrain.FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Drivetrain.BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Drivetrain.BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Drivetrain.FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Drivetrain.FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Drivetrain.imu.resetYaw();

        if (degrees > 0) {
            // clockwise
            if (Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) < 0) {
                while (Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - degrees < 2) {
                    telemetry.addData("yaw", Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
                    Drivetrain.BL.setPower(0.35);
                    Drivetrain.BR.setPower(-0.35);
                    Drivetrain.FL.setPower(0.35);
                    Drivetrain.FR.setPower(-0.35);
                }
            } else if (Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) > 0) {
                while (Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - degrees > 2) {
                    telemetry.addData("yaw", Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));

                    Drivetrain.BL.setPower(0.35);
                    Drivetrain.BR.setPower(-0.35);
                    Drivetrain.FL.setPower(0.35);
                    Drivetrain.FR.setPower(-0.35);
                }
            }


        } else if (degrees < 0) {
            // counterclockwise
            if (Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) < 0) {
                while (Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - degrees < 2) {
                    telemetry.addData("yaw", Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
                    Drivetrain.BL.setPower(-0.35);
                    Drivetrain.BR.setPower(0.35);
                    Drivetrain.FL.setPower(-0.35);
                    Drivetrain.FR.setPower(0.35);
                }
            } else if (Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) > 0) {
                while (Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - degrees > 2) {
                    telemetry.addData("yaw", Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));

                    Drivetrain.BL.setPower(-0.35);
                    Drivetrain.BR.setPower(0.35);
                    Drivetrain.FL.setPower(-0.35);
                    Drivetrain.FR.setPower(0.35);
                }
            }
        }

        telemetry.addData(" initial yaw", Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        telemetry.addLine("exiting turn method");
        telemetry.addData(" final yaw", Drivetrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        telemetry.update();

        Drivetrain.BL.setPower(0);
        Drivetrain.BR.setPower(0);
        Drivetrain.FL.setPower(0);
        Drivetrain.FR.setPower(0);

        //completed = true;
        //}
    }
}

*/