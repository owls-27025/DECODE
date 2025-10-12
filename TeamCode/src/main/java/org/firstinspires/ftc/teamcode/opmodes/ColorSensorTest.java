package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import org.firstinspires.ftc.teamcode.opmodes.starter.ColorSensorHelper;

@TeleOp
public class ColorSensorTest extends LinearOpMode {
    
    public double average(double num1, double num2, double num3) {
        return (num1 + num2 + num3) / 3;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        ColorSensorHelper.init(hardwareMap);
        waitForStart();
        while(opModeIsActive()) {
            double num1 = ColorSensorHelper.readSample(hardwareMap.get(ColorSensor.class, "sensor1")).h;
            double currentTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < currentTime + 1500) {
                // do nothing
            }
            double num2 = ColorSensorHelper.readSample(hardwareMap.get(ColorSensor.class, "sensor1")).h;
            currentTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < currentTime + 1500) {
                // do nothing
            }
            double num3 = ColorSensorHelper.readSample(hardwareMap.get(ColorSensor.class, "sensor1")).h;
            currentTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < currentTime + 1500) {
                // do nothing
            }
            telemetry.addData("Average color: ", average(num1, num2, num3));
            telemetry.update();
            sleep(100000);
        }
    }
}