package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;

@TeleOp
public class Pinpoint extends OpMode {
    GoBildaPinpointDriver pinpoint;

    @Override
    public void init() {
        Drivetrain.init(hardwareMap);
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Configuration.odometry.itemName);

    }

    @Override
    public void loop() {
        telemetry.addData("IMU Heading: ", pinpoint.getHeading(AngleUnit.DEGREES));
        pinpoint.update();
        telemetry.update();
    }
}