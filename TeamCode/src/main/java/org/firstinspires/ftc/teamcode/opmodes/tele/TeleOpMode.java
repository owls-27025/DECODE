package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import com.acmerobotics.roadrunner.Pose2d;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
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

        shooter.setHood(0.05);
        Robot.Globals.shooterVelocity = 1100;
    }

    @Override
    public void runLoop() {
        // manual shooter vel change
        if (p2.pressed(OwlsGamepad.Button.DPAD_UP)) Robot.Globals.shooterVelocity += 50;
        if (p2.pressed(OwlsGamepad.Button.DPAD_DOWN)) Robot.Globals.shooterVelocity -= 50;

        if (p2.pressed(OwlsGamepad.Button.RT)) {
            Robot.Globals.shooterVelocity = 1400;
            shooter.setHood(1.0);
        }
        if (p2.pressed(OwlsGamepad.Button.LT)) {
            Robot.Globals.shooterVelocity = 1100;
            shooter.setHood(0);
        }

        if (p2.pressed(OwlsGamepad.Button.A)) {
            Robot.Globals.shooterVelocity = 1200;
            shooter.setHood(0.5);
        }

        if (p2.pressed(OwlsGamepad.Button.DPAD_RIGHT)) shooter.setHood(shooter.getHood() + 0.1);
        if (p2.pressed(OwlsGamepad.Button.DPAD_LEFT)) shooter.setHood(shooter.getHood() - 0.1);

        if (p2.pressed(OwlsGamepad.Button.Y)) {
            if (spindexer.getFlapPosition() < 0.5) {
                spindexer.flapUp();
            } else {
                spindexer.flapDown();
            }
        }

        if (p2.pressed(OwlsGamepad.Button.X)) {
            spindexer.toggleStop();
        }

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

        if (p1.released(OwlsGamepad.Button.X)) {
            robot.queueStop = true;
        }

        // manual shoot
        if (p1.pressed(OwlsGamepad.Button.Y)) {
            robot.manualShoot = true;
        }

        // cancel
        if (p1.pressed(OwlsGamepad.Button.B) || p2.pressed(OwlsGamepad.Button.B)) {
            robot.forceStop = true;
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
        telemetry.addData("Shooter tol", Robot.Globals.shooterLowTolerance);

        telemetry.addData("Distance (mm)", distance.getDistanceMm());
        telemetry.addData("Detects Ball", distance.isBall());

        telemetry.addData("Artifacts", robot.artifactCount);
        telemetry.addData("Spindexer pos", spindexer.findPosition());
        telemetry.addData("Spindexer motor", spindexer.getCurrent());
        telemetry.addData("Spindexer speed", Robot.Globals.spindexerSpeed);

        telemetry.addData("Hood Pos", shooter.getHood());

        telemetry.addData("Field Centric", Robot.Globals.isFieldCentric);
        telemetry.addData("Right Stick", Robot.Globals.isRightStick);
        telemetry.addData("Drive Speed", Robot.Globals.driveSpeed);
        telemetry.addData("Slow Speed", Robot.Globals.slowDriveSpeed);

        telemetry.addData("Robot Stopped", robot.forceStop);

        telemetry.addData("Distance to Goal", limelight.getDistanceToGoal());

        telemetry.addData("tY", limelight.getTy());
        telemetry.addData("Is limelight running", limelight.doesExist());
        telemetry.addData("Is result valid", limelight.getLatestResult().isValid());
        telemetry.addData("ID", limelight.getID());

        telemetry.addData("IMU Heading", drivetrain.getOdometryHeading());

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("Drive x", drivetrain.getPose().getX(DistanceUnit.INCH));
        packet.put("Drive y", drivetrain.getPose().getY(DistanceUnit.INCH));
        packet.put("Drive heading (deg)", drivetrain.getPose().getHeading(AngleUnit.DEGREES));

        packet.put("Limelight x", limelight.getLatestResult().getBotpose_MT2().getPosition().toUnit(DistanceUnit.INCH).x);
        packet.put("Limelight y", limelight.getLatestResult().getBotpose_MT2().getPosition().toUnit(DistanceUnit.INCH).y);
        packet.put("Limelight heading (deg)", limelight.getLatestResult().getBotpose_MT2().getOrientation().getYaw(AngleUnit.DEGREES));

        dash.sendTelemetryPacket(packet);
    }
}