package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import static org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper.SpindexerMotor;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.distanceSensor.DistanceSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.shooter.ShooterHelper;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.DoubleStream;

public class Spindexer {
    //based on spindexer motor ticks per rotation
    private final static int HALF_SLOT_TICKS = 48;
    private final static int SLOT_TICKS = 96;
    private static long loopIndex = 0L;

    private final static double SHOOTER_VELOCITY = 1000;

    static Telemetry ttelemetry;
    //There are 6 positions on the spindexer each represented as below
    //              3
    //      2       *       4
    //          *   *   *
    //              *
    //          *   *   *
    //      1       *       5
    //              0

    private static final double[] intakePositions = new double[]{1.0,3.0,5.0};
    private final double[] shootPositions = new double[]{0.0,2.0,4.0};
    public static String[] colors;
    public static String[] motif;

    private static int intakeArtifactCount;

    public static void init(HardwareMap hardwareMap, Telemetry telemetry) {
        SpindexerHelper.init(hardwareMap, telemetry);
        DistanceSensorHelper.init(hardwareMap);
        ColorSensorHelper.init(hardwareMap);
        ShooterHelper.init(hardwareMap);
        ttelemetry = telemetry;
    }

    public static void moveSpindexer(int ticks) {
        int targetTicks = SpindexerMotor.getCurrentPosition() + ticks;
        moveSpindexerTo(targetTicks);
        ttelemetry.addData("current ticks", SpindexerMotor.getCurrentPosition());
        ttelemetry.addData("target ticks", SpindexerMotor.getTargetPosition());

    }

    public static void moveSpindexerTo(int targetPosition){
        SpindexerMotor.setTargetPosition(targetPosition);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }

    //This method moves the spindexer to the intake position no matter in which position the
    //spindexer is in
    public static void intakePosition() {
        int currPositionTicks = SpindexerMotor.getCurrentPosition();
        int targetPosition;
        //Check if the current position is already Intake position
        double currPos = findExactPosition(currPositionTicks);
        ttelemetry.addLine("Moving to intake position....Before");
        ttelemetry.addData("Loop Index..", loopIndex);
        ttelemetry.addData("Current position Ticks:", currPositionTicks);
        ttelemetry.addData("Current position:", currPos);
        if (DoubleStream.of(intakePositions).anyMatch(n -> n == currPos)) {
            targetPosition = currPositionTicks;
        } else {
            int nextHalfSlotTicks = (currPositionTicks + HALF_SLOT_TICKS - currPositionTicks % HALF_SLOT_TICKS);
            double nextPos = (double)((double)nextHalfSlotTicks / HALF_SLOT_TICKS) % 6;
            ttelemetry.addData("Next Half slot ticks", nextHalfSlotTicks);
            ttelemetry.addData("Next Pos:", nextPos);
            //Check if the next position is one of the Intake positions - 1, 3, 5
            if (DoubleStream.of(intakePositions).anyMatch(n -> n == nextPos)) {
                targetPosition = nextHalfSlotTicks;
            } else {
                targetPosition = nextHalfSlotTicks + HALF_SLOT_TICKS;
            }
        }
        ttelemetry.addData("Target Intake position:", targetPosition);

        moveSpindexerTo(targetPosition);
        ttelemetry.addData("Current position after intake motor moved:", SpindexerMotor.getTargetPosition());
        //telemetry.update();
    }

    public static double findExactPosition(int ticks) {
        return (double)(ticks/HALF_SLOT_TICKS) % 6;
    }

    /**
     * This method is used to intake 3 artifacts and read the colors into the colors array
     */
    public static void intake() throws InterruptedException {
        colors = new String[3];
        intakePosition();
        for (int i=0;i<3;i++) {
            intakeArtifact();
            Thread.sleep(2000);
//            while(Objects.equals(ColorSensorHelper.getColor(), "Neither")) {
//                ttelemetry.addData("colorInWhile", ColorSensorHelper.getColor());
//                ttelemetry.update();
//            }
            boolean detected_color = false;
            String colorRead = "Neither";
            while(!detected_color) {
                colorRead = ColorSensorHelper.getColor();
                if (!Objects.equals(colorRead, "Neither")) {
                    detected_color = true;
                }
            }
            colors[i] = colorRead;
            //colors[i] = ColorSensorHelper.getColor();
            ttelemetry.addData("Color: ",colors[i]);
            ttelemetry.update();
            moveSpindexer(HALF_SLOT_TICKS);
            ttelemetry.addLine("thingy");
            Thread.sleep(2000);
            ttelemetry.addData("current ticks1", SpindexerMotor.getCurrentPosition());
            ttelemetry.addData("target ticks1", SpindexerMotor.getTargetPosition());
            ttelemetry.update();
//            while(Math.abs(SpindexerMotor.getCurrentPosition() - SpindexerMotor.getTargetPosition()) > 20) {
//                telemetry.addData("distanccee", DistanceSensorHelper.distance.getDistance(DistanceUnit.MM));
//                telemetry.update();
//            }
        }
        ttelemetry.addData("colors:", Arrays.toString(colors));
        ttelemetry.update();
    }

    public static void intakeArtifact(){
        ttelemetry.addData("Intake Artifact Count before intake:", intakeArtifactCount);
        while(!DistanceSensorHelper.isBall()){
            ttelemetry.addLine("Waiting for ball intake...");
            //telemetry.update();
        }
        if(DistanceSensorHelper.isBall()) {
            //Move to color sensor position
            moveSpindexer(HALF_SLOT_TICKS);
            intakeArtifactCount++;
        }
        ttelemetry.addData("Intake Artifact Count:", intakeArtifactCount);
    }

    public static void motorTelemetry(){
        ttelemetry.addData("Loop Index ----------------------", loopIndex);
        ttelemetry.addData("Target Spindexer Position", SpindexerMotor.getTargetPosition());
        ttelemetry.addData("Slot Position: ", findExactPosition(SpindexerMotor.getCurrentPosition()));
        ttelemetry.addData("Current Spindexer Position: ", SpindexerMotor.getCurrentPosition());
        ttelemetry.update();
    }

    public static void shoot() throws InterruptedException {
        SpindexerHelper.moveToPosition(calculateShooter(colors, motif));
        for(int i = 0; i < 3; i++) {
            ShooterHelper.shoot(Spindexertestest.SHOOTER_VELOCITY);
            while(Math.abs(ShooterHelper.shooterMotor.getVelocity() - Spindexertestest.SHOOTER_VELOCITY) > 2) {
//                ttelemetry.addData("shooter velocity", ShooterHelper.shooterMotor.getVelocity());
//                ttelemetry.update();
            }

            Thread.sleep(750);
            SpindexerHelper.moveServo(1);
            Thread.sleep(750);
            SpindexerHelper.moveServo(0.5);
            Thread.sleep(750);
            SpindexerHelper.moveToNextPosition();
        }
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