package org.firstinspires.ftc.teamcode.auto.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;

public class Shoot implements Action {
    public int artifacts;
    public int velocity;
    public boolean initialized;

    public Shoot(int numArtifacts, int velocity) {
        artifacts = numArtifacts;
        initialized = false;
        this.velocity = velocity;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            Subsystems.currentAutoShootState = Subsystems.AutoShootState.INIT;
            initialized = true;
        }
        packet.put("Shooter Velocity", ShooterHelper.shooterMotor.getVelocity());
        return Subsystems.shootAuto(artifacts, velocity);
    }
}