package org.firstinspires.ftc.teamcode.shared.mechanisms.distance;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Robot;

public class Distance {
    private final Rev2mDistanceSensor distance;

    public Distance(Robot robot) {
        distance = robot.registerItem(Rev2mDistanceSensor.class, robot.config.distance);
    }

    public boolean isBall() {
        return distance != null && distance.getDistance(DistanceUnit.MM) < 130;
    }

    public double getDistanceMm() {
        return distance == null ? Double.POSITIVE_INFINITY : distance.getDistance(DistanceUnit.MM);
    }
}
