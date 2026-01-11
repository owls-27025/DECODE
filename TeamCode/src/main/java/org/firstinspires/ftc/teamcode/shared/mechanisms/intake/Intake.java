package org.firstinspires.ftc.teamcode.shared.mechanisms.intake;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;

public class Intake {
    private final DcMotor intake;
    private final CRServo leftServo;
    private final CRServo rightServo;
    private final Servo intakeServo;

    public Intake(Robot.Configuration configuration) {
        intake = configuration.registerItem(DcMotor.class, Robot.Configuration.intake);
        if (intake != null) {
            intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        leftServo = configuration.registerItem(CRServo.class, Robot.Configuration.leftSweeper);
        rightServo = configuration.registerItem(CRServo.class, Robot.Configuration.rightSweeper);

        leftServo.setDirection(CRServo.Direction.REVERSE);

        intakeServo = configuration.registerItem(Servo.class, Robot.Configuration.intakeServo);
    }

    public void start() {
        setPower(Robot.Globals.intakeSpeed);
        leftServo.setPower(1);
        rightServo.setPower(1);
        intakeServo.setPosition(0.6);
    }

    public void stop() {
        setPower(0);
        leftServo.setPower(0);
        rightServo.setPower(0);
        intakeServo.setPosition(0);
    }

    public void reverse() {
        intakeServo.setPosition(0.6);
        setPower(-Robot.Globals.intakeSpeed);
    }

    public void setPower(double power) {
        if (intake != null) intake.setPower(power);
    }
}