package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;

@TeleOp
public class encoders extends OpMode {
    @Override
    public void init() {
        Subsystems.init(hardwareMap, telemetry);
        Drivetrain.init(hardwareMap);
        telemetry.setAutoClear(false);
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            for (int i = 0; i < 5; i++) {
                SpindexerHelper.moveHalfPosition(true);
                telemetry.addData("current pos", SpindexerHelper.SpindexerMotor.getCurrentPosition());
                telemetry.addData("off by", Math.abs((SpindexerHelper.SpindexerMotor.getCurrentPosition() - SpindexerHelper.TPR)));
                while (SpindexerHelper.SpindexerMotor.isBusy());
                telemetry.update();
            }
        }
    }
}
