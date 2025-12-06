package org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Configuration;

public class DistanceSensorHelper {
    public static Rev2mDistanceSensor colorSensor;


    public static void init(HardwareMap hw) {
        if (Configuration.distance.itemActive) {
            colorSensor = hw.get(Rev2mDistanceSensor.class, Configuration.distance.itemName);
        }
    }

    public static boolean isBall() {
        return colorSensor.getDistance(DistanceUnit.MM) < 130;
    }
}