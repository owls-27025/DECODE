package org.firstinspires.ftc.teamcode.mechanisms.subsystems;

import static org.firstinspires.ftc.teamcode.helpers.Helpers.easeInOutSine;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper.*;
import static org.firstinspires.ftc.teamcode.teleop.V1.currentSpeed;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.light.Light;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.DistanceSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.intake.IntakeHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.teleop.V1;

import java.util.concurrent.TimeUnit;

public class Subsystems {
    public final static int HALF_SLOT_TICKS = SpindexerHelper.TPR;

    public static double SHOOTER_VELOCITY = 1100;
    public static double SUBTRACTION_VELOCITY = 50;

    public static ElapsedTime delayTimer;
    public static boolean delayStarted;
    public static boolean spindexerMoved;
    public static int initialArtifactCount;

    // There are 6 positions on the spindexer each represented as below
    //              3
    //      2       *       4
    //          *   *   *
    //              *
    //          *   *   *
    //      1       *       5
    //              0

    public static final double[] intakePositions = new double[]{1.0, 3.0, 5.0};
    public static final double[] shootPositions = new double[]{0.0, 2.0, 4.0};
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

    public static IntakeState currentIntakeState;


    public enum ShootState {
        INIT,
        MOVING_TO_SHOOT_POSITION,
        SPINNING_UP_SHOOTER,
        FIRING,
        ADVANCING_NEXT_BALL,
        COMPLETED
    }

    public static ShootState currentShootState;

    public enum AutoShootState {
        INIT,
        MOVING_TO_SHOOT_POSITION,
        SPINNING_UP_SHOOTER,
        FIRING,
        ADVANCING_NEXT_BALL,
        COMPLETED
    }

    public static AutoShootState currentAutoShootState;

    public enum AutoIntakeState {
        INIT,
        MOVING_TO_POSITION,
        WAITING_FOR_BALL,
        MOVING_TO_NEXT_POSITION,
        COMPLETED
    }

    public static AutoIntakeState currentAutoIntakeState;


    public enum HumanState {
        INIT,
        WAITING,
        MOVING,
        COMPLETED
    }

    public static HumanState currentHumanState;


    private static int shotsLeft;
    private static long lastCheckTime;

    private static Telemetry subsystemTelemetry;

    private static boolean shootStarted;

    public static boolean isHumanIntake;

    public static void init(HardwareMap hardwareMap, Telemetry telemetry) {
        // init subsystems
        SpindexerHelper.init(hardwareMap);
        DistanceSensorHelper.init(hardwareMap);
        ShooterHelper.init(hardwareMap);
        IntakeHelper.init(hardwareMap);
        Drivetrain.init(hardwareMap);
        Light.init(hardwareMap);

        // variables
        artifactCount = 0;
        currentIntakeState = IntakeState.INIT;
        currentShootState = ShootState.INIT;
        currentAutoShootState = AutoShootState.INIT;
        currentHumanState = HumanState.INIT;
        shotsLeft = 0;

        subsystemTelemetry = telemetry;

        isDetected = false;

        delayStarted = false;
        delayTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        shootStarted = false;

        isHumanIntake = false;
    }


    public static void intake(Gamepad gamepad2) {
        if (currentIntakeState == IntakeState.INIT) {
            if (((gamepad2.aWasPressed() && !V1.inhibitButtons) || artifactCount == 0) && currentShootState == ShootState.INIT && !isHumanIntake) {
                // start intake
                currentIntakeState = IntakeState.MOVING_TO_POSITION;
            }
        } else {
            switch (currentIntakeState) {
                case MOVING_TO_POSITION:
                    // start intake motor
                    isDetected = false;
                    intakePosition();
                    IntakeHelper.start();

                    currentIntakeState = IntakeState.WAITING_FOR_BALL;
                    break;

                case WAITING_FOR_BALL:
                    // wait for color sensor to detect ball

                    if (DistanceSensorHelper.isBall() && !isDetected && artifactCount < 3) {
                        isDetected = true;
                        SpindexerHelper.moveToNextPosition();

                        artifactCount++;

                        if (artifactCount >= 3) {
                            currentIntakeState = IntakeState.COMPLETED;
                        } else {
                            currentIntakeState = IntakeState.MOVING_TO_NEXT_POSITION;
                        }
                    }
                    break;

                case MOVING_TO_NEXT_POSITION:
                    if (!SpindexerHelper.SpindexerMotor.isBusy()) {
                        currentIntakeState = IntakeState.WAITING_FOR_BALL;
                    }

                    isDetected = false;
                    break;

                case COMPLETED:
                    // stop intake motor
                    IntakeHelper.stop();
                    SpindexerHelper.shootPosition();
                    currentIntakeState = IntakeState.INIT;
                    break;
            }
        }
    }

