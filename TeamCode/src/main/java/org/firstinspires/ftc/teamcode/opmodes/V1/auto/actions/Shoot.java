package org.firstinspires.ftc.teamcode.opmodes.V1.auto.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.Robot;

public class Shoot implements Action {
    public int artifacts;
    public boolean initialized;

    private Robot robot;

    public Shoot(Robot robot, int numArtifacts) {
        this.robot = robot;
        artifacts = numArtifacts;
        initialized = false;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            robot.mechanisms.currentShootState = org.firstinspires.ftc.teamcode.mechanisms.Mechanisms.ShootState.MOVING_TO_SHOOT_POSITION;
            initialized = true;
        }
        return robot.mechanisms.shootAuto(artifacts);
    }
}