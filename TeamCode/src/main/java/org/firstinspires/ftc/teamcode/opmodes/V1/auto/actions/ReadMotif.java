package org.firstinspires.ftc.teamcode.opmodes.V1.auto.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.Robot;

public class ReadMotif implements Action {
    int motif;

    private final Robot robot;

    public ReadMotif(Robot robot) {
        this.robot = robot;

        motif = 0;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (robot.limelight.getMotif() != 0) {
            motif = robot.limelight.getMotif();
            return true;
        }
        return false;
    }
}

