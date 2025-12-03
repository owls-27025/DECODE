package org.firstinspires.ftc.teamcode.auto.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;

public class SpinUpShooter implements Action {
    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        ShooterHelper.shoot(Globals.ShooterVelocity);
        return false;
    }
}
