package org.firstinspires.ftc.teamcode.auto.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.mechanisms.limelight.Limelight;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;

public class ReadMotif implements Action {
    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (Limelight.getMotif()) {
            // first letter is current position, second letter is position counterclockwise
            if (ShooterHelper.calculateMotifOffset(Globals.Colors.GPP) == 0) {
                return false;
            } else if (ShooterHelper.calculateMotifOffset(Globals.Colors.GPP) == 1) {
                SpindexerHelper.moveToNextPosition();
                return false;
            } else if (ShooterHelper.calculateMotifOffset(Globals.Colors.GPP) == -1) {
                SpindexerHelper.moveToPreviousPosition();
                return false;
            }
            return false;
        }
        return true;
    }
}

