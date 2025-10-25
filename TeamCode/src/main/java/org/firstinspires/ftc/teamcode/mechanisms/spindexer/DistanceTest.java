package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "distance sensor", group = "Spindexer")
public class DistanceTest extends OpMode {
    Rev2mDistanceSensor distance;

    @Override
    public void init() {
        distance = hardwareMap.get(Rev2mDistanceSensor.class, "distance sensor");
    }

    @Override
    public void loop() {
        telemetry.addData("distance", distance.getDistance(DistanceUnit.MM));
        telemetry.update();
    }
}
