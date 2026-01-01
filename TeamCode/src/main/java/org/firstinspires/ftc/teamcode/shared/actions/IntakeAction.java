package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import org.firstinspires.ftc.teamcode.Robot;

public class IntakeAction extends BaseAction {
    private enum State { FORWARD, REVERSE, STOP }
    private State state = State.FORWARD;
    private State resumeState = State.FORWARD;

    public IntakeAction(Robot robot) { super(robot); }

    private void setState(State next) {
        if (state == next) return;
        if (state != State.REVERSE) resumeState = state;
        state = next;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (isCancelled()) return false;

        boolean full = robot.artifactCount >= 3;

        if (robot.intakeReversed) {
            setState(State.REVERSE);
        } else if (full) {
            setState(State.STOP);
        } else if (robot.startIntake) {
            setState(State.FORWARD);
            robot.startIntake = false;
        } else if (state == State.REVERSE) {
            setState(resumeState);
        }

        switch (state) {
            case FORWARD: intake.start(); break;
            case STOP:    intake.stop(); break;
            case REVERSE: intake.reverse(); break;
        }

        dbg("Intake State", state);
        return true;
    }

    @Override protected void onCancel() { intake.stop(); }
}
