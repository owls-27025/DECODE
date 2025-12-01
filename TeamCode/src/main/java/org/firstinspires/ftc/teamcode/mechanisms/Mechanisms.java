package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.Controls;
import org.firstinspires.ftc.teamcode.mechanisms.distanceSensor.DistanceSensor;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.intake.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.light.Light;
import org.firstinspires.ftc.teamcode.mechanisms.shooter.Shooter;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;

import java.util.concurrent.TimeUnit;

public class Mechanisms {

    private final double shooterVelocity;
    private final double shooterTolerance;
    private final int forcedArtifacts;

    private final Spindexer spindexer;
    private final DistanceSensor distanceSensor;
    private final Shooter shooter;
    private final Intake intake;
    private final Drivetrain drivetrain;
    private final Light light;

    public ElapsedTime delayTimer;
    public boolean delayStarted;
    public boolean spindexerMoved;
    public int initialArtifactCount;

    public String[] colors;
    public int[] hues;
    public String[] motif;

    public int artifactCount;

    boolean isDetected;

    public enum IntakeState {
        INIT,
        MOVING_TO_POSITION,
        WAITING_FOR_BALL,
        MOVING_TO_NEXT_POSITION,
        COMPLETED
    }

    public IntakeState currentState;

    public enum ShootState {
        INIT,
        MOVING_TO_SHOOT_POSITION,
        SPINNING_UP_SHOOTER,
        FIRING,
        ADVANCING_NEXT_BALL,
        COMPLETED
    }

    public ShootState currentShootState;

    public enum AutoShootState {
        INIT,
        MOVING_TO_SHOOT_POSITION,
        SPINNING_UP_SHOOTER,
        FIRING,
        ADVANCING_NEXT_BALL,
        COMPLETED
    }

    public AutoShootState currentAutoShootState;

    public enum HumanState {
        INIT,
        WAITING,
        MOVING,
        COMPLETED
    }

    public HumanState currentHumanState;

    private int shotsLeft;
    @SuppressWarnings("unused")
    private long lastCheckTime;

    private final Telemetry telemetry;

    private boolean shootStarted;

    public boolean isHumanIntake;

    public Mechanisms(Spindexer spindexer,
                      DistanceSensor distanceSensor,
                      Shooter shooter,
                      Intake intake,
                      Drivetrain drivetrain,
                      Light light,
                      Telemetry telemetry,
                      double shooterVelocity,
                      double shooterTolerance,
                      int forcedArtifacts) {

        this.spindexer = spindexer;
        this.distanceSensor = distanceSensor;
        this.shooter = shooter;
        this.intake = intake;
        this.drivetrain = drivetrain;
        this.light = light;
        this.telemetry = telemetry;

        this.shooterVelocity = shooterVelocity;
        this.shooterTolerance = shooterTolerance;
        this.forcedArtifacts = forcedArtifacts;

        artifactCount = 0;
        currentState = IntakeState.INIT;
        currentShootState = ShootState.INIT;
        currentAutoShootState = AutoShootState.INIT;
        currentHumanState = HumanState.INIT;
        shotsLeft = 0;

        isDetected = false;

        delayStarted = false;
        delayTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        shootStarted = false;

        isHumanIntake = false;
    }

    // ---------------- INTAKE (TeleOp) ----------------

