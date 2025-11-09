package org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper.SpindexerMotor;

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

        telemetry.addData("Target Position", SpindexerMotor.getTargetPosition());
        telemetry.addData("Position: ", SpindexerHelper.findPosition());
        telemetry.addData("Current Position: ", SpindexerMotor.getCurrentPosition());
        telemetry.addData("stuff", Arrays.toString(motif1));
        telemetry.addData("stufffe", Arrays.toString(motif3));

        telemetry.update();
    }
}
