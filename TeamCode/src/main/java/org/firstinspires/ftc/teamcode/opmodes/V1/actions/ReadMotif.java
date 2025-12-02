package org.firstinspires.ftc.teamcode.opmodes.V1.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanisms.limelight.Limelight;

public class ReadMotif implements Action {
    int motif;

    private final Robot robot;
    private final Limelight limelight;

    public ReadMotif(Robot robot) {
        this.robot = robot;
        this.limelight = robot.getLimelight();

        motif = 0;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (limelight.getMotif() != 0) {
            motif = limelight.getMotif();
            return true;
        }
        return false;
    }
}