    public void intake(Controls controls) {
        // using driver2 and inhibitButtons from Controls
        if (currentState == IntakeState.INIT) {
            if (controls.d2AJustPressed && !controls.inhibitButtons) {
                currentState = IntakeState.MOVING_TO_POSITION;
            }
        } else {
            if (controls.d2AJustPressed && !controls.inhibitButtons) {
                currentState = IntakeState.MOVING_TO_POSITION;
            }
            switch (currentState) {
                case MOVING_TO_POSITION:
                    isDetected = false;
                    spindexer.intakePosition();
                    intake.start();
                    currentState = IntakeState.WAITING_FOR_BALL;
                    break;

                case WAITING_FOR_BALL:
                    if (distanceSensor.isBall() && !isDetected && artifactCount < 3) {
                        isDetected = true;
                        spindexer.moveToNextPosition();
                        artifactCount++;

                        if (artifactCount >= 3) {
                            currentState = IntakeState.COMPLETED;
                        } else {
                            currentState = IntakeState.MOVING_TO_NEXT_POSITION;
                        }
                    }
                    break;

                case MOVING_TO_NEXT_POSITION:
                    if (!spindexer.isBusy()) {
                        currentState = IntakeState.WAITING_FOR_BALL;
                    }
                    isDetected = false;
                    break;

                case COMPLETED:
                    intake.stop();
                    spindexer.shootPosition();
                    currentState = IntakeState.INIT;
                    break;
            }
        }
    }

    // ---------------- INTAKE (Auto) ----------------

