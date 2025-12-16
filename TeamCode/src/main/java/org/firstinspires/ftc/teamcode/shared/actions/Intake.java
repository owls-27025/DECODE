package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import org.firstinspires.ftc.teamcode.Robot;

public class Intake extends BaseAction {
    private State state = State.FORWARD;

    private enum State {
        FORWARD,
        REVERSE,
        STOP
    }

    public Intake(Robot robot) {
        super(robot);
    }

    private void enter(State next) {
        state = next;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        switch (state) {
            case FORWARD:
                intake.start();
                if (robot.artifactCount >= 3) {
                    enter(State.STOP);
                }
                break;

            case STOP:
                intake.stop();
                return false;

            case REVERSE:
                intake.reverse();
                if (robot.artifactCount >= 3) {
                    enter(State.STOP);
                }
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