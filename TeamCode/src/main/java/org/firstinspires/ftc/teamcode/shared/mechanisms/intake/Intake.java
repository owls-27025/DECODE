package org.firstinspires.ftc.teamcode.shared.mechanisms.intake;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robot;

public class Intake {
    private final DcMotor intake;

    public Intake(Robot.Config config) {
        intake = config.registerItem(DcMotor.class, Robot.Config.intake);
        if (intake != null) {
            intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void start() {
        setPower(Robot.Globals.intakeSpeed);
    }

    public void stop() {
        setPower(0);
    }

    public void reverse() {
        setPower(-Robot.Globals.intakeSpeed);
    }

    public void setPower(double power) {
        if (intake != null) intake.setPower(power);
    }
}
