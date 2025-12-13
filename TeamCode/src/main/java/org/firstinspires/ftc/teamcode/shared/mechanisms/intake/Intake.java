package org.firstinspires.ftc.teamcode.shared.mechanisms.intake;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robot;

public class Intake {
    private final DcMotor intake;
    private final Robot robot;

    public Intake(Robot robot) {
        this.robot = robot;
        intake = robot.registerItem(DcMotor.class, robot.config.intake);
        if (intake != null) {
            intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void start() {
        setPower(robot.intakeSpeed);
    }

    public void stop() {
        setPower(0);
    }

    public void reverse() {
        setPower(-robot.intakeSpeed);
    }

    public void setPower(double power) {
        if (intake != null) intake.setPower(power);
    }
}
