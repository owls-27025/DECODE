package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

import java.util.ArrayDeque;
import java.util.Deque;

public class IntakeAction extends BaseAction {
    private State state;

    private enum State {
        FORWARD,
        REVERSE,
        STOP
    }

    public IntakeAction(Robot robot) {
        super(robot);
        enter(State.FORWARD);
    }

    private void enter(State next) {
//        stack.push(state);
        state = next;
    }

//    private final Deque<State> stack = new ArrayDeque<>();

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
//        if (robot.intakeReversed) {
//            enter(State.REVERSE);
//        } else if (robot.intakeReverseCompleted && !stack.isEmpty()) {
//            state = stack.pop();
//            robot.intakeReverseCompleted = false;
//        }
        switch (state) {
            case FORWARD:
                intake.start();
                if (robot.artifactCount >= 3) {
                    enter(State.STOP);
                }
                break;

            case STOP:
                intake.stop();
                break;

            case REVERSE:
                intake.reverse();
                break;
        }

        telemetry.addData("Intake State", state);
        return true;
    }

    @Override
    protected void onCancel() {
        intake.stop();
    }

}