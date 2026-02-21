package org.firstinspires.ftc.teamcode.shared.mechanisms.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Robot;

public class Shooter {
    private final DcMotorEx shooter;
    private final DcMotorEx shooter1;
    private final Servo hood;

    public Shooter(Robot.Configuration configuration) {
        shooter = Robot.Configuration.registerItem(DcMotorEx.class, Robot.Configuration.shooter);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setDirection(DcMotor.Direction.REVERSE);

        shooter1 = Robot.Configuration.registerItem(DcMotorEx.class, Robot.Configuration.shooter1);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setDirection(DcMotor.Direction.FORWARD);

        hood = Robot.Configuration.registerItem(Servo.class, Robot.Configuration.hood);
    }

    public void shoot(double power) {
        shooter.setPower(power);
        shooter1.setPower(power);
    }

    public void shoot(double power, int motor) {
        if (motor == 1) shooter.setPower(power);
        else if (motor == 2) shooter1.setPower(power);
    }

    public int calculateMotifOffset(Robot.Globals.Colors target) {
        int diff = Robot.Globals.motif.index - target.index;
        if (diff == 2) diff = -1;
        if (diff == -2) diff = 1;
        return diff;
    }

    public double getVelocity() {
        return shooter.getVelocity();
    }

    public double getVelocity(int motor) {
        return motor == 1 ? shooter.getVelocity() : shooter1.getVelocity();
    }

    public void setHood(double pos) { hood.setPosition(pos); }

    public double getHood() { return hood.getPosition(); }

    public double getPower() {
        return shooter.getPower() + shooter1.getPower() / 2;
    }
    public double getPower(int motor) { return motor == 1 ? shooter.getPower() : shooter1.getPower(); }
}