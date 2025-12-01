package org.firstinspires.ftc.teamcode.opmodes.V1.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.Mechanisms;
import org.firstinspires.ftc.teamcode.helpers.BaseOpMode;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "TeleOp", group = "OpModes")
public class TeleOpV1 extends BaseOpMode {

    private boolean firing = false;

    @Override
    protected void onInit() {
        telemetry.addLine("TeleOp init");
        telemetry.update();
    }

    @Override
    protected void onLoop() {
        if (controls.d2YJustPressed) {
            robot.shooterVelocity += 50;
        } else if (controls.d2AJustPressed) {
            robot.shooterVelocity -= 50;
        }

        if (controls.d2LBumper) {
            if (robot.mechanisms.isHumanIntake) {
                robot.spindexer.moveToNextPosition();
            } else {
                robot.spindexer.moveHalfPosition(false);
            }
        } else if (controls.d2RBumper) {
            if (robot.mechanisms.isHumanIntake) {
                robot.spindexer.moveToNextPosition();
            } else {
                robot.spindexer.moveHalfPosition(true);
            }
        }

        if (controls.driver2.left_trigger > 0.05) {
            robot.spindexer.intakePosition();
        } else if (controls.driver2.right_trigger > 0.05) {
            robot.spindexer.shootPosition();
        }

        robot.loop(controls.driver1, controls.driver2);

        if ((controls.d1BJustPressed || controls.d2BJustPressed) && !controls.inhibitButtons) {
            robot.mechanisms.currentShootState = Mechanisms.ShootState.COMPLETED;
            robot.mechanisms.currentState      = Mechanisms.IntakeState.COMPLETED;
        }

        if (controls.d1BackJustPressed) {
            robot.intake.reverse();
        }

        sendTelemetry();
    }

    private void sendTelemetry() {
        // shooter telemetry
        telemetry.addData("Shooter Velocity Target", robot.shooterVelocity);
        telemetry.addData("Shooter Velocity Current", robot.shooter.getVelocity());

        // color sensor telemetry
        telemetry.addData("Distance", robot.distanceSensor.distanceSensor.getDistance(DistanceUnit.MM));
        telemetry.addData("Detects Ball", robot.distanceSensor.isBall());

        // intake telemetry
        telemetry.addData("Artifacts", robot.mechanisms.artifactCount);

        // spindexer telemetry
        telemetry.addData("Spindexer Position", robot.spindexer.findPosition());
        telemetry.addData("Spindexer Motor Ticks", robot.spindexer.spindexerMotor.getCurrentPosition());
        telemetry.addData("Spindexer Speed", robot.SpindexerSpeed);

        // state machine telemetry
        telemetry.addData("Shooter State", robot.mechanisms.currentShootState);
        if (robot.mechanisms.currentShootState == Mechanisms.ShootState.FIRING) {
            firing = true;
        }
        telemetry.addData("Has fired", firing);

        telemetry.addData("Intake State", robot.mechanisms.currentState);
        telemetry.addData("Delay", robot.mechanisms.delayTimer.time(TimeUnit.MILLISECONDS));
        telemetry.addData("Delay started", robot.mechanisms.delayStarted);
    }
}
