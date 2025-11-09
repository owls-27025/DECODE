package org.firstinspires.ftc.teamcode.mechanisms.subsystems;

import static org.firstinspires.ftc.teamcode.Helpers.easeInOutSine;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper.SpindexerMotor;
import static org.firstinspires.ftc.teamcode.teleop.V1.currentSpeed;
import static org.firstinspires.ftc.teamcode.teleop.V1.isIntaking;
import static org.firstinspires.ftc.teamcode.teleop.V1.isShooting;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.intake.IntakeHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.teleop.V1;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class Subsystems {
    private final static int HALF_SLOT_TICKS = 48;

    public static double SHOOTER_VELOCITY = 1000;
    public static double SUBTRACTION_VELOCITY = 50;

    //There are 6 positions on the spindexer each represented as below
    //              3
    //      2       *       4
    //          *   *   *
    //              *
    //          *   *   *
    //      1       *       5
    //              0

    private static final double[] intakePositions = new double[]{1.0,3.0,5.0};
    private static final double[] shootPositions = new double[]{0.0,2.0,4.0};
    public static String[] colors;
    public static int[] hues;
    public static String[] motif;

    public static int artifactCount;
    private static Telemetry subSysTelemetry;

    public static void init(HardwareMap hardwareMap, Telemetry telemetry) {
        // init subsystems
        SpindexerHelper.init(hardwareMap);
        ColorSensorHelper.init(hardwareMap);
        ShooterHelper.init(hardwareMap);
        IntakeHelper.init(hardwareMap);
        Drivetrain.init(hardwareMap);

        // set up telemetry
        subSysTelemetry = telemetry;

        // variables
        artifactCount = 3;
    }

    public static void moveSpindexer(int ticks) {
        int targetTicks = SpindexerMotor.getCurrentPosition() + ticks;
        moveSpindexerTo(targetTicks);

    }

    public static void moveSpindexerTo(int targetPosition){
        SpindexerMotor.setTargetPosition(targetPosition);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }

    //This method moves the spindexer to the intake position no matter in which position the spindexer is in
    public static void intakePosition() {
        int currPositionTicks = SpindexerMotor.getCurrentPosition();
        int targetPosition;

        //Check if the current position is already Intake position

        double currPos = findExactPosition(currPositionTicks);
        if (DoubleStream.of(intakePositions).anyMatch(n -> n == currPos)) {
            targetPosition = currPositionTicks;
        } else {
            int nextHalfSlotTicks = (currPositionTicks + HALF_SLOT_TICKS - currPositionTicks % HALF_SLOT_TICKS);
            double nextPos = (double)((double)nextHalfSlotTicks / HALF_SLOT_TICKS) % 6;

            //Check if the next position is one of the Intake positions - 1, 3, 5

            if (DoubleStream.of(intakePositions).anyMatch(n -> n == nextPos)) {
                targetPosition = nextHalfSlotTicks;
            } else {
                targetPosition = nextHalfSlotTicks + HALF_SLOT_TICKS;
            }
        }

        moveSpindexerTo(targetPosition);
    }

    public static void shootPosition() {
        int currPositionTicks = SpindexerMotor.getCurrentPosition();
        int targetPosition;

        //Check if the current position is already shooting position

        double currPos = findExactPosition(currPositionTicks);
        if (DoubleStream.of(shootPositions).anyMatch(n -> n == currPos)) {
            targetPosition = currPositionTicks;
        } else {
            int nextHalfSlotTicks = (currPositionTicks + HALF_SLOT_TICKS - currPositionTicks % HALF_SLOT_TICKS);
            double nextPos = (double)((double)nextHalfSlotTicks / HALF_SLOT_TICKS) % 6;

            //Check if the next position is one of the shooting positions - 0, 2, 4

            if (DoubleStream.of(shootPositions).anyMatch(n -> n == nextPos)) {
                targetPosition = nextHalfSlotTicks;
            } else {
                targetPosition = nextHalfSlotTicks + HALF_SLOT_TICKS;
            }
        }

        moveSpindexerTo(targetPosition);
    }

    public static double findExactPosition(int ticks) {
        return (double)(ticks/HALF_SLOT_TICKS) % 6;
    }

    public static void intake() throws InterruptedException {
        if (!isIntaking) {
            // move to nearest intake position and start motor
            intakePosition();
            IntakeHelper.start();

            for (int i = 0; i < 3; i++) {
                while (!ColorSensorHelper.isBall()) {
                    // waits for ball to be on ramp
                }

                // wait for ball to move into intake
                Thread.sleep(100);

                // move to next position
                SpindexerHelper.moveToNextPosition();

                artifactCount++;
            }

            IntakeHelper.stop();
        }
        isIntaking = false;
    }

    /**
     * This method is used to intake 3 artifacts and read the colors into the colors array
     */


    public static void shoot() throws InterruptedException {
        if (!isShooting) {
            shootPosition();

            int oldArtifactCount = artifactCount;

            for (int i = 0; i < oldArtifactCount; i++) {
                if (i == 0) {
                    ShooterHelper.shoot(SHOOTER_VELOCITY - SUBTRACTION_VELOCITY);
                } else if (i == 1) {
                    ShooterHelper.shoot(SHOOTER_VELOCITY - (SUBTRACTION_VELOCITY / 2));
                } else {
                    ShooterHelper.shoot(SHOOTER_VELOCITY);
                }
                while (Math.abs(ShooterHelper.shooterMotor.getVelocity() - SHOOTER_VELOCITY) > 5) ;

                Thread.sleep(750);
                SpindexerHelper.moveServo(1);
                Thread.sleep(750);
                SpindexerHelper.moveServo(0.5);
                SpindexerHelper.moveToNextPosition();

                while (SpindexerHelper.SpindexerMotor.isBusy()) ;

                artifactCount--;
            }
        }
        isShooting = false;
    }

    // motifs
    static String[] m1 = {"Purple", "Purple", "Green"};
    static String[] m2 = {"Purple", "Green", "Purple"};
    static String[] m3 = {"Green", "Purple", "Purple"};

    // color combinations
    static String[] i1 = {"Purple", "Purple", "Green"};
    static String[] i2 = {"Purple", "Green", "Purple"};
    static String[] i3 = {"Green", "Purple", "Purple"};
    static String[] i4 = {"Purple", "Purple", "Purple"};
    static String[] i5 = {"Green", "Green", "Purple"};
    static String[] i6 = {"Green", "Green", "Green"};
    static String[] i7 = {"Green", "Purple", "Green"};
    static String[] i8 = {"Purple", "Green", "Green"};

    // group them into arrays for lookup
    static String[][] motifs = {m1, m2, m3};
    static String[][] colorCombos = {i1, i2, i3, i4, i5, i6, i7, i8};

    public static int calculateShooter(String[] colors, String[] motif) {
        if(colors == null || colors.length < 3) {
            return 0;
        } else {
            int motifIndex = -1;
            int colorIndex = -1;

            // find which motif matches
            for (int i = 0; i < motifs.length; i++) {
                if (Arrays.equals(motifs[i], motif)) {
                    motifIndex = i;
                    break;
                }
            }

            // find which color combo matches
            for (int i = 0; i < colorCombos.length; i++) {
                if (Arrays.equals(colorCombos[i], colors)) {
                    colorIndex = i;
                    break;
                }
            }

            // offsets
            int[][] offsets = {
                    {0, 2, 1}, // m1
                    {1, 0, 2}, // m2
                    {2, 1, 0}  // m3
            };

            // if it works use offset, otherwise 0
            if (colorIndex < 3) {
                return offsets[motifIndex][colorIndex];
            } else {
                return 0;
            }
        }
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
        double flPower = ((y + x + rx) * currentSpeed);
        double blPower = ((y - x + rx) * currentSpeed);
        double frPower = ((y - x - rx) * currentSpeed);
        double brPower = ((y + x - rx) * currentSpeed);
        subSysTelemetry.addData("y", y);
        subSysTelemetry.addData("x", x);
        subSysTelemetry.addData("rx", rx);
        subSysTelemetry.addData("FL Power", flPower);
        subSysTelemetry.addData("FR Power", frPower);
        subSysTelemetry.addData("BL Power", blPower);
        subSysTelemetry.addData("BR Power", brPower);





        // move drivetrain
        Drivetrain.FL.setPower((y + x + rx) * currentSpeed);
        Drivetrain.BL.setPower((y - x + rx) * currentSpeed);
        Drivetrain.FR.setPower((y - x - rx) * currentSpeed);
        Drivetrain.BR.setPower((y + x - rx) * currentSpeed);
    }
}