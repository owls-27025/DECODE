package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.ColorSensorHelper;

import java.util.concurrent.TimeUnit;

@TeleOp
public class intakeTest extends OpMode {

    @Override
    public void init() {
        Drivetrain.init(hardwareMap);
        Subsystems.init(hardwareMap, telemetry);
        Subsystems.currentState = Subsystems.IntakeState.MOVING_TO_POSITION;
    }

    @Override
    public void loop() {
        if (Subsystems.artifactCount < 3) {
            Subsystems.intakeAuto();
        }

        Subsystems.drivetrain(gamepad1);

        telemetry.addData("artifact count", Subsystems.artifactCount);
        telemetry.addData("distance", ColorSensorHelper.colorSensor.getDistance(DistanceUnit.MM));
        telemetry.addData("delay timer", Subsystems.delayTimer.time(TimeUnit.MILLISECONDS));
        telemetry.update();
    }
}
