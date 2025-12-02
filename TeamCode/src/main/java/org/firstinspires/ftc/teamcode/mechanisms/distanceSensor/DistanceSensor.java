package org.firstinspires.ftc.teamcode.mechanisms.distanceSensor;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.helpers.Configuration.ItemConfig;
import org.firstinspires.ftc.teamcode.helpers.BaseSubsystem;

public class DistanceSensor extends BaseSubsystem {

    public Rev2mDistanceSensor distanceSensor;

    public DistanceSensor(HardwareMap hw,
                          Telemetry telemetry,
                          ItemConfig cfg) {
        super(hw, telemetry, cfg.itemActive);

        ifActive(() -> distanceSensor = hw.get(Rev2mDistanceSensor.class, cfg.itemName));
    }

    public boolean isBall() {
        if (!active || distanceSensor == null) return false;
        return distanceSensor.getDistance(DistanceUnit.MM) < 130;
    }

    public double getDistance() {
        return distanceSensor.getDistance(DistanceUnit.MM);
    }
}
