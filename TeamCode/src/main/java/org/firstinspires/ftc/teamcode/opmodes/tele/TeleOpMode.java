package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.shared.actions.*;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;

@SuppressWarnings("unused")
public class TeleOpMode extends OwlsOpMode {
    private ActionManager actionManager;
    private Action intakeAction;
    private Action shootAction;
    private Action spindexerAction;

    public enum PreviousIntakeState { STOPPED, FORWARD, NA }

    @Override
    public void onInit() {
        actionManager = new ActionManager();
    }

    @Override
    public void onStart() {
        intakeAction = actionManager.addAndReturn(new IntakeAction(robot));
        shootAction = actionManager.addAndReturn(new ShootAction(robot));
        spindexerAction = actionManager.addAndReturn(new SpindexerAction(robot));
    }

    @Override
    public void runLoop() {
        // manual shooter vel change
        if (p2.pressed(OwlsGamepad.Button.DPAD_UP)) Robot.Globals.shooterVelocity += 50;
        if (p2.pressed(OwlsGamepad.Button.DPAD_DOWN)) Robot.Globals.shooterVelocity -= 50;

        if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) shooter.setHood(shooter.getHood() + 0.1);
        if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) shooter.setHood(shooter.getHood() - 0.1);

        // manual spindexer control
        if (p2.pressed(OwlsGamepad.Button.LB)) {
            robot.leftRequested = true;
        } else if (p2.pressed(OwlsGamepad.Button.RB)) {
            robot.rightRequested = true;
        }

        // manual spindexer pos
        if (p2.leftTriggerPressed(0.2)) spindexer.intakePosition();
        else if (p2.rightTriggerPressed(0.2)) spindexer.shootPosition();

        // rgb
        if (!robot.intakeReversed) {
            switch (robot.artifactCount) {
                case 0:
                    light.off();
                    break;
                case 1:
                    light.red();
                    break;
                case 2:
                    light.yellow();
                    break;
                case 3:
                    light.green();
                    break;
            }
        }

        // intake
        if (p1.pressed(OwlsGamepad.Button.A)) {
            robot.startIntake = true;
        }

        // auto shoot
        if (p1.pressed(OwlsGamepad.Button.X)) {
            robot.startShoot = true;
        }

        // manual shoot
        if (p1.pressed(OwlsGamepad.Button.Y)) {
            robot.manualShoot = true;
        }

        // cancel
        if (p1.pressed(OwlsGamepad.Button.B) || p2.pressed(OwlsGamepad.Button.B)) {
            robot.stop = true;
        }

        // reverse intake
        if (p1.held(OwlsGamepad.Button.BACK)) {
            robot.intakeReversed = true;
            light.blue();
        } else {
            robot.intakeReversed = false;
            robot.intakeReverseCompleted = true;
        }

        // human intake
        if (p2.pressed(OwlsGamepad.Button.BACK)) {
            robot.isHumanIntake = !robot.isHumanIntake;
        }

        // reset spindexer encoder
        if (p2.pressed(OwlsGamepad.Button.RS)) spindexer.reset();

        // reset artifact count
        if (p1.pressed(OwlsGamepad.Button.LS)) robot.artifactCount = 0;

        drivetrain.drive(p1);

        TelemetryPacket packet = new TelemetryPacket();
        actionManager.run(packet);
        dash.sendTelemetryPacket(packet);
    }

    @Override
    public void telemetry() {
        telemetry.addData("Shooter target vel", Robot.Globals.shooterVelocity);
        telemetry.addData("Shooter vel", shooter.getVelocity());
        telemetry.addData("Shooter tol", Robot.Globals.shooterTolerance);

        telemetry.addData("Distance (mm)", distance.getDistanceMm());
        telemetry.addData("Detects Ball", distance.isBall());

        telemetry.addData("Artifacts", robot.artifactCount);
        telemetry.addData("Spindexer pos", spindexer.findPosition());
        telemetry.addData("Spindexer motor", spindexer.getCurrent());
        telemetry.addData("Spindexer speed", Robot.Globals.spindexerSpeed);

        telemetry.addData("Field Centric", Robot.Globals.isFieldCentric);
        telemetry.addData("Right Stick", Robot.Globals.isRightStick);
        telemetry.addData("Drive Speed", Robot.Globals.driveSpeed);
        telemetry.addData("Slow Speed", Robot.Globals.slowDriveSpeed);

        telemetry.addData("Robot Stopped", robot.stop);
    }
}
