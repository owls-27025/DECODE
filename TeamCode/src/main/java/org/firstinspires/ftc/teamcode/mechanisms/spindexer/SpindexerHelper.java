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
    private static final int INTAKE_OFFSET = 100; // tbd
    private static final int SHOOTING_OFFSET = 100; // tbd
    private static final int COLOR_SENSOR_OFFSET = 100; // tbd
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

    public static int findPosition() {
        int ticks = 0;
        if(Spindexer.state == Spindexer.State.INTAKE) {
            ticks = SpindexerMotor.getCurrentPosition() + INTAKE_OFFSET;
        } else if(Spindexer.state == Spindexer.State.SHOOTING) {
            ticks = SpindexerMotor.getCurrentPosition() + SHOOTING_OFFSET;
        } else if(Spindexer.state == Spindexer.State.COLORSENSOR) {
            ticks = SpindexerMotor.getCurrentPosition() + COLOR_SENSOR_OFFSET;
        }
        int idx = Math.round(ticks / (float) SINGLE);
        return Math.floorMod(idx, SLOTS);
    }

    public static void moveToNextPosition() {
        int current = SpindexerMotor.getCurrentPosition();
        int target = 0;
        if(Spindexer.state == Spindexer.State.INTAKE) {
            target = current + SINGLE + INTAKE_OFFSET;
        } else if(Spindexer.state == Spindexer.State.SHOOTING) {
            target = current + SINGLE + SHOOTING_OFFSET;
        } else if(Spindexer.state == Spindexer.State.COLORSENSOR) {
            target = current + SINGLE + COLOR_SENSOR_OFFSET;
        }
        SpindexerMotor.setTargetPosition(target);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }

    public static void moveToPosition(int index) {
        int currentTicks = SpindexerMotor.getCurrentPosition();
        int currentIndex = findPosition();
        int deltaSlots = Math.floorMod(index - currentIndex, SLOTS);
        int target = 0;
        if(Spindexer.state == Spindexer.State.INTAKE) {
            target = currentTicks + deltaSlots * SINGLE + INTAKE_OFFSET;
        } else if(Spindexer.state == Spindexer.State.SHOOTING) {
            target = currentTicks + deltaSlots * SINGLE + SHOOTING_OFFSET;
        } else if(Spindexer.state == Spindexer.State.COLORSENSOR) {
            target = currentTicks + deltaSlots * SINGLE + COLOR_SENSOR_OFFSET;
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
        Spindexer.state = Spindexer.State.COLORSENSOR;

        for (int i = 0; i < 3; i++) {
            moveToPosition(i);
            colors[i] = ColorSensorHelper.getColor();
        }

        String[] result = colors.clone();

        for (int i = 0; i < SLOTS; i++) colors[i] = "-";

        return result;
    }
}
