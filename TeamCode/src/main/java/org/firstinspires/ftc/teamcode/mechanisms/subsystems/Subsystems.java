package org.firstinspires.ftc.teamcode.mechanisms.subsystems;

import static org.firstinspires.ftc.teamcode.Helpers.easeInOutSine;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper.SpindexerMotor;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper.intakePosition;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper.shootPosition;
import static org.firstinspires.ftc.teamcode.teleop.V1.currentSpeed;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.intake.IntakeHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.teleop.V1;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;

public class Subsystems {
    public final static int HALF_SLOT_TICKS = 48;

    public static double SHOOTER_VELOCITY = 1000;
    public static double SUBTRACTION_VELOCITY = 50;

    public static ElapsedTime delayTimer;
    public static boolean delayStarted;

    //There are 6 positions on the spindexer each represented as below
    //              3
    //      2       *       4
    //          *   *   *
    //              *
    //          *   *   *
    //      1       *       5
    //              0

    public static final double[] intakePositions = new double[]{1.0,3.0,5.0};
    public static final double[] shootPositions = new double[]{0.0,2.0,4.0};
    public static String[] colors;
    public static int[] hues;
    public static String[] motif;

    public static int artifactCount;

    static boolean isDetected;


    public enum IntakeState {
        INIT,
        MOVING_TO_POSITION,
        WAITING_FOR_BALL,
        MOVING_TO_NEXT_POSITION,
        COMPLETED
    }

    public static IntakeState currentState;


    public enum ShootState {
        INIT,
        MOVING_TO_SHOOT_POSITION,
        SPINNING_UP_SHOOTER,
        FIRING,
        ADVANCING_NEXT_BALL,
        COMPLETED
    }

    public static ShootState currentShootState;
    private static int shotsLeft;
    private static long lastCheckTime;

    private static Telemetry subsystemTelemetry;

