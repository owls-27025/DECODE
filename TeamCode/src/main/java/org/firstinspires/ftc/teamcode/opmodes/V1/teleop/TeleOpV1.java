package org.firstinspires.ftc.teamcode.opmodes.V1.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.helpers.BaseOpMode;
import org.firstinspires.ftc.teamcode.opmodes.V1.actions.HumanPlayerIntake;
import org.firstinspires.ftc.teamcode.opmodes.V1.actions.Shoot;
import org.firstinspires.ftc.teamcode.opmodes.V1.actions.Intake;

@TeleOp(name = "TeleOp", group = "OpModes")
public class TeleOpV1 extends BaseOpMode {
    private Shoot  currentShootAction  = null;
    private Intake currentIntakeAction = null;

    @Override
    protected void onInit() {
        // yo
    }

    @Override
    protected void onLoop() {
        if (controls.d1AJustPressed
                && currentIntakeAction == null
                && currentShootAction == null
                && !controls.inhibitButtons) {
            currentIntakeAction = new Intake(robot);
        }

        if (controls.d2XJustPressed
                && currentShootAction == null
                && !controls.inhibitButtons) {
            int numArtifacts = robot.artifactCount;
            if (numArtifacts > 0) {
                currentShootAction = new Shoot(robot, numArtifacts);
            }
        }

        if (controls.d2YJustPressed
                && currentShootAction == null
                && !controls.inhibitButtons) {
            int numArtifacts = robot.forcedArtifacts;
            if (numArtifacts > 0) {
                currentShootAction = new Shoot(robot, numArtifacts);
            }
        }

        if ((controls.d1BJustPressed || controls.d2BJustPressed)
                && !controls.inhibitButtons) {
            cancelActions();
        }

        if (controls.d2LBumper) {
            if (robot.isHumanIntake) {
                robot.spindexer.moveToNextPosition();
            } else {
                robot.spindexer.moveHalfPosition(false);
            }
        } else if (controls.d2RBumper) {
            if (robot.isHumanIntake) {
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

        if (controls.d2BackJustPressed && !controls.inhibitButtons) {
            TelemetryPacket packet = new TelemetryPacket();
            new HumanPlayerIntake(robot).run(packet);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }

        if (currentIntakeAction != null) {
            TelemetryPacket packet = new TelemetryPacket();
            boolean stillRunning = currentIntakeAction.run(packet);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);

            if (!stillRunning) {
                currentIntakeAction = null;
            }
        }

        if (currentShootAction != null) {
            TelemetryPacket packet = new TelemetryPacket();
            boolean stillRunning = currentShootAction.run(packet);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);

            if (!stillRunning) {
                currentShootAction = null;
            }
        }

        if (controls.d1BackJustPressed) {
            robot.intake.reverse();
        }
    }

    private void cancelActions() {
        currentShootAction  = null;
        currentIntakeAction = null;

        robot.intake.stop();

        robot.spindexer.moveServo(0.5);
    }

    @Override
    protected void telemetry() {
        // yo
    }
}