    public boolean intakeAuto() {
        switch (currentState) {
            case MOVING_TO_POSITION:
                isDetected = false;
                artifactCount = 0;
                spindexer.intakePosition();
                intake.start();

                delayStarted = false;
                currentState = IntakeState.WAITING_FOR_BALL;
                break;

            case WAITING_FOR_BALL:
                if (distanceSensor.isBall() && artifactCount < 3) {
                    if (!delayStarted) {
                        delayStarted = true;
                        delayTimer.reset();
                    }
                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 250) {
                        spindexer.moveToNextPosition();
                        telemetry.addLine("yo");
                        telemetry.update();

                        artifactCount++;

                        if (artifactCount >= 3) {
                            currentState = IntakeState.COMPLETED;
                        } else {
                            currentState = IntakeState.MOVING_TO_NEXT_POSITION;
                        }
                    }
                }
                break;

            case MOVING_TO_NEXT_POSITION:
                if (!spindexer.isBusy()) {
                    currentState = IntakeState.WAITING_FOR_BALL;
                }

                delayStarted = false;
                isDetected = false;
                break;

            case COMPLETED:
                intake.stop();
                spindexer.shootPosition();
                currentState = IntakeState.INIT;
                return true;
        }
        return false;
    }

    // ---------------- SHOOT (TeleOp) ----------------

    public void shoot(Controls controls) {
        if (!isHumanIntake) {
            shooter.shoot(shooterVelocity);
        }

        if (currentShootState == ShootState.INIT) {
            if (controls.d2XJustPressed) {
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            } else if (controls.d2YJustPressed) {
                artifactCount = forcedArtifacts;
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            }
        } else if (currentShootState == ShootState.COMPLETED) {
            if (controls.d2XJustPressed) {
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            } else if (controls.d2YJustPressed) {
                artifactCount = forcedArtifacts;
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            }
            currentShootState = ShootState.INIT;
        } else if (artifactCount > 0) {
            if (controls.d2XJustPressed) {
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            } else if (controls.d2YJustPressed) {
                artifactCount = forcedArtifacts;
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            }
            switch (currentShootState) {
                case MOVING_TO_SHOOT_POSITION:
                    spindexer.shootPosition();
                    shotsLeft = artifactCount;
                    initialArtifactCount = artifactCount;
                    currentShootState = ShootState.SPINNING_UP_SHOOTER;
                    delayStarted = false;
                    break;

                case SPINNING_UP_SHOOTER:
                    double targetVelocity = shooterVelocity;

                    shooter.shoot(targetVelocity);

                    if (!delayStarted) {
                        delayTimer.reset();
                        delayStarted = true;
                    }

                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 100) {
                        delayTimer.reset();
                        if (Math.abs(shooter.getVelocity() - targetVelocity) <= shooterTolerance) {
                            delayStarted = false;
                            currentShootState = ShootState.FIRING;
                        }
                    }
                    break;

                case FIRING:
                    spindexer.moveServo(1);
                    if (!delayStarted) {
                        delayTimer.reset();
                        delayStarted = true;
                    }
                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 500) {
                        spindexer.moveServo(0.5);
                        delayStarted = false;
                        spindexerMoved = false;
                        currentShootState = ShootState.ADVANCING_NEXT_BALL;
                    }
                    break;

                case ADVANCING_NEXT_BALL:
                    if (!spindexerMoved) {
                        if (initialArtifactCount == 2) {
                            spindexer.setMotorDirection(DcMotor.Direction.REVERSE);
                            spindexer.moveToNextPosition();
                            spindexer.setMotorDirection(DcMotor.Direction.FORWARD);
                        } else {
                            spindexer.moveToNextPosition();
                        }
                        spindexerMoved = true;
                    }

                    if (!delayStarted) {
                        delayTimer.reset();
                        delayStarted = true;
                    }
                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 100) {
                        delayTimer.reset();
                        if (!spindexer.isBusy()) {
                            artifactCount--;
                            shotsLeft--;

                            if (shotsLeft <= 0) {
                                currentShootState = ShootState.COMPLETED;
                            } else {
                                currentShootState = ShootState.SPINNING_UP_SHOOTER;
                            }
                        }
                    }
                    break;

                case COMPLETED:
                    currentShootState = ShootState.INIT;
                    break;
            }
        }
    }

    // ---------------- HUMAN INTAKE ----------------

    public void intakeHuman(Controls controls) {
        if (controls.d2BackJustPressed) {
            if (shooter.getVelocity() > 0) {
                shooter.shoot(0);
                isHumanIntake = true;
            } else {
                shooter.shoot(shooterVelocity);
                isHumanIntake = false;
            }
        }
    }

    // ---------------- SHOOT (Auto) ----------------

    public boolean shootAuto(int numArtifacts) {
        switch (currentAutoShootState) {
            case MOVING_TO_SHOOT_POSITION:
                telemetry.addLine("running");
                telemetry.update();

                spindexer.shootPosition();
                shotsLeft = numArtifacts;
                currentAutoShootState = AutoShootState.SPINNING_UP_SHOOTER;
                delayStarted = false;
                break;

            case SPINNING_UP_SHOOTER:
                telemetry.addLine("spinning up shooter");
                telemetry.update();

                double targetVelocity = shooterVelocity;
                shooter.shoot(targetVelocity);

                if (Math.abs(shooter.getVelocity() - targetVelocity) <= shooterTolerance) {
                    currentAutoShootState = AutoShootState.FIRING;
                }
                break;

            case FIRING:
                spindexer.moveServo(1);
                if (!delayStarted) {
                    delayTimer.reset();
                    delayStarted = true;
                }
                if (delayTimer.time(TimeUnit.MILLISECONDS) > 500) {
                    spindexer.moveServo(0.5);
                    delayStarted = false;
                    spindexerMoved = false;
                    currentAutoShootState = AutoShootState.ADVANCING_NEXT_BALL;
                }
                break;

            case ADVANCING_NEXT_BALL:
                if (!spindexerMoved) {
                    if (numArtifacts == 2) {
                        spindexer.setMotorDirection(DcMotor.Direction.REVERSE);
                        spindexer.moveToNextPosition();
                        spindexer.setMotorDirection(DcMotor.Direction.FORWARD);
                    } else {
                        spindexer.moveToNextPosition();
                    }
                    spindexerMoved = true;
                }

                if (!delayStarted) {
                    delayTimer.reset();
                    delayStarted = true;
                }
                if (delayTimer.time(TimeUnit.MILLISECONDS) > 100) {
                    delayTimer.reset();
                    if (!spindexer.isBusy()) {
                        artifactCount--;
                        shotsLeft--;

                        if (shotsLeft <= 0) {
                            currentAutoShootState = AutoShootState.COMPLETED;
                        } else {
                            currentAutoShootState = AutoShootState.SPINNING_UP_SHOOTER;
                        }
                    }
                }
                break;

            case COMPLETED:
                currentAutoShootState = AutoShootState.INIT;
                return false;
        }
        return true;
    }
}
