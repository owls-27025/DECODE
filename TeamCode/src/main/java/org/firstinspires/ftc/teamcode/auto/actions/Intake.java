package org.firstinspires.ftc.teamcode.auto.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;

public class Intake implements Action {
    public boolean initialized;

    public Intake() {
        initialized = false;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            Subsystems.currentState = Subsystems.IntakeState.MOVING_TO_POSITION;
            initialized = true;
        }
        return Subsystems.intakeAuto();
    }

}