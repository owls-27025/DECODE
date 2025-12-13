package org.firstinspires.ftc.teamcode.shared.mechanisms.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Robot;

public class Shooter {
    private final DcMotorEx shooter;
    private final Robot robot;

    public Shooter(Robot robot) {
        this.robot = robot;
        shooter = robot.registerItem(DcMotorEx.class, robot.config.shooter);
        if (shooter != null) {
            shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            shooter.setDirection(DcMotor.Direction.REVERSE);
        }
    }

    public void shoot(double velocity) {
        if (shooter != null) shooter.setVelocity(velocity);
    }

    /** +1 means rotate CCW one slot, -1 means rotate CW one slot, 0 means aligned. */
    public int calculateMotifOffset(Robot.Colors target) {
        int diff = robot.motif.index - target.index;
        if (diff == 2) diff = -1;
        if (diff == -2) diff = 1;
        return diff;
    }

    public double getVelocity() {
        return shooter == null ? 0.0 : shooter.getVelocity();
    }
}
