package org.firstinspires.ftc.teamcode.opmodes.V1.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanisms.distanceSensor.DistanceSensor;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;

import java.util.concurrent.TimeUnit;

public class Intake implements Action {
    private boolean initialized;

    private final Robot robot;
    private final org.firstinspires.ftc.teamcode.mechanisms.intake.Intake intake;
    private final Spindexer spindexer;
    private final DistanceSensor distanceSensor;

    private boolean delayStarted;
    private final ElapsedTime delayTimer;

    private enum States {
        MOVING_TO_POSITION,
        WAITING_FOR_BALL,
        MOVING_TO_NEXT_POSITION,
        COMPLETED
    }

    private States currentState;

    public Intake(Robot robot) {
        this.robot = robot;
        this.intake = robot.getIntake();
        this.spindexer = robot.getSpindexer();
        this.distanceSensor = robot.getDistanceSensor();
        this.delayTimer = new ElapsedTime();

        initialized = false;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {

        if (!initialized) {
            currentState = States.MOVING_TO_POSITION;
            initialized = true;
        }

        switch (currentState) {
            case MOVING_TO_POSITION:
                delayStarted = false;
                spindexer.intakePosition();
                intake.start();
                currentState = States.WAITING_FOR_BALL;
                break;

            case WAITING_FOR_BALL:
                if (distanceSensor.isBall() && robot.getArtifactCount() < 3) {
                    if (!delayStarted) {
                        delayStarted = true;
                        delayTimer.reset();
                    }
                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 250) {
                        spindexer.moveToNextPosition();
                        robot.setArtifactCount(robot.getArtifactCount() + 1);

                        if (robot.getArtifactCount() >= 3) {
                            currentState = States.COMPLETED;
                        } else {
                            currentState = States.MOVING_TO_NEXT_POSITION;
                        }
                    }
                } else {
                    delayStarted = false;
                }
                break;

            case MOVING_TO_NEXT_POSITION:
                if (!spindexer.isBusy()) {
                    delayStarted = false;
                    currentState = States.WAITING_FOR_BALL;
                }
                break;

            case COMPLETED:
                intake.stop();
                spindexer.shootPosition();
                return false;
        }
        return true;
    }
}
