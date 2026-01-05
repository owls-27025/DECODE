package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;

public class ServoTest extends OwlsOpMode {
    @Override
    public void onStart() {
        limelight.setYawPos(0.5);
    }

    @Override
    public void runLoop() {
        if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) limelight.setYawPos(limelight.getYawPos() + 0.01);
        if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) limelight.setYawPos(limelight.getYawPos() - 0.01);
        limelight.getMotif();
    }

    @Override
    public void telemetry() {
        telemetry.addData("yaw position", limelight.getYawPos());
        telemetry.addData("motif", Robot.Globals.motif);


    }
}