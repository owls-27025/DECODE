package org.firstinspires.ftc.teamcode.mechanisms.spindexer.distanceSensor;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DistanceSensorHelper {
    static Rev2mDistanceSensor distance;

    public static void init(HardwareMap hardwareMap) {
        distance = hardwareMap.get(Rev2mDistanceSensor.class, "distance sensor");
    }

    public static boolean isBall() {
        return distance.getDistance(DistanceUnit.MM) < 200;
    }
}