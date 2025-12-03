package org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer;

import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.HALF_SLOT_TICKS;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.intakePositions;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.shootPositions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.helpers.Globals;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class SpindexerHelper {
    public static DcMotor SpindexerMotor;
    public static Servo SpindexerServo;

    // TPR = ticks per rotation, SINGLE = ticks for a single position, SLOTS = three different positions

    public static final int TPR = 145; // for new spindexer, 1150 rpm motor
    private static final int SLOTS = 3;
    private static final int SINGLE = HALF_SLOT_TICKS * 2;
    private static String[] colors = new String[SLOTS];

    public static void init(HardwareMap hardwareMap) {
        // SPEED = Globals.SpindexerSpeed;
        if (Configuration.spindexerMotor.itemActive) {
            SpindexerMotor = hardwareMap.get(DcMotor.class, Configuration.spindexerMotor.itemName);
        }
        if (Configuration.spindexerServo.itemActive) {
            SpindexerServo = hardwareMap.get(Servo.class, Configuration.spindexerServo.itemName);
        }

        SpindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpindexerMotor.setTargetPosition(0);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        SpindexerMotor.setPower(Globals.SpindexerSpeed);
        SpindexerMotor.setDirection(DcMotor.Direction.FORWARD);

        Arrays.fill(colors, "-");
    }

    public static int findPosition() {
        int ticks = SpindexerMotor.getCurrentPosition();
        return (ticks / HALF_SLOT_TICKS) % 6;
    }

    public static void moveToNextPosition() {
        int current = SpindexerMotor.getTargetPosition();
        int target = current + SINGLE;
        SpindexerMotor.setTargetPosition(target);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(Globals.SpindexerSpeed);
    }

    public static void moveHalfPosition(boolean forward) {
        int current = SpindexerMotor.getTargetPosition();
        int target = (forward) ? current + HALF_SLOT_TICKS : current - HALF_SLOT_TICKS;
        SpindexerMotor.setTargetPosition(target);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(Globals.SpindexerSpeed);
    }

    public static void moveServo(double pos) {
        if (SpindexerServo == null) return;
        SpindexerServo.setPosition(pos);
    }

    public static void moveSpindexerTo(int targetPosition){
        SpindexerMotor.setTargetPosition(targetPosition);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(Globals.SpindexerSpeed);
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
}
