package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper;

import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper.SpindexerMotor;

@TeleOp(name = "Spindexer Test", group = "Spindexer")
public class SpindexerTest extends OpMode {
    @Override
    public void init() {
        SpindexerHelper.init(hardwareMap);
    }
    public void loop() {

        String[] motif1 = null;
        String[] motif2 = null;
        String[] motif3 = null;
        String[] motif4 = null;
        if (gamepad1.a) {
            motif1 = Spindexer2.shootMotifBall(0);
        } else if (gamepad1.b) {
            motif3 = Spindexer.shootMotifBall(0);
        }

        telemetry.addData("Target Position", SpindexerMotor.getTargetPosition());
        telemetry.addData("Position: ", SpindexerHelper.findPosition());
        telemetry.addData("Current Position: ", SpindexerMotor.getCurrentPosition());
        telemetry.addData("stuff", Arrays.toString(motif1));
        telemetry.addData("stufffe", Arrays.toString(motif3));

        telemetry.update();
    }
}
