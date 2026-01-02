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
        SHOOT_POS,
        HUMAN_PLAYER
    }

    private States state;

    private final ElapsedTime spindexerTimer;
    private final ElapsedTime stateTimer;

    private boolean timerStarted;
    @SuppressWarnings("FieldCanBeLocal")
    private boolean shotRequested;
    private int shotsRemaining;
    @SuppressWarnings("FieldCanBeLocal")
    private boolean humanPlayerRequested;
    @SuppressWarnings("FieldCanBeLocal")
    private boolean intakeRequested;

    private int[] positions;

    private boolean advanceHumanIntake;
    private int humanSlotIndex;

    public SpindexerAction(Robot robot) {
        super(robot);
        spindexerTimer = new ElapsedTime();
        stateTimer = new ElapsedTime();
        enter(States.START);
        positions = new int[3];
        java.util.Arrays.fill(positions, -1);
        humanSlotIndex = 0;
    }

    private void enter(States state) {
        this.state = state;
        spindexerTimer.reset();
        stateTimer.reset();
        robot.spindexerReady = false;
        timerStarted = false;

        if (state == States.SHOOT_POS) {
            robot.intakeComplete = true;

            if (!robot.manualShoot) {
                if (positions[0] != -1) {
                    spindexer.goToTicks(positions[0] + (3 * Robot.Globals.tpr));
                } else {
                    spindexer.shootPosition();
                }
            }

            if (robot.manualShoot) {
                shotsRemaining = 1;
            } else if (robot.startShoot) {
                shotsRemaining = robot.artifactCount;
            } else {
                shotsRemaining = 0;
            }
        } else if (state == States.INTAKE_POS) {
            spindexer.intakePosition();
            robot.startIntake = true;
            robot.intakeComplete = false;
        } else if (state == States.TRANSITION) {
            spindexer.shootPosition();
        } else if (state == States.HUMAN_PLAYER) {
            robot.intakeComplete = true;

            if (robot.artifactCount >= 3) {
                spindexer.shootPosition();
                return;
            }

            int tpr = Robot.Globals.tpr;

            int nextEmptyIntake;
            if (robot.artifactCount == 0 || positions[robot.artifactCount - 1] == -1) {
                spindexer.shootPosition();
                nextEmptyIntake = spindexer.getTarget();
            } else {
                nextEmptyIntake = positions[robot.artifactCount - 1] + (2 * tpr);
            }

            spindexer.goToTicks(nextEmptyIntake + (3 * tpr));
        }
    }

    @Override
    public boolean run(@NotNull TelemetryPacket telemetryPacket) {
        dbgLine("Spindexer State: " + state + " Time in State:" + stateTimer.time(TimeUnit.SECONDS));
        dbgLine("Timer Value: " + spindexerTimer.time(TimeUnit.MILLISECONDS));
        dbgLine("Positions: [0]=" + positions[0] + " [1]=" + positions[1] + " [2]=" + positions[2]);
        dbgLine("Current Position: " + spindexer.findPosition());

        shotRequested = (robot.manualShoot || robot.startShoot) && state != States.SHOOT_POS;
        humanPlayerRequested = robot.isHumanIntake && state != States.HUMAN_PLAYER;
        intakeRequested = robot.startIntake && state != States.INTAKE_POS;

        if (intakeRequested) {
            intakeRequested = false;
            enter(States.INTAKE_POS);
        }

        if (shotRequested) {
            shotRequested = false;
            enter(States.SHOOT_POS);
        }

        if (humanPlayerRequested) {
            humanPlayerRequested = false;
            enter(States.HUMAN_PLAYER);
        }

        if (robot.leftRequested || robot.rightRequested) {
            if (state == States.HUMAN_PLAYER) {
                advanceHumanIntake = true;
            } else {
                if (robot.leftRequested) {
                    spindexer.moveHalfPosition(false);
                } else if  (robot.rightRequested) {
                    spindexer.moveHalfPosition(true);
                }
            }
            robot.leftRequested = false;
            robot.rightRequested = false;
        }

        switch (state) {
            case START:
                enter(States.INTAKE_POS);
                break;

            case INTAKE_POS:
                if (distance.isBall() && !spindexer.isBusy()) {
                    if (robot.artifactCount < 3) {
                        positions[robot.artifactCount] = spindexer.getTarget();

                        spindexer.moveToNextPosition();
                        robot.artifactCount++;

                        spindexerTimer.reset();
                    }
                }

                if (robot.artifactCount >= 3) {
                    enter(States.TRANSITION);
                }
                break;

            case TRANSITION:
                break;

            case SHOOT_POS:
                if ((shotsRemaining > 0 && robot.artifactCount > 0) || robot.manualShoot) {

                    if (Math.abs(spindexer.getCurrent() - spindexer.getTarget()) <= 10) {
                        robot.spindexerReady = true;
                    }

                    if (robot.shooterReady && robot.spindexerReady) {
                        if (!timerStarted) {
                            spindexerTimer.reset();
                            timerStarted = true;
                            spindexer.flapUp();
                        }

                        if (spindexerTimer.time(TimeUnit.MILLISECONDS) >= 400) {
                            spindexer.flapDown();

                            spindexer.moveToNextPosition();

                            if (!robot.manualShoot) {
                                robot.artifactCount--;
                                shotsRemaining--;

                                positions[0] = positions[1];
                                positions[1] = positions[2];
                                positions[2] = -1;
                            } else {
                                shotsRemaining--;
                            }

                            robot.spindexerReady = false;
                            timerStarted = false;
                            spindexerTimer.reset();

                            if (shotsRemaining == 0) {
                                robot.manualShoot = false;
                                robot.startShoot = false;

                                java.util.Arrays.fill(positions, -1);

                                enter(States.INTAKE_POS);
                            } else {
                                if (!robot.manualShoot) {
                                    if (positions[0] != -1) {
                                        spindexer.goToTicks(positions[0] + (3 * Robot.Globals.tpr));
                                    } else {
                                        spindexer.shootPosition();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //noinspection DataFlowIssue
                    robot.manualShoot = false;
                    robot.startShoot = false;

                    positions[0] = -1;
                    positions[1] = -1;
                    positions[2] = -1;

                    enter(States.INTAKE_POS);
                }
                break;
            case HUMAN_PLAYER:
                if (!robot.isHumanIntake) {
                    enter(States.TRANSITION);
                }

                if (Math.abs(spindexer.getCurrent() - spindexer.getTarget()) <= 10) {
                    robot.spindexerReady = true;
                }

                if (robot.spindexerReady && spindexerTimer.time(TimeUnit.MILLISECONDS) >= 250) {
                    if (advanceHumanIntake) {
                        advanceHumanIntake = false;
                        if (robot.artifactCount < 3) {
                            positions[robot.artifactCount] = spindexer.getTarget() - (3 * Robot.Globals.tpr);

                            robot.artifactCount++;

                            spindexer.moveToNextPosition();

                            robot.spindexerReady = false;
                            spindexerTimer.reset();

                            if (robot.artifactCount >= 3) {
                                robot.isHumanIntake = false;
                                enter(States.TRANSITION);
                            }
                        }
                    }
                }
                break;
        }

        return true;
    }
}
