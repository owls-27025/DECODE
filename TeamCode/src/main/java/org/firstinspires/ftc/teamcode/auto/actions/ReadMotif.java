package org.firstinspires.ftc.teamcode.auto.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.mechanisms.limelight.Limelight;

public class ReadMotif implements Action {
    int motif;

    public ReadMotif() { motif = 0; }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (Limelight.getMotif() != 0) {
            motif = Limelight.getMotif();
            return true;
        }
        return false;
    }

    public Action readMotif() { return new ReadMotif(); }
}

