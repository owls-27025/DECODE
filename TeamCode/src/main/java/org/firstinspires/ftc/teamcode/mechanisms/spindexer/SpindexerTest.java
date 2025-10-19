package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper;
import static org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper.SpindexerMotor;

@TeleOp(name = "Spindexer Test", group = "Spindexer")
public class SpindexerTest extends OpMode {

    public Gamepad currentGamepad1;
    public Gamepad previousGamepad1;
    @Override
    public void init() {
        SpindexerHelper.init(hardwareMap);
    }
    public void loop() {

        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        if (currentGamepad1.a && !previousGamepad1.a) {
            SpindexerHelper.moveToNextPosition();
        }

        telemetry.addData("Target Position", SpindexerMotor.getTargetPosition());
        telemetry.addData("Position: ", SpindexerHelper.findPosition(SpindexerMotor.getCurrentPosition(), 537));
        telemetry.addData("Current Position: ", SpindexerMotor.getCurrentPosition());
        telemetry.update();
    }
}
