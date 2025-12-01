package org.firstinspires.ftc.teamcode.opmodes.V1.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanisms.shooter.Shooter;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;

import java.util.concurrent.TimeUnit;

public class Shoot implements Action {
    public int artifacts;
    public int initialArtifacts;
    public boolean initialized;

    private final Robot robot;
    private final Spindexer spindexer;
    private final Shooter shooter;

    private enum States {
        MOVING_TO_SHOOT_POSITION,
        SPINNING_UP_SHOOTER,
        FIRING,
        ADVANCING_NEXT_BALL,
        COMPLETED
    }

    private States currentState;

    private boolean delayStarted;
    private final ElapsedTime delayTimer;

    private boolean spindexerMoved;


    public Shoot(Robot robot, int numArtifacts) {
        this.robot = robot;
        this.spindexer = robot.spindexer;
        this.shooter = robot.shooter;

        artifacts = numArtifacts;
        this.delayTimer = new ElapsedTime();
        initialized = false;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            currentState = States.MOVING_TO_SHOOT_POSITION;
            initialArtifacts = artifacts;
            initialized = true;
        }

        switch (currentState) {
            case MOVING_TO_SHOOT_POSITION:
                spindexer.shootPosition();

                currentState = States.SPINNING_UP_SHOOTER;
                delayStarted = false;
                break;

            case SPINNING_UP_SHOOTER:
                double targetVelocity = robot.shooterVelocity;
                shooter.shoot(targetVelocity);

                if (Math.abs(shooter.getVelocity() - targetVelocity) <= robot.shooterTolerance) {
                    currentState = States.FIRING;
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
                    currentState = States.ADVANCING_NEXT_BALL;
                }
                break;

            case ADVANCING_NEXT_BALL:
                if (!spindexerMoved) {
                    if (artifacts == 2) {
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
                        artifacts--;

                        if (artifacts <= 0) {
                            currentState = States.COMPLETED;
                        } else {
                            currentState = States.SPINNING_UP_SHOOTER;
                        }
                    }
                }
                break;

            case COMPLETED:
                return false;
        }
        return true;
    }
}

