package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.drivetrain;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.intake;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.shoot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;

@TeleOp(name = "TeleOp", group = "OpModes")
public class V1 extends OpMode {
    // drivetrain
    public static double currentSpeed = 1;
    public static boolean isFieldCentric = false;

    @Override
    public void init() {
        // initialize subsystems
        Subsystems.init(hardwareMap, telemetry);
    }

    public void loop() {
        // change shooter velocity
        if (gamepad2.dpadUpWasPressed()) {
            Subsystems.SHOOTER_VELOCITY += 50;
        } else if (gamepad2.dpadDownWasPressed()) {
            Subsystems.SHOOTER_VELOCITY -= 50;
        }

        // change shooter subtraction
        if (gamepad2.dpadLeftWasPressed()) {
            Subsystems.SUBTRACTION_VELOCITY -= 10;
        } else if (gamepad2.dpadRightWasPressed()) {
            Subsystems.SUBTRACTION_VELOCITY += 10;
        }

        Subsystems.intake(gamepad2);

        Subsystems.shoot(gamepad2);

        drivetrain(gamepad1);
        telemetry();
    }

    private void telemetry() {
        // shooter telemetry
        telemetry.addData("Shooting Velocity", Subsystems.SHOOTER_VELOCITY);
        telemetry.addData("Shooting Subtraction", Subsystems.SUBTRACTION_VELOCITY);

        // color sensor telemetry
        telemetry.addData("Distance", ColorSensorHelper.colorSensor.getDistance(DistanceUnit.MM));
        telemetry.addData("Detects Ball", ColorSensorHelper.isBall());

        // intake telemetry
        telemetry.addData("Artifacts", Subsystems.artifactCount);

        // spindexer telemetry
        telemetry.addData("Spindexer Position", SpindexerHelper.findPosition());

        // drivetrain telemetry
        telemetry.addData("Field Centric", isFieldCentric);

        // state machine telemetry
        telemetry.addData("Shooter State", Subsystems.currentShootState);
        telemetry.addData("Intake State", Subsystems.currentState);


        telemetry.update();
    }
}