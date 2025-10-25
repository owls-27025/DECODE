package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;

import java.util.Arrays;

public class SpindexerHelper {
    public static DcMotor SpindexerMotor;
    public static Servo SpindexerServo;
//TPR = ticks per rotation, SINGLE = ticks for a single position, SLOTS = three different positions
    private static final int TPR = 288;
    private static final int SLOTS = 3;
    private static final int SINGLE = TPR / SLOTS;
    private static final int ARRIVAL_TOL = 10;
    private static final long SETTLE_MS = 500;

    public enum State { IDLE, MOVING, FINISHING, SAMPLING, DONE }
    private static State state = State.IDLE;
    private static String[] colors = new String[SLOTS];
    private static int samplesTaken = 0;
    private static long arrivedAtMs = 0L;

    public static void init(HardwareMap hardwareMap) {
        SpindexerMotor = hardwareMap.get(DcMotor.class, "spindexer");
        SpindexerServo = hardwareMap.get(Servo.class, "spindexer servo");

        SpindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpindexerMotor.setTargetPosition(0);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        SpindexerMotor.setPower(0.5);

        Arrays.fill(colors, "-");
        state = State.IDLE;
        samplesTaken = 0;
        arrivedAtMs = 0L;
    }

    public static int findPosition() {
        int ticks = SpindexerMotor.getCurrentPosition();
        int idx = Math.round(ticks / (float) SINGLE);
        return Math.floorMod(idx, SLOTS);
    }

    public static void moveToNextPosition() {
        int current = SpindexerMotor.getCurrentPosition();
        int target = current + SINGLE;
        SpindexerMotor.setTargetPosition(target);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }

    public static void moveToPosition(int index) {
        int currentTicks = SpindexerMotor.getCurrentPosition();
        int currentIndex = findPosition();
        int deltaSlots = Math.floorMod(index - currentIndex, SLOTS);
        int target = currentTicks + deltaSlots * SINGLE;

        SpindexerMotor.setTargetPosition(target);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }

    public static void moveServo(double pos01) {
        if (SpindexerServo == null) return;
        SpindexerServo.setPosition(Math.max(0.0, Math.min(1.0, pos01)));
    }

    public static String[] getColors() {
        if (state == State.IDLE) {
            for (int i = 0; i < SLOTS; i++) colors[i] = "-";
            samplesTaken = 0;
            state = State.MOVING;
            moveToNextPosition();
        }

        while (state != State.DONE) {
            switch (state) {
                case MOVING: {
                    int err = SpindexerMotor.getTargetPosition() - SpindexerMotor.getCurrentPosition();
                    if (Math.abs(err) <= ARRIVAL_TOL) {
                        arrivedAtMs = System.currentTimeMillis();
                        state = State.FINISHING;
                    }
                    break;
                }
                case FINISHING: {
                    if (System.currentTimeMillis() - arrivedAtMs >= SETTLE_MS) {
                        state = State.SAMPLING;
                    }
                    break;
                }
                case SAMPLING: {
                    int pos = findPosition();
                    String c = ColorSensorHelper.getColor();
                    colors[pos] = (c != null) ? c : "unknown";
                    samplesTaken++;
                    if (samplesTaken >= SLOTS) {
                        state = State.DONE;
                    } else {
                        state = State.MOVING;
                        moveToNextPosition();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        String[] result = colors.clone();

        for (int i = 0; i < SLOTS; i++) colors[i] = "-";
        samplesTaken = 0;
        state = State.IDLE;
        arrivedAtMs = 0L;

        return result;
    }
}
