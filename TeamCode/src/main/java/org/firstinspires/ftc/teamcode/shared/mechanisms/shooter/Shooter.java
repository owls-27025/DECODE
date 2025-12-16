package org.firstinspires.ftc.teamcode.shared.mechanisms.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Robot;

public class Shooter {
    private final DcMotorEx shooter;

    public Shooter(Robot.Config config) {
        shooter = config.registerItem(DcMotorEx.class, Robot.Config.shooter);
        if (shooter != null) {
            shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            shooter.setDirection(DcMotor.Direction.REVERSE);
        }
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
}
