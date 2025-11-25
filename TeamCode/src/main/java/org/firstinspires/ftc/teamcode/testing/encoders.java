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
        SpindexerHelper.SpindexerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        SpindexerHelper.SpindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Drivetrain.init(hardwareMap);
    }

    @Override
    public void loop() {
        SpindexerHelper.SpindexerMotor.setPower(gamepad1.left_stick_y * 0.05);
        telemetry.addData("spindexer encoder", SpindexerHelper.SpindexerMotor.getCurrentPosition());
        telemetry.update();
    }
}