    public static void init(HardwareMap hardwareMap, Telemetry telemetry) {
        // init subsystems
        SpindexerHelper.init(hardwareMap);
        ColorSensorHelper.init(hardwareMap);
        ShooterHelper.init(hardwareMap);
        IntakeHelper.init(hardwareMap);
        Drivetrain.init(hardwareMap);

        // variables
        artifactCount = 3;
        currentState = IntakeState.INIT;
        currentShootState = ShootState.INIT;
        shotsLeft = 0;

        subsystemTelemetry = telemetry;

        isDetected = false;

        delayStarted = false;
        delayTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public static void intake(Gamepad gamepad2) {
        if (currentState == IntakeState.INIT) {
            if (gamepad2.aWasPressed()) {
                currentState = IntakeState.MOVING_TO_POSITION;
            } else if (gamepad2.bWasPressed()) {
                currentState = IntakeState.COMPLETED;
            }
        } else {
            if (gamepad2.bWasPressed()) {
                currentState = IntakeState.COMPLETED;
                return;
            } else if (gamepad2.aWasPressed()) {
                currentState = IntakeState.MOVING_TO_POSITION;
            }
            switch (currentState) {
                case MOVING_TO_POSITION:
                    isDetected = false;
                    intakePosition();
                    IntakeHelper.start();

                    delayStarted = false;

                    currentState = IntakeState.WAITING_FOR_BALL;
                    break;

                case WAITING_FOR_BALL:
                    if (ColorSensorHelper.isBall() && !isDetected && artifactCount < 3) {
                        if (delayStarted == false) {
                            delayTimer.reset();
                            delayStarted = true;
                        }
                    }
                    if (delayStarted && delayTimer.time(TimeUnit.MILLISECONDS) > 500) {
                        delayTimer.reset();
                        delayStarted = false;
                        isDetected = true;

                        SpindexerHelper.moveToNextPosition();

                        artifactCount++;

                        if (artifactCount >= 3) {
                            currentState = IntakeState.COMPLETED;
                        } else {
                            isDetected = false;
                        }
                    }
                    break;

                case COMPLETED:
                    IntakeHelper.stop();
                    currentState = IntakeState.INIT;
                    break;
            }
        }
    }




    public static void shoot(Gamepad gamepad2) {
        if (currentShootState == ShootState.INIT) {
            if (gamepad2.xWasPressed()) {
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            }
        } else if (currentShootState != ShootState.COMPLETED && artifactCount > 0) {
            if (gamepad2.bWasPressed()) {
                currentShootState = ShootState.COMPLETED;
                return;
            } else if (gamepad2.xWasPressed()) {
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            }

            switch (currentShootState) {
                case MOVING_TO_SHOOT_POSITION:
                    shootPosition();
                    shotsLeft = artifactCount;
                    currentShootState = ShootState.SPINNING_UP_SHOOTER;
                    delayStarted = false;
                    break;

                case SPINNING_UP_SHOOTER:
                    double targetVelocity = getTargetVelocity();

                    ShooterHelper.shoot(targetVelocity);

                    if(!delayStarted) {
                        delayTimer.reset();
                        delayStarted = true;
                    }

                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 100) {
                        delayTimer.reset();
                        if (Math.abs(ShooterHelper.shooterMotor.getVelocity() - targetVelocity) <= 5) {
                            currentShootState = ShootState.FIRING;
                        }
                    }
                    break;

                case FIRING:
                    SpindexerHelper.moveServo(1);
                    delayTimer.reset();
                    delayStarted = true;
                    if (delayTimer.milliseconds() - delayTimer.startTime() > 100) {
                        SpindexerHelper.moveServo(0.5);
                        currentShootState = ShootState.ADVANCING_NEXT_BALL;
                    }
                    break;

                case ADVANCING_NEXT_BALL:
                    SpindexerHelper.moveToNextPosition();

                    long checkTime = System.currentTimeMillis();
                    if (checkTime - lastCheckTime >= 100) {
                        lastCheckTime = checkTime;
                        if (!SpindexerHelper.SpindexerMotor.isBusy()) {
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
                    break;
            }
        }
    }

    private static double getTargetVelocity() {
        int shotIndex = shotsLeft == artifactCount ? 0 :
                shotsLeft == artifactCount - 1 ? 1 : 2;

        double targetVelocity;
        if (shotIndex == 0) {
            targetVelocity = SHOOTER_VELOCITY - SUBTRACTION_VELOCITY;
        } else if (shotIndex == 1) {
            targetVelocity = SHOOTER_VELOCITY - (SUBTRACTION_VELOCITY / 2);
        } else {
            targetVelocity = SHOOTER_VELOCITY;
        }
        return targetVelocity;
    }


    public static void drivetrain(Gamepad gamepad1) {
        // speed control
        if(gamepad1.left_bumper) {
            currentSpeed = 0.35;
        } else {
            currentSpeed = 1;
        }

        // field centric toggle
        if(gamepad1.guideWasPressed()) {
            if(!V1.isFieldCentric) {
                V1.isFieldCentric = true;
                return;
            } else {
                V1.isFieldCentric = false;
                return;
            }
        }

        // reset imu
        if(gamepad1.startWasPressed()) {
            Drivetrain.resetIMU();
        }

        // set variables for driving
        double y = easeInOutSine(-gamepad1.left_stick_y);
        double x = easeInOutSine(gamepad1.left_stick_x);
        double rx = easeInOutSine(gamepad1.right_stick_x);

        // calculate field centric variables
        if(V1.isFieldCentric) {
            y = Drivetrain.fieldCentricDrive(x, y)[0];
            x = Drivetrain.fieldCentricDrive(x, y)[1];
        }

        // move drivetrain
        Drivetrain.FL.setPower((y + x + rx) * currentSpeed);
        Drivetrain.BL.setPower((y - x + rx) * currentSpeed);
        Drivetrain.FR.setPower((y - x - rx) * currentSpeed);
        Drivetrain.BR.setPower((y + x - rx) * currentSpeed);
    }
}