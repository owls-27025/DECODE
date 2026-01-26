package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import org.firstinspires.ftc.teamcode.Robot;

public class IntakeAction extends BaseAction {
    private State state;
    private State previousState;

    private enum State {
        FORWARD,
        REVERSE,
        STOP
    }

    public IntakeAction(Robot robot) {
        super(robot);
        enter(State.FORWARD);
        previousState = state;
    }

    private void enter(State next) {
        previousState = state;
        state = next;
    }


    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (robot.startIntake && state != State.FORWARD) {
            enter(State.FORWARD);
        }

        if (robot.intakeReversed && state != State.REVERSE) {
            enter(State.REVERSE);
        }

        if (robot.forceStop) {
            enter(State.STOP);
        }

        switch (state) {
            case FORWARD:
                intake.start();
                if (robot.artifactCount >= 3 || robot.intakeComplete) {
                    enter(State.STOP);
                }
                break;

            case STOP:
                intake.stop();
                break;

            case REVERSE:
                intake.reverse();
                if (robot.intakeReverseCompleted) {
                    enter(previousState);
                }
                break;
        }

//        dbg("Intake State", state);
        return true;
    }

    @Override
    protected void onCancel() {
        intake.stop();
    }

}