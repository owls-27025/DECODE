package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.shared.actions.ActionManager;
import org.firstinspires.ftc.teamcode.shared.actions.HumanPlayerIntake;
import org.firstinspires.ftc.teamcode.shared.actions.Intake;
import org.firstinspires.ftc.teamcode.shared.actions.Shoot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;

@TeleOp(name = "TeleOp", group = "OpModes")
@SuppressWarnings("unused")
public class TeleOpMode extends OwlsOpMode {
    private ActionManager actionManager;
    private Action intakeAction;
    private Action shootAction;
    private Action humanIntakeAction;

    public enum PreviousIntakeState { STOPPED, FORWARD, NA }

    @Override
    public void onInit() {
        actionManager = new ActionManager();
    }

    @Override
    public void runLoop() {
        if (!robot.isHumanIntake) {
            shooter.shoot(robot.shooterVelocity);
        }

        if (p2.pressed(OwlsGamepad.Button.DPAD_UP)) robot.shooterVelocity += 50;
        if (p2.pressed(OwlsGamepad.Button.DPAD_DOWN)) robot.shooterVelocity -= 50;

        if (p2.pressed(OwlsGamepad.Button.LB)) {
            if (robot.isHumanIntake) spindexer.moveToNextPosition();
            else spindexer.moveHalfPosition(false);
        } else if (p2.pressed(OwlsGamepad.Button.RB)) {
            if (robot.isHumanIntake) spindexer.moveToPreviousPosition();
            else spindexer.moveHalfPosition(true);
        }

        if (p2.leftTriggerPressed(0.2)) spindexer.intakePosition();
        else if (p2.rightTriggerPressed(0.2)) spindexer.shootPosition();

        switch (robot.artifactCount) {
            case 0: light.blue(); break;
            case 1: light.red(); break;
            case 2: light.yellow(); break;
            case 3: light.green(); break;
        }

        if (p1.pressed(OwlsGamepad.Button.A)) {
            actionManager.cancel(intakeAction);
            intakeAction = actionManager.addAndReturn(new Intake(robot));
        }

        if (p1.pressed(OwlsGamepad.Button.X)) {
            actionManager.cancel(shootAction);
            shootAction = actionManager.addAndReturn(new Shoot(robot, robot.artifactCount, robot.shooterVelocity));
        }

        if (p1.pressed(OwlsGamepad.Button.Y)) {
            actionManager.cancel(shootAction);
            shootAction = actionManager.addAndReturn(new Shoot(robot, 1, robot.shooterVelocity));
        }

        if (p1.pressed(OwlsGamepad.Button.B) || p2.pressed(OwlsGamepad.Button.B)) {
            actionManager.cancelAll();
        }

        if (p1.held(OwlsGamepad.Button.BACK)) {
            intake.reverse();
            robot.intakeReversed = true;
        } else {
            robot.intakeReversed = false;
        }

        if (p2.pressed(OwlsGamepad.Button.BACK)) {
            actionManager.cancel(humanIntakeAction);
            humanIntakeAction = actionManager.addAndReturn(new HumanPlayerIntake(robot));
        }

        if (p2.pressed(OwlsGamepad.Button.RS)) spindexer.reset();

        if (p1.pressed(OwlsGamepad.Button.LS)) robot.artifactCount = 0;

        drivetrain.drive(p1);

        TelemetryPacket packet = new TelemetryPacket();
        actionManager.run(packet);
        dash.sendTelemetryPacket(packet);
    }

    @Override
    public void telemetry() {
        telemetry.addData("Shooter target vel", robot.shooterVelocity);
        telemetry.addData("Shooter vel", shooter.getVelocity());
        telemetry.addData("Shooter tol", robot.shooterTolerance);

        telemetry.addData("Distance (mm)", distance.getDistanceMm());
        telemetry.addData("Detects Ball", distance.isBall());

        telemetry.addData("Artifacts", robot.artifactCount);
        telemetry.addData("Spindexer pos", spindexer.findPosition());
        telemetry.addData("Spindexer motor", spindexer.getCurrent());
        telemetry.addData("Spindexer speed", robot.spindexerSpeed);

        telemetry.addData("Field Centric", robot.isFieldCentric);
        telemetry.addData("Right Stick", robot.isRightStick);
        telemetry.addData("Drive Speed", robot.driveSpeed);
        telemetry.addData("Slow Speed", robot.slowDriveSpeed);

    }
}
