package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;

import java.util.Arrays;

public class SpindexerHelper {
    public static DcMotor SpindexerMotor;
    public static Servo SpindexerServo;
//hello
    //TPR = ticks per rotation, SINGLE = ticks for a single position, SLOTS = three different positions
    private static final int TPR = 288;
    private static final int SLOTS = 3;
    private static final int SINGLE = TPR / SLOTS;
    private static final int INTAKE_OFFSET = 48;
    private static final int SHOOTING_OFFSET = 0;
    private static final int COLOR_SENSOR_OFFSET = 80;
    private static String[] colors = new String[SLOTS];

    public static void init(HardwareMap hardwareMap) {
        SpindexerMotor = hardwareMap.get(DcMotor.class, "spindexer");
        SpindexerServo = hardwareMap.get(Servo.class, "spindexer servo");

        SpindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpindexerMotor.setTargetPosition(0);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        SpindexerMotor.setPower(0.5);

        Arrays.fill(colors, "-");
    }

    public static int getStateOffset() {
        return (Spindexer.state == Spindexer.State.INTAKE) ? INTAKE_OFFSET : (Spindexer.state == Spindexer.State.SHOOTING) ? SHOOTING_OFFSET : (Spindexer.state == Spindexer.State.COLORSENSOR) ? COLOR_SENSOR_OFFSET : SHOOTING_OFFSET;
    }


    public static int findPosition() {
        int ticks = SpindexerMotor.getCurrentPosition() - getStateOffset();
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
        if (index < 0) return;

        int currIdx = findPosition();
        int deltaSlots = Math.floorMod(index - currIdx, SLOTS);
        int currentTicks = SpindexerMotor.getCurrentPosition();
        int target = currentTicks + deltaSlots * SINGLE;
        if(target % 96 != 48) {
            target = currentTicks + deltaSlots * SINGLE + getStateOffset();
        }

        SpindexerMotor.setTargetPosition(target);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }

    public static void moveServo(double pos01) {
        if (SpindexerServo == null) return;
        SpindexerServo.setPosition(Math.max(0.0, Math.min(1.0, pos01)));
    }

    public static String[] getColors() {
        Spindexer.state = Spindexer.State.INTAKE;

        for (int i = 0; i < 3; i++) {
            moveToPosition(i);
            if(SpindexerMotor.isBusy()) {
                // nothing
            }
            colors[i] = ColorSensorHelper.getColor();
        }

        String[] result = colors.clone();

        Arrays.fill(colors, "-");

        return result;
    }
}
