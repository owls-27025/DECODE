package org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Color Sensor Test", group = "ColorSensor")
public class ColorSensorTest extends OpMode {
    
    public double average(double num1, double num2, double num3) {
        return (num1 + num2 + num3) / 3;
    }

    @Override
    public void init() {
        ColorSensorHelper.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Color: ", ColorSensorHelper.getColor());
        telemetry.update();
    }
}