package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;

@TeleOp(name = "TeleOp", group = "OpModes")
public class PrototypeTest extends OpMode {
    @Override
    public void init() {
        Drivetrain board = new Drivetrain();
    }
    public void loop() {

    }
}
