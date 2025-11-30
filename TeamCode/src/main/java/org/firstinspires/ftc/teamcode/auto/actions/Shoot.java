package org.firstinspires.ftc.teamcode.auto.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.auto.V1;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;

public class Shoot implements Action {
    public int artifacts;
    public boolean initialized;

    public Shoot(int numArtifacts) {
        artifacts = numArtifacts;
        initialized = false;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            Subsystems.currentShootState = Subsystems.ShootState.MOVING_TO_SHOOT_POSITION;
            initialized = true;
        }
        return Subsystems.shootAuto(artifacts);
    }

    public Action shoot(int artifacts) {
        return new Shoot(artifacts);
    }
}