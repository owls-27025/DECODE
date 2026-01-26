package org.firstinspires.ftc.teamcode.opmodes.testing;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;

public class ServoTest extends OwlsOpMode {
    private boolean readRequested;

    @Override
    public void onStart() {
        limelight.setYawPos(0.5);
        limelight.setPitchPos(0.5);
    }

    @Override
    public void runLoop() {
        if (p1.pressed(OwlsGamepad.Button.A) || readRequested) {
            readRequested = !limelight.getMotif();
        }
        if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) limelight.setPitchPos(limelight.getPitchPos() + 0.01);
        if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) limelight.setPitchPos(limelight.getPitchPos() - 0.01);
        if (p1.pressed(OwlsGamepad.Button.DPAD_RIGHT)) limelight.setYawPos(limelight.getYawPos() + 0.01);
        if (p1.pressed(OwlsGamepad.Button.DPAD_LEFT)) limelight.setYawPos(limelight.getYawPos() - 0.01);
    }

    @Override
    public void telemetry() {
        telemetry.addData("pitch position", limelight.getPitchPos());
        telemetry.addData("yaw position", limelight.getYawPos());
        telemetry.addData("motif", Robot.Globals.motif);
        telemetry.addData("pipeline", limelight.getPipeline());
        telemetry.addData("id", limelight.getID());
    }
}