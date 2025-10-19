package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class SpindexerHelper {
    public static DcMotor SpindexerMotor;

    public  static Servo SpindexerServo;

    public static void init(HardwareMap hardwareMap) {
        SpindexerMotor = hardwareMap.get(DcMotor.class, "spindexer motor");
        SpindexerServo = hardwareMap.get(Servo.class, "spindexer servo");
        SpindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpindexerMotor.setTargetPosition(0);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }

//    public static int findPosition(int currentEncoderPosition, int ticksPerRevolution) {
//        int singlePosition = ticksPerRevolution/3;
//        return (currentEncoderPosition / singlePosition) % 3;
//    }

    public static int findPosition() {
        int singlePosition = 288/3;
        return (SpindexerMotor.getCurrentPosition() / singlePosition) % 3;
    }

    public static void moveServo() {
        // placeholder servo code
        SpindexerServo.setPosition(50);
    };
//    public static int moveToNextPosition(int ticksPerRevolution) {
//        int singlePosition = ticksPerRevolution/3;
//        int movePosition = singlePosition + SpindexerMotor.getCurrentPosition();
//        SpindexerMotor.setTargetPosition(movePosition);
//        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        SpindexerMotor.setPower(0.5);
//        return movePosition;
//    }

    public static int moveToNextPosition() {
        int singlePosition = 288 / 3;
        int movePosition = singlePosition + SpindexerMotor.getCurrentPosition();
        SpindexerMotor.setTargetPosition(movePosition);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
        return movePosition;
    }

    public static void moveToPosition(int index) {
        int movePosition = index - findPosition();
        SpindexerMotor.setTargetPosition(movePosition);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }
}