    public static boolean intakeAuto() {
        switch (currentAutoIntakeState) {
            case INIT:
                currentAutoIntakeState = AutoIntakeState.MOVING_TO_POSITION;
                break;
            case MOVING_TO_POSITION:
                // start intake motor
                subsystemTelemetry.addLine("moving to position");
                subsystemTelemetry.update();
                isDetected = false;
                intakePosition();
                IntakeHelper.start();

                delayStarted = false;

                currentAutoIntakeState = AutoIntakeState.WAITING_FOR_BALL;
                break;

            case WAITING_FOR_BALL:
                // wait for color sensor to detect ball
                subsystemTelemetry.addData("artiffact counet", artifactCount);
                subsystemTelemetry.update();
                if (DistanceSensorHelper.isBall() && !isDetected && artifactCount < 3) {
                    isDetected = true;
                    SpindexerHelper.moveToNextPosition();
                    artifactCount++;
                    if (artifactCount >= 3) {
                        currentAutoIntakeState = AutoIntakeState.COMPLETED;
                    } else {
                        currentAutoIntakeState = AutoIntakeState.MOVING_TO_NEXT_POSITION;
                    }
                }
                break;

            case MOVING_TO_NEXT_POSITION:
                if (!SpindexerHelper.SpindexerMotor.isBusy()) {
                    currentAutoIntakeState = AutoIntakeState.WAITING_FOR_BALL;
                }

                delayStarted = false;
                isDetected = false;
                break;

            case COMPLETED:
                // stop intake motor
                IntakeHelper.stop();
                SpindexerHelper.shootPosition();
                return false;
        }
        return true;
    }

    public static void intakeHuman(Gamepad gamepad2) {
        if (gamepad2.backWasPressed()) {
            if (ShooterHelper.shooterMotor.getVelocity() > 0) {
                ShooterHelper.shooterMotor.setVelocity(0);
                ShooterHelper.shooterMotor.setPower(-0.2);
                //Light.white();
                isHumanIntake = true;
            } else {
                ShooterHelper.shooterMotor.setPower(0);
                ShooterHelper.shooterMotor.setVelocity(Globals.ShooterVelocity);
                isHumanIntake = false;
            }
        }
    }

    public static void shoot(Gamepad gamepad2) {
        if (!isHumanIntake) {
            ShooterHelper.shooterMotor.setVelocity(Globals.ShooterVelocity);
        }
        if (currentShootState == ShootState.INIT) {
            if (gamepad2.xWasPressed()) {
                // start shooter
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            } else if (gamepad2.yWasPressed()) {
                // forced shoot
                artifactCount = Globals.ForcedArtifacts;
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            }
        } else if (currentShootState == ShootState.COMPLETED) {
            if (gamepad2.xWasPressed()) {
                // start shooter
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            } else if (gamepad2.yWasPressed()) {
                // forced shoot
                artifactCount = Globals.ForcedArtifacts;
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            }
            currentShootState = ShootState.INIT;
        } else if (artifactCount > 0) {
            if (gamepad2.xWasPressed()) {
                // start shooter
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            } else if (gamepad2.yWasPressed()) {
                // forced shoot
                artifactCount = Globals.ForcedArtifacts;
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            }
            switch (currentShootState) {
                case MOVING_TO_SHOOT_POSITION:
                    // start shooter motor
                    shootPosition();
                    shotsLeft = artifactCount;
                    initialArtifactCount = artifactCount;
                    currentShootState = ShootState.SPINNING_UP_SHOOTER;
                    delayStarted = false;
                    break;

                case SPINNING_UP_SHOOTER:
                    // wait until shooter is at target velocity
                    double targetVelocity = Globals.ShooterVelocity;

                    ShooterHelper.shoot(targetVelocity);

                    if (Math.abs(ShooterHelper.shooterMotor.getVelocity() - targetVelocity) <= Globals.ShooterTolerance) {
                        delayStarted = false;
                        currentShootState = ShootState.FIRING;
                    }

                    break;

                case FIRING:
                    // shoot one artifact
                    SpindexerHelper.moveServo(1);
                    if (!delayStarted) {
                        delayTimer.reset();
                        delayStarted = true;
                    }
                    // sleep for 500 ms
                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 500) {
                        SpindexerHelper.moveServo(Globals.servoDown);
                        delayStarted = false;
                        spindexerMoved = false;
                        currentShootState = ShootState.ADVANCING_NEXT_BALL;
                    }
                    break;

                case ADVANCING_NEXT_BALL:
                    // move spindexer and prep for next ball
                    if (!spindexerMoved) {
                        if (initialArtifactCount == 2) {
                            SpindexerHelper.SpindexerMotor.setDirection(DcMotor.Direction.REVERSE);
                            SpindexerHelper.moveToNextPosition();
                            SpindexerHelper.SpindexerMotor.setDirection(DcMotor.Direction.FORWARD);
                        } else {
                            SpindexerHelper.moveToNextPosition();
                        }
                        spindexerMoved = true;
                    }

                    int current = SpindexerHelper.SpindexerMotor.getCurrentPosition();
                    int target  = SpindexerMotor.getTargetPosition();
                    int posError = Math.abs(current - target);

                    if (posError < HALF_SLOT_TICKS * 0.1) {
                        artifactCount--;
                        shotsLeft--;
                        if (shotsLeft <= 0) {
                            currentShootState = ShootState.COMPLETED;
                        } else {
                            currentShootState = ShootState.SPINNING_UP_SHOOTER;
                        }
                    }
                    break;

                case COMPLETED:
                    currentShootState = ShootState.INIT;
                    break;
            }
        }
    }

    public static boolean shootAuto(int numArtifacts, int velocity) {
        if (numArtifacts <= 0) {
            currentAutoShootState = AutoShootState.INIT;
            return false;
        }

        switch (currentAutoShootState) {
            case INIT:
                artifactCount = numArtifacts;
                shotsLeft = numArtifacts;
                initialArtifactCount = numArtifacts;
                spindexerMoved = false;
                delayStarted = false;

                currentAutoShootState = AutoShootState.MOVING_TO_SHOOT_POSITION;
                break;

            case MOVING_TO_SHOOT_POSITION:
                shootPosition();
                shotsLeft = artifactCount;
                delayStarted = false;

                currentAutoShootState = AutoShootState.SPINNING_UP_SHOOTER;
                break;

            case SPINNING_UP_SHOOTER:
                ShooterHelper.shoot(velocity);

                if (Math.abs(ShooterHelper.shooterMotor.getVelocity() - (double) velocity) <= Globals.ShooterTolerance) {
                    delayStarted = false;
                    currentAutoShootState = AutoShootState.FIRING;
                }
                break;

            case FIRING:
                SpindexerHelper.moveServo(1);

                if (!delayStarted) {
                    delayTimer.reset();
                    delayStarted = true;
                }

                if (delayTimer.time(TimeUnit.MILLISECONDS) > 500) {
                    SpindexerHelper.moveServo(Globals.servoDown);
                    delayStarted = false;
                    spindexerMoved = false;
                    currentAutoShootState = AutoShootState.ADVANCING_NEXT_BALL;
                }
                break;

            case ADVANCING_NEXT_BALL:
                if (!spindexerMoved) {
                    if (initialArtifactCount == 2) {
                        SpindexerHelper.SpindexerMotor.setDirection(DcMotor.Direction.REVERSE);
                        SpindexerHelper.moveToNextPosition();
                        SpindexerHelper.SpindexerMotor.setDirection(DcMotor.Direction.FORWARD);
                    } else {
                        SpindexerHelper.moveToNextPosition();
                    }
                    spindexerMoved = true;
                }

                int current = SpindexerHelper.SpindexerMotor.getCurrentPosition();
                int target  = SpindexerMotor.getTargetPosition();
                int posError = Math.abs(current - target);

                if (posError < HALF_SLOT_TICKS * 0.1) {
                    artifactCount--;
                    shotsLeft--;

                    if (shotsLeft <= 0) {
                        currentAutoShootState = AutoShootState.COMPLETED;
                    } else {
                        currentAutoShootState = AutoShootState.SPINNING_UP_SHOOTER;
                    }
                }
                break;

            case COMPLETED:
                SpindexerHelper.intakePosition();
                return false;
        }

        return true;
    }



    public static void drivetrain(Gamepad gamepad1) {
        // speed control
        if (gamepad1.left_bumper) {
            currentSpeed = Globals.SlowDriveSpeed;
        } else {
            currentSpeed = Globals.DriveSpeed;
        }

        // reset imu
        if (gamepad1.startWasPressed()) {
            Drivetrain.resetIMU();
        }

        // set variables for driving
        double y = 0;
        double x = 0;
        double rx = 0;
        if (!Globals.isRightStick) {
            y = easeInOutSine(-gamepad1.left_stick_y);
            x = easeInOutSine(gamepad1.left_stick_x);
            rx = easeInOutSine(gamepad1.right_stick_x);
        } else {
            y = easeInOutSine (-gamepad1.right_stick_y);
            x = easeInOutSine(gamepad1.right_stick_x);
            rx = easeInOutSine(gamepad1.left_stick_x);
        }

        // calculate field centric variables
        if (Globals.isFieldCentric) {
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