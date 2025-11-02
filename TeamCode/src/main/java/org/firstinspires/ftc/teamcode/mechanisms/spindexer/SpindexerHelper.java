package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.deprecated.SpindexerOld;

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
    private static final int COLOR_SENSOR_OFFSET = 0;
    private static String[] colors = new String[SLOTS];
    private static Telemetry spindexerTelemetry;

    public static void init(HardwareMap hardwareMap, Telemetry telemetry) {
        SpindexerMotor = hardwareMap.get(DcMotor.class, Config.spindexerMotor);
        SpindexerServo = hardwareMap.get(Servo.class, Config.spindexerServo);

        SpindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpindexerMotor.setTargetPosition(0);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        SpindexerMotor.setPower(0.5);
        SpindexerMotor.setDirection(DcMotor.Direction.REVERSE);

        Arrays.fill(colors, "-");

        spindexerTelemetry = telemetry;
    }

    public static int getStateOffset() {
        if(SpindexerOld.state == SpindexerOld.State.SHOOTING) {
            return SHOOTING_OFFSET;
        } else if(SpindexerOld.state == SpindexerOld.State.INTAKE) {
            return INTAKE_OFFSET;
        } else if(SpindexerOld.state == SpindexerOld.State.COLORSENSOR) {
            return COLOR_SENSOR_OFFSET;
        }
        return 0;
    }


    public static int findPosition() {
        int ticks = SpindexerMotor.getCurrentPosition() - getStateOffset();
        return (ticks / SINGLE) % 3;
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
        spindexerTelemetry.addData("deltaSlots", deltaSlots);
        spindexerTelemetry.update();

        int currentTicks = SpindexerMotor.getCurrentPosition();
        int target = currentTicks + deltaSlots * SINGLE;

        SpindexerMotor.setTargetPosition(target);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }

    public static void moveServo(double pos) {
        if (SpindexerServo == null) return;
        SpindexerServo.setPosition(pos);
    }
}
