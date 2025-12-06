package org.firstinspires.ftc.teamcode.auto.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.auto.V1;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;

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
            Subsystems.currentAutoShootState = Subsystems.AutoShootState.INIT;
            initialized = true;
        }
        packet.put("Shooter Velocity", ShooterHelper.shooterMotor.getVelocity());
        return Subsystems.shootAuto(artifacts);
    }
}