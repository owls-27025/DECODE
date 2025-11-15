package org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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
        telemetry.addData("Hue: ", ColorSensorHelper.readSample(ColorSensorHelper.colorSensor).h);
        telemetry.addData("Distance: ", ColorSensorHelper.colorSensor.getDistance(DistanceUnit.MM));
        telemetry.update();
    }
}