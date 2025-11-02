package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexertestest;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.deprecated.SpindexerOld;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.shooter.ShooterHelper;

import java.util.Arrays;

@TeleOp(name = "V1Test", group = "OpModes")
public class V1test extends OpMode {
    public enum State { AUTO, MANUAL}
    private static State state = V1test.State.AUTO;
    int currentBall = 0;
    String[] colors;
    boolean hasColors = false;
    String currentColor;
    double currentSpeed = 1;
    boolean isFieldCentric = false; 
    int currentIntakeBall = 0;

    @Override
    public void init() {
//        Drivetrain.init(hardwareMap);
        Spindexer.init(hardwareMap, telemetry);
    }
    public void loop() {
//        drivetrain();
        try {
            shooter();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        try {
//            intake();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        if(gamepad2.right_bumper) {
            state = State.MANUAL;
        } else if(gamepad2.left_bumper) {
            state = State.AUTO;
        }

//        telemetry.addData("Spindexer Position", SpindexerHelper.findPosition());
//        telemetry.addData("Current Position", SpindexerHelper.SpindexerMotor.getCurrentPosition());
//        telemetry.addData("Current Offset", SpindexerHelper.getStateOffset());
//        telemetry.addData("Current State", SpindexerOld.state);
//        telemetry.addData("current state again", SpindexerOld.state);
//        telemetry.addData("Target Position", SpindexerHelper.SpindexerMotor.getTargetPosition());
//        telemetry.addData("Power", SpindexerHelper.SpindexerMotor.getPower());

    }

    public void drivetrain() {
        if(gamepad1.left_bumper) {
            currentSpeed = 0.35;
        } else {
            currentSpeed = 1;
        }

        if(gamepad1.guide) {
            if(isFieldCentric) {
                isFieldCentric = true;
                return;
            } else {
                isFieldCentric = false;
                return;
            }
        }

        if(gamepad1.start) {
            Drivetrain.resetIMU();
        }

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        if(isFieldCentric) {
            y = Drivetrain.fieldCentricDrive(x, y)[0];
            x = Drivetrain.fieldCentricDrive(x, y)[1];
        }

        Drivetrain.FL.setPower((y + x + rx) * currentSpeed);
        Drivetrain.BL.setPower((y - x + rx) * currentSpeed);
        Drivetrain.FR.setPower((y - x - rx) * currentSpeed);
        Drivetrain.BR.setPower((y + x - rx) * currentSpeed);
    }

    public void shooter() throws InterruptedException {
        if (gamepad1.bWasPressed()) {
            Spindexer.shoot();
        }
        if (gamepad1.xWasPressed()) {

            ShooterHelper.shoot(Spindexertestest.SHOOTER_VELOCITY);
            while(Math.abs(ShooterHelper.shooterMotor.getVelocity() - Spindexertestest.SHOOTER_VELOCITY) > 5) {
                telemetry.addData("shooter velocity", ShooterHelper.shooterMotor.getVelocity());
                telemetry.update();
            }

            Thread.sleep(750);
            SpindexerHelper.moveServo(1);
            Thread.sleep(750);
            SpindexerHelper.moveServo(0.5);
            Thread.sleep(750);
            SpindexerHelper.moveToNextPosition();

            ShooterHelper.shoot(1400);
            while(Math.abs(ShooterHelper.shooterMotor.getVelocity() - 1400) > 5) {
                telemetry.addData("shooter velocity", ShooterHelper.shooterMotor.getVelocity());
                telemetry.update();
            }

            Thread.sleep(750);
            SpindexerHelper.moveServo(1);
            Thread.sleep(750);
            SpindexerHelper.moveServo(0.5);
            Thread.sleep(750);
            SpindexerHelper.moveToNextPosition();

            ShooterHelper.shoot(1400);
            while(Math.abs(ShooterHelper.shooterMotor.getVelocity() - 1400) > 5) {
                telemetry.addData("shooter velocity", ShooterHelper.shooterMotor.getVelocity());
                telemetry.update();
            }

            Thread.sleep(750);
            SpindexerHelper.moveServo(1);
            Thread.sleep(750);
            SpindexerHelper.moveServo(0.5);
            Thread.sleep(750);
            SpindexerHelper.moveToNextPosition();

        }
    }

    private void intake() throws InterruptedException {
        if(gamepad1.aWasPressed()) {
            Spindexer.intake();
        }
    }
}