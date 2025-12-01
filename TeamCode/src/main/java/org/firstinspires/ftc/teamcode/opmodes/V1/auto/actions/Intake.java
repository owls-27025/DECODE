package org.firstinspires.ftc.teamcode.opmodes.V1.auto.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.Robot;

public class Intake implements Action {
    public boolean initialized;

    private final Robot robot;

    public Intake(Robot robot) {
        this.robot = robot;

        initialized = false;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            robot.currentState = robot.IntakeState.MOVING_TO_POSITION;
            initialized = true;
        }
        return robot.mechanisms.intakeAuto();
    }

}