package org.firstinspires.ftc.teamcode.mechanisms.subsystems;

import static org.firstinspires.ftc.teamcode.Helpers.easeInOutSine;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper.intakePosition;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper.shootPosition;
import static org.firstinspires.ftc.teamcode.teleop.V1.currentSpeed;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.auto.AutoTest;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.intake.IntakeHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.options.Globals;
import org.firstinspires.ftc.teamcode.teleop.V1;

import java.util.concurrent.TimeUnit;

public class Subsystems {
    public final static int HALF_SLOT_TICKS = SpindexerHelper.TPR;

    public static double SHOOTER_VELOCITY = 1100;
    public static double SUBTRACTION_VELOCITY = 50;

    public static ElapsedTime delayTimer;
    public static boolean delayStarted;
    public static boolean spindexerMoved;

    // There are 6 positions on the spindexer each represented as below
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

    private static boolean shootStarted;

    public static void init(HardwareMap hardwareMap, Telemetry telemetry) {
        // init subsystems
        SpindexerHelper.init(hardwareMap);
        ColorSensorHelper.init(hardwareMap);
        ShooterHelper.init(hardwareMap);
        IntakeHelper.init(hardwareMap);
        Drivetrain.init(hardwareMap);

        // variables
        artifactCount = 0;
        currentState = IntakeState.INIT;
        currentShootState = ShootState.INIT;
        shotsLeft = 0;

        subsystemTelemetry = telemetry;

        isDetected = false;

        delayStarted = false;
        delayTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        shootStarted = false;
    }


    public static void intake(Gamepad gamepad2) {
        if (currentState == IntakeState.INIT) {
            if (gamepad2.aWasPressed() && !gamepad2.start) {
                // start intake
                currentState = IntakeState.MOVING_TO_POSITION;
            }
        } else {
            if (gamepad2.aWasPressed() && !gamepad2.start) {
                currentState = IntakeState.MOVING_TO_POSITION;
            }
            switch (currentState) {
                case MOVING_TO_POSITION:
                    // start intake motor
                    isDetected = false;
                    intakePosition();
                    IntakeHelper.start();

                    delayStarted = false;

                    currentState = IntakeState.WAITING_FOR_BALL;
                    break;

                case WAITING_FOR_BALL:
                    // wait for color sensor to detect ball

                    if (ColorSensorHelper.isBall() && !isDetected && artifactCount < 3) {
                        isDetected = true;

                        if (!delayStarted) {
                            delayStarted = true;
                            delayTimer.reset();
                        }

                        if (delayTimer.time(TimeUnit.MILLISECONDS) > 250) {
                            SpindexerHelper.moveToNextPosition();

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
                    if (!SpindexerHelper.SpindexerMotor.isBusy()) {
                        currentState = IntakeState.WAITING_FOR_BALL;
                    }

                    isDetected = false;
                    break;

                case COMPLETED:
                    // stop intake motor
                    IntakeHelper.stop();
                    SpindexerHelper.shootPosition();
                    currentState = IntakeState.INIT;
                    break;
            }
        }
    }

    public static void intakeAuto(int ticks) throws InterruptedException {
        boolean finished = false;
        while (!finished) {
            intakePosition();

            IntakeHelper.start();

            Drivetrain.BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Drivetrain.BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Drivetrain.FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Drivetrain.FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            Drivetrain.BL.setTargetPosition(ticks);
            Drivetrain.BR.setTargetPosition(ticks);
            Drivetrain.FL.setTargetPosition(ticks);
            Drivetrain.FR.setTargetPosition(ticks);

            Drivetrain.BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Drivetrain.BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Drivetrain.FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Drivetrain.FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            Drivetrain.BL.setPower(0);
            Drivetrain.BR.setPower(0);
            Drivetrain.FL.setPower(0);
            Drivetrain.FR.setPower(0);

            while (!ColorSensorHelper.isBall()) {
                // wai
                Drivetrain.BL.setPower(0.35);
                Drivetrain.BR.setPower(0.35);
                Drivetrain.FL.setPower(0.35);
                Drivetrain.FR.setPower(0.35);
            }
            sleep(200);

            SpindexerHelper.moveToNextPosition();

            while (Drivetrain.BL.isBusy() && Drivetrain.BR.isBusy() && Drivetrain.FL.isBusy() && Drivetrain.FR.isBusy()) {
                // wait
            }

            Drivetrain.BL.setPower(0);
            Drivetrain.BR.setPower(0);
            Drivetrain.FL.setPower(0);
            Drivetrain.FR.setPower(0);

            finished = true;
        }
    }

    public static void shoot(Gamepad gamepad2) {
        if (currentShootState == ShootState.INIT) {
            if (gamepad2.xWasPressed()) {
                // start shooter
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            } else if (gamepad2.yWasPressed()) {
                // forced shoot
                artifactCount = Globals.ForcedArtifacts;
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            } else if (gamepad2.backWasPressed()) {
                if (ShooterHelper.shooterMotor.getPower() == 0) {
                    ShooterHelper.shooterMotor.setVelocity(0);
                    ShooterHelper.shooterMotor.setPower(-0.2);
                } else {
                    ShooterHelper.shooterMotor.setPower(0);
                }
            }
        } else if (currentShootState == ShootState.COMPLETED) {
            currentShootState = ShootState.INIT;
        } else if (artifactCount > 0) {
            if (gamepad2.xWasPressed()) {
                // start shooter
                currentShootState = ShootState.MOVING_TO_SHOOT_POSITION;
            }
            switch (currentShootState) {
                case MOVING_TO_SHOOT_POSITION:
                    // start shooter motor
                    shootPosition();
                    shotsLeft = artifactCount;
                    currentShootState = ShootState.SPINNING_UP_SHOOTER;
                    delayStarted = false;
                    break;

                case SPINNING_UP_SHOOTER:
                    // wait until shooter is at target velocity
                    double targetVelocity = SHOOTER_VELOCITY;

                    ShooterHelper.shoot(targetVelocity);

                    if(!delayStarted) {
                        delayTimer.reset();
                        delayStarted = true;
                    }

                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 100) {
                        delayTimer.reset();
                        if (Math.abs(ShooterHelper.shooterMotor.getVelocity() - targetVelocity) <= 15) {
                            delayStarted = false;
                            currentShootState = ShootState.FIRING;
                        }
                    }
                    break;

                case FIRING:
                    // shoot one artifact
                    SpindexerHelper.moveServo(1);
                    if (!delayStarted) {
                        delayTimer.reset();
                        delayStarted = true;
                    }
                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 500) {
                        SpindexerHelper.moveServo(0.5);
                        delayStarted = false;
                        spindexerMoved = false;
                        currentShootState = ShootState.ADVANCING_NEXT_BALL;
                    }
                    break;

                case ADVANCING_NEXT_BALL:
                    // move spindexer and prep for next ball
                    if (!spindexerMoved) {
                        SpindexerHelper.moveToNextPosition();
                        spindexerMoved = true;
                    }

                    if (!delayStarted) {
                        delayTimer.reset();
                        delayStarted = true;
                    }

                    if (delayTimer.time(TimeUnit.MILLISECONDS) > 100) {
                        delayTimer.reset();
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
                    currentShootState = ShootState.INIT;
                    break;
            }
        }
    }

    public static void shootAuto(int numArtifacts) throws InterruptedException {
        int artifacts = numArtifacts;

        while (artifacts > 0) {
            shootPosition();
            double targetVelocity = SHOOTER_VELOCITY;

            ShooterHelper.shoot(targetVelocity);

            while (Math.abs(ShooterHelper.shooterMotor.getVelocity() - targetVelocity) >= 5) {
                // wait
            }
            sleep(500);
            // shoot one artifact
            SpindexerHelper.moveServo(1);
//            subsystemTelemetry.addLine("servo up");
//            subsystemTelemetry.update();

            sleep(500);

            SpindexerHelper.moveServo(0.5);

            sleep(200);

            // move spindexer and prep for next ball
            SpindexerHelper.moveToNextPosition();

            while (SpindexerHelper.SpindexerMotor.isBusy()) {
                    // empty :3
            }
            artifacts--;
        }
    }


    public static void drivetrain(Gamepad gamepad1) {
        // speed control
        if(gamepad1.left_bumper) {
            currentSpeed = Globals.SlowDriveSpeed;
        } else {
            currentSpeed = Globals.DriveSpeed;
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