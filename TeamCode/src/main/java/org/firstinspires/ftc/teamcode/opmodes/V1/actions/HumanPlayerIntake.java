package org.firstinspires.ftc.teamcode.opmodes.V1.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanisms.shooter.Shooter;

public class HumanPlayerIntake implements Action {

    private final Robot robot;
    private final Shooter shooter;
    private boolean initialized = false;

    public HumanPlayerIntake(Robot robot) {
        this.robot = robot;
        this.shooter = robot.getShooter();
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            if (shooter.getVelocity() > 0) {
                shooter.shoot(0);
                shooter.shooterMotor.setPower(-0.5);
                robot.setHumanIntaking(true);
            } else {
                shooter.shoot(robot.getShooterVelocity());
                robot.setHumanIntaking(false);
            }
            initialized = true;
        }
        return false;
    }
}
