package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.teamcode.Robot;

public class ReadMotif extends BaseAction {

    public ReadMotif(Robot robot) {
        super(robot);
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (limelight.getMotif()) {
            int offset = shooter.calculateMotifOffset(Robot.Globals.Colors.GPP);
            if (offset == 0) return false;
            if (offset == 1) { spindexer.moveToNextPosition(); return false; }
            if (offset == -1) { spindexer.moveToPreviousPosition(); return false; }
            return false;
        }
        return true;
    }
}
