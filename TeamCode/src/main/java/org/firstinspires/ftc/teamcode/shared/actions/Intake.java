package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import org.firstinspires.ftc.teamcode.Robot;

public class Intake extends BaseAction {
    private State state = State.MOVING_TO_POSITION;

    private boolean lastBall = false;
    private boolean requestedIndex = false;

    private enum State {
        MOVING_TO_POSITION,
        WAITING_FOR_BALL,
        INDEXING,
        COMPLETED
    }

    public Intake(Robot robot) {
        super(robot);
    }

    private void enter(State next) {
        state = next;
        if (next == State.WAITING_FOR_BALL) {
            requestedIndex = false;
        }
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (isCancelled()) return false;

        boolean ballNow = distance.isBall();
        boolean ballRisingEdge = ballNow && !lastBall;
        lastBall = ballNow;

        switch (state) {
            case MOVING_TO_POSITION:
                spindexer.intakePosition();
                intake.start();
                enter(State.WAITING_FOR_BALL);
                break;

            case WAITING_FOR_BALL:
                if (robot.artifactCount >= 3) {
                    enter(State.COMPLETED);
                    break;
                }

                if (ballRisingEdge && !requestedIndex) {
                    spindexer.moveToNextPosition();
                    requestedIndex = true;
                    enter(State.INDEXING);
                }
                break;

            case INDEXING:
                if (!spindexer.isBusy()) {
                    robot.artifactCount++;
                    enter(robot.artifactCount >= 3 ? State.COMPLETED : State.WAITING_FOR_BALL);
                }
                break;

            case COMPLETED:
                intake.stop();
                spindexer.shootPosition();
                return false;
        }
        if (!robot.intakeReversed) {
            intake.start();
        }
        return true;
    }

    @Override
    protected void onCancel() {
        intake.stop();
        spindexer.shootPosition();
    }

}