package org.firstinspires.ftc.teamcode.opmodes.testing;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoParams;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;

public class ServoTest extends OwlsOpMode {
    private boolean readRequested;
    private AutoParams autoParams;

    @Override
    public void onStart() {
        limelight.setYawPos(0.5);
        limelight.setPitchPos(0.5);
        autoParams = new AutoParams();
        autoParams.strategy = AutoParams.Strategies.FRONT;
        autoParams.alliance = AutoParams.Alliances.RED;
    }

    @Override
    public void runLoop() {
        if (p1.pressed(OwlsGamepad.Button.A) || readRequested) {
            readRequested = !limelight.getMotif(autoParams);
        }

        if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) limelight.setPitchPos(limelight.getPitchPos() + 0.01);
        if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) limelight.setPitchPos(limelight.getPitchPos() - 0.01);
        if (p1.pressed(OwlsGamepad.Button.DPAD_RIGHT)) limelight.setYawPos(limelight.getYawPos() + 0.01);
        if (p1.pressed(OwlsGamepad.Button.DPAD_LEFT)) limelight.setYawPos(limelight.getYawPos() - 0.01);
        if (p1.pressed(OwlsGamepad.Button.X)) autoParams.strategy = autoParams.strategy == AutoParams.Strategies.FRONT ? AutoParams.Strategies.BACK : AutoParams.Strategies.FRONT;
        if (p1.pressed(OwlsGamepad.Button.B)) autoParams.alliance = autoParams.alliance == AutoParams.Alliances.RED ? AutoParams.Alliances.BLUE : AutoParams.Alliances.RED;
    }

    @Override
    public void telemetry() {
        telemetry.addData("pitch position", limelight.getPitchPos());
        telemetry.addData("yaw position", limelight.getYawPos());
        telemetry.addData("motif", Robot.Globals.motif);
        telemetry.addData("pipeline", limelight.getPipeline());
        telemetry.addData("id", limelight.getID());
        telemetry.addData("alliance", autoParams.alliance);
        telemetry.addData("side", autoParams.strategy);
    }
}