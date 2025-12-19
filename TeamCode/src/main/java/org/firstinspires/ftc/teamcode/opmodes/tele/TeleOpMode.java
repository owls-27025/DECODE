package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.shared.actions.*;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;

@SuppressWarnings("unused")
public class TeleOpMode extends OwlsOpMode {
    private ActionManager actionManager;
    private Action intakeAction;
    private Action shootAction;
    private Action humanIntakeAction;
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
        if (!robot.isHumanIntake) {
            shooter.shoot(Robot.Globals.shooterVelocity);
        }

        // manual shooter vel change
        if (p2.pressed(OwlsGamepad.Button.DPAD_UP)) Robot.Globals.shooterVelocity += 50;
        if (p2.pressed(OwlsGamepad.Button.DPAD_DOWN)) Robot.Globals.shooterVelocity -= 50;

        // manual spindexer control
        if (p2.pressed(OwlsGamepad.Button.LB)) {
            if (robot.isHumanIntake) spindexer.moveToNextPosition();
            else spindexer.moveHalfPosition(false);
        } else if (p2.pressed(OwlsGamepad.Button.RB)) {
            if (robot.isHumanIntake) spindexer.moveToPreviousPosition();
            else spindexer.moveHalfPosition(true);
        }

        // manual spindexer pos
        if (p2.leftTriggerPressed(0.2)) spindexer.intakePosition();
        else if (p2.rightTriggerPressed(0.2)) spindexer.shootPosition();

        // rgb
        switch (robot.artifactCount) {
            case 0: light.blue(); break;
            case 1: light.red(); break;
            case 2: light.yellow(); break;
            case 3: light.green(); break;
        }

        // intake
        if (p1.pressed(OwlsGamepad.Button.A)) {
            actionManager.cancel(intakeAction);
            actionManager.cancel(spindexerAction);
            intakeAction = actionManager.addAndReturn(new IntakeAction(robot));
            spindexerAction = actionManager.addAndReturn(new SpindexerAction(robot));
        }

        // auto shoot
        if (p1.pressed(OwlsGamepad.Button.X)) {
            actionManager.cancel(shootAction);
            shootAction = actionManager.addAndReturn(new ShootAction(robot));
            robot.startShoot = true;
        }

        // manual shoot
        if (p1.pressed(OwlsGamepad.Button.Y)) {
            actionManager.cancel(shootAction);
            shootAction = actionManager.addAndReturn(new ShootAction(robot));
            robot.startShoot = true;
            robot.manualShoot = true;
        }

        // cancel
        if (p1.pressed(OwlsGamepad.Button.B) || p2.pressed(OwlsGamepad.Button.B)) {
            actionManager.cancelAll();
        }

        // reverse intake
        if (p1.held(OwlsGamepad.Button.BACK)) {
            robot.intakeReversed = true;
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

    }
}
