package org.firstinspires.ftc.teamcode.shared.actions;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class SpindexerAction extends BaseAction {
    private enum States {
        START,
        INTAKE_POS,
        TRANSITION,
        INDEXING,
        SHOOT_POS
    }

    private States state = States.START;

    private ElapsedTime timer;

    private boolean timerReset;

    public SpindexerAction(Robot robot) {
        super(robot);
        timer = new ElapsedTime();
    }

    private void enter(States state) {
        this.state = state;
        timer.reset();
        robot.spindexerReady = false;
        timerReset = false;
    }

    @Override
    public boolean run(@NotNull TelemetryPacket telemetryPacket) {
        switch(state) {
            case START:
                spindexer.intakePosition();
                enter(States.INTAKE_POS);
                break;
            case INTAKE_POS:
                if (timer.time(TimeUnit.MILLISECONDS) >= 250) {
                    if (distance.isBall()) {
                        if (robot.artifactCount < 3) {
                            spindexer.moveToNextPosition();
                            robot.artifactCount++;
                            timer.reset();
                        }
                        if (robot.artifactCount >= 3) {
                            spindexer.shootPosition();
                            enter(States.SHOOT_POS);
                        }
                    }
                }
                break;
            case SHOOT_POS:
                robot.spindexerReady = true;
                if (robot.shooterReady) {
                    if (!timerReset) {
                        timer.reset();
                        timerReset = true;
                    }
                    spindexer.flapUp();
                    if (timer.time(TimeUnit.MILLISECONDS) >= 400) {
                        spindexer.flapDown();
                        timerReset = false;
                    }
                }
                break;
        }
        return false;
    }
}
