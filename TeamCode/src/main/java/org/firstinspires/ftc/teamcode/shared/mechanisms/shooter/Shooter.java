package org.firstinspires.ftc.teamcode.shared.mechanisms.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.Robot;

public class Shooter {
    private final DcMotorEx shooter;
    private final Servo hood;

    public Shooter(Robot.Configuration configuration) {
        shooter = configuration.registerItem(DcMotorEx.class, Robot.Configuration.shooter);
        if (shooter != null) {
            shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        hood = configuration.registerItem(Servo.class, Robot.Configuration.hood);
    }

    public void shoot(double velocity) {
        if (shooter != null) shooter.setVelocity(velocity);
    }

    public int calculateMotifOffset(Robot.Globals.Colors target) {
        int diff = Robot.Globals.motif.index - target.index;
        if (diff == 2) diff = -1;
        if (diff == -2) diff = 1;
        return diff;
    }

    public double getVelocity() {
        return shooter == null ? 0.0 : shooter.getVelocity();
    }

    public void setHood(double pos) { hood.setPosition(pos); }

    public double getHood() { return hood.getPosition(); }

    public PIDFCoefficients getPIDFCoefficients() {
        return shooter.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setPIDFCoefficients(PIDFCoefficients pidf) {
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
    }
}
