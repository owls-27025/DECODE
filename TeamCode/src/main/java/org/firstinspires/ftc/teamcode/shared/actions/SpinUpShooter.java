package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import org.firstinspires.ftc.teamcode.Robot;

public class SpinUpShooter extends BaseAction {
    public SpinUpShooter(Robot robot) {
        super(robot);
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        shooter.shoot(robot.shooterVelocity);
        return false;
    }
}
