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
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "TeleOp", group = "OpModes")
public class V1 extends OpMode {
    // drivetrain
    public static double currentSpeed = 1;
    public static boolean isFieldCentric = false;
    boolean firing = false;

    @Override
    public void init() {
        // initialize subsystems
        Subsystems.init(hardwareMap, telemetry);
        firing = false;
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
            SpindexerHelper.SPEED -= 0.05;
        } else if (gamepad2.dpadRightWasPressed()) {
            SpindexerHelper.SPEED += 0.05;
        }

        if (gamepad2.left_bumper) {
            SpindexerHelper.moveHalfPosition(false);
        } else if (gamepad2.right_bumper) {
            SpindexerHelper.moveHalfPosition(true);
        }

        if (gamepad2.left_trigger != 0) {
            SpindexerHelper.intakePosition();
        } else if (gamepad2.right_trigger != 0) {
            SpindexerHelper.shootPosition();
        }

        Subsystems.intake(gamepad2);

        Subsystems.shoot(gamepad2);

        if (gamepad2.b) {
            Subsystems.currentShootState = Subsystems.ShootState.COMPLETED;
            Subsystems.currentState = Subsystems.IntakeState.COMPLETED;
        }

        drivetrain(gamepad1);
        telemetry();
    }

    private void telemetry() {
        // shooter telemetry
        telemetry.addData("Current Velocity", Subsystems.SHOOTER_VELOCITY);

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
        if (Subsystems.currentShootState == Subsystems.ShootState.FIRING) {
            firing = true;
        }

        telemetry.addData("Has fired", firing);

        telemetry.addData("Intake State", Subsystems.currentState);
        telemetry.addData("Delay", Subsystems.delayTimer.time(TimeUnit.MILLISECONDS));
        telemetry.addData("Delay started", Subsystems.delayStarted);

        telemetry.addData("spindexer motor position", SpindexerHelper.SpindexerMotor.getCurrentPosition());

        telemetry.addData("spindexer speed", SpindexerHelper.SPEED);

        telemetry.update();
    }
}