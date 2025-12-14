package org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Configuration;

public class DistanceSensorHelper {
    public static Rev2mDistanceSensor distanceSensor;


    public static void init(HardwareMap hw) {
        if (Configuration.distance.itemActive) {
            distanceSensor = hw.get(Rev2mDistanceSensor.class, Configuration.distance.itemName);
        }
    }

    public static boolean isBall() {
        return distanceSensor.getDistance(DistanceUnit.MM) < 130;
    }
}