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

    private States state;

    private final ElapsedTime spindexerTimer;
    private final ElapsedTime stateTimer;

    private boolean timerStarted;

    public SpindexerAction(Robot robot) {
        super(robot);
        spindexerTimer = new ElapsedTime();
        stateTimer = new ElapsedTime();
        enter(States.START);
    }

    private void enter(States state) {
        this.state = state;
        spindexerTimer.reset();
        stateTimer.reset();
        robot.spindexerReady = false;
    }

    @Override
    public boolean run(@NotNull TelemetryPacket telemetryPacket) {
        telemetry.addLine("Spindexer State: " + state + " Time in State:" + stateTimer.time(TimeUnit.SECONDS));
        telemetry.addLine("Timer Value: " + spindexerTimer.time(TimeUnit.MILLISECONDS));
        if (robot.artifactCount <= 0) {
            robot.startShoot = false;
        }
        switch(state) {
            case START:
                spindexer.intakePosition();
                enter(States.INTAKE_POS);
                break;
            case INTAKE_POS:
                if (spindexerTimer.time(TimeUnit.MILLISECONDS) >= 250) {
                    if (distance.isBall()) {
                        if (robot.artifactCount < 3) {
                            spindexer.moveToNextPosition();
                            robot.artifactCount++;
                            spindexerTimer.reset();
                        }
                        if (robot.artifactCount >= 3) {
                            spindexer.shootPosition();
                            enter(States.SHOOT_POS);
                        }
                    }
                }
                break;
            case SHOOT_POS:
                if (robot.artifactCount > 0 || robot.manualShoot) {
                    if (Math.abs(spindexer.getCurrent() - spindexer.getTarget()) <= 10) {
                        robot.spindexerReady = true;
                    }
                    if (robot.shooterReady && robot.startShoot) {
                        spindexer.flapUp();
                        if (spindexerTimer.time(TimeUnit.MILLISECONDS) >= 400) {
                            spindexer.flapDown();
                            spindexer.moveToNextPosition();
                            if (!robot.manualShoot) robot.artifactCount--;
                            robot.spindexerReady = false;
                            robot.manualShoot = false;
                        }
                    }
                } else {
                    robot.startShoot = false;
                }
                break;
        }
        return true;
    }
}