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
        STOP,
        INDEXING,
        SHOOT_POS,
        HUMAN_PLAYER,
        TRANSITION
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

            if (!robot.sort) {
                dbgLine("Entering wrong if");
                if (!robot.manualShoot) {
                    if (positions[0] != -1) {
                        spindexer.goToTicks(positions[0] + (3 * Robot.Globals.tpr));
                    } else {
                        spindexer.shootPosition();
                    }
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
        } else if (state == States.STOP) {
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
                spindexer.goToTicks(nextEmptyIntake + (3 * tpr));
            }
        }
    }

    public void setColors(Robot.Globals.Colors colors) {
        robot.colors = colors;
        robot.sort = true;
    }

    @Override
    public boolean run(@NotNull TelemetryPacket telemetryPacket) {
//        dbgLine("Spindexer State: " + state + " Time in State:" + stateTimer.time(TimeUnit.SECONDS));
//        dbgLine("Timer Value: " + spindexerTimer.time(TimeUnit.MILLISECONDS));
//        dbgLine("Positions: [0]=" + positions[0] + " [1]=" + positions[1] + " [2]=" + positions[2]);
//        dbgLine("Current Position: " + spindexer.findPosition());

        shotRequested = (robot.manualShoot || robot.startShoot) && state != States.SHOOT_POS && state != States.INDEXING;
        humanPlayerRequested = robot.isHumanIntake && state != States.HUMAN_PLAYER;
        intakeRequested = robot.startIntake && state != States.INTAKE_POS;

            if (robot.stop) {
                enter(States.STOP);
                shotRequested = false;
                humanPlayerRequested = false;
                intakeRequested = false;
            }

            if (shotRequested) {
                shotRequested = false;
                robot.startIntake = false;
                if (!robot.sort) {
                    enter(States.SHOOT_POS);
                } else {
                    enter(States.INDEXING);
                }
            }

            if (intakeRequested) {
                intakeRequested = false;
                enter(States.INTAKE_POS);
            }

            if (humanPlayerRequested) {
                humanPlayerRequested = false;
                enter(States.HUMAN_PLAYER);
            }

            if (robot.leftRequested || robot.rightRequested) {
                if (state == States.HUMAN_PLAYER) {
                    advanceHumanIntake = true;
                } else {
                    if (robot.leftRequested && !spindexer.isBusy()) {
                        spindexer.moveHalfPosition(false);
                    } else if (robot.rightRequested && !spindexer.isBusy()) {
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
                    robot.startIntake = false;
                    if (distance.isBall() && !spindexer.isBusy()) {
                        if (robot.artifactCount < 3) {
                            positions[robot.artifactCount] = spindexer.getTarget();

                            spindexer.moveToNextPosition();
                            robot.artifactCount++;

                            spindexerTimer.reset();
                        }
                    }

                    if (robot.artifactCount >= 3) {
                        enter(States.STOP);
                    }
                    break;

                case STOP:
                    robot.stop = false;
                    break;

                case SHOOT_POS:
                    if (robot.sort) {
                        robot.sort = false;
                    }

//                    dbgLine("Entered shoot");
                    if ((shotsRemaining > 0 && robot.artifactCount > 0) || robot.manualShoot) {
//                        dbgLine("Shots remaining:" + shotsRemaining + ", artifact count: " + robot.artifactCount);
                        if (Math.abs(spindexer.getCurrent() - spindexer.getTarget()) <= 10) {
                            robot.spindexerReady = true;
                        }

//                        dbg("Shooter Ready", robot.shooterReady);
//                        dbg("Spindexer Ready", robot.spindexerReady);
//                        dbg("Spindexer Timer", spindexerTimer.time());
                        if (robot.shooterReady && robot.spindexerReady) {
//                            dbgLine("Shooter/spindexer ready");

                            if (!timerStarted) {
                                spindexerTimer.reset();
                                timerStarted = true;
                                dbgLine("Flap Almost Up");
                                spindexer.flapUp();
                                dbgLine("Flap Up");
                            }

                            if (spindexerTimer.time(TimeUnit.MILLISECONDS) >= 400) {
                                dbgLine("Flap Almost Down");
                                spindexer.flapDown();
                                dbgLine("Flap Down");

                                spindexer.moveToNextPosition();
                                dbgLine("Move to next position");

                                if (robot.artifactCount > 0) robot.artifactCount--;
                                shotsRemaining--;
                                positions[0] = positions[1];
                                positions[1] = positions[2];
                                positions[2] = -1;

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
                        enter(States.STOP);
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
                                    enter(States.STOP);
                                }
                            }
                        }
                    }
                    break;
                case INDEXING:
                    dbgLine("Entered indexing");
                    int intakeStart = positions[0] != -1 ? positions[0] + (3 * Robot.Globals.tpr) : spindexer.getTarget();

                    if (Math.abs(spindexer.getCurrent() - intakeStart) > 10) {
                        spindexer.goToTicks(intakeStart);
                        break;
                    }

                    int motifOffset = calculateMotifOffset(robot.colors);
                    if (motifOffset == 1) {
                        spindexer.moveToNextPosition();
                    } else if (motifOffset == -1) {
                        spindexer.moveToPreviousPosition();
                    }

                    enter(States.SHOOT_POS);
            }
        return true;
    }

    public int calculateMotifOffset(Robot.Globals.Colors colors) {
        int diff = Robot.Globals.motif.index - colors.index;

        if (diff == 2) diff = -1;
        if (diff == -2) diff = 1;

        return diff;
    }
}
