package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp
public class SpindexerTest extends OpMode {

    public DcMotor SpindexerMotor;
    public Gamepad currentGamepad1;
    public Gamepad previousGamepad1;
    @Override
    public void init() {
        SpindexerMotor = hardwareMap.get(DcMotor.class, "spindexer motor");
        SpindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpindexerMotor.setTargetPosition(0);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();

    }
    public void loop() {

        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        if (currentGamepad1.a && !previousGamepad1.a) {
            moveToPosition(537);
        }

        telemetry.addData("Target Position", SpindexerMotor.getTargetPosition());
        telemetry.addData("Position: ", findPosition(SpindexerMotor.getCurrentPosition(), 537));
        telemetry.addData("Current Position: ", SpindexerMotor.getCurrentPosition());
        telemetry.update();
    }

    public int findPosition(int currentEncoderPosition, int ticksPerRevolution) {
        int singlePosition = ticksPerRevolution/3;
        int finalPosition = (currentEncoderPosition / singlePosition) % 3;
        return finalPosition;
    }
    public int moveToPosition(int ticksPerRevolution) {
        int singlePosition = ticksPerRevolution/3;
        int movePosition = singlePosition + SpindexerMotor.getCurrentPosition();
        SpindexerMotor.setTargetPosition(movePosition);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
        return movePosition;
    }
}
