package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;

public class PIDTuning extends OwlsOpMode {
    PIDFCoefficients pidf;
    int selected;
    double velocity;
    double[] increments = {0.0001, 0.001, 0.01, 0.1, 1, 10};
    int selectedIncrement;

    @Override
    protected void onStart() {
        pidf = shooter.getPIDFCoefficients();
        selected = 0;
        velocity = 0;
        selectedIncrement = 0;
    }

    @Override
    protected void runLoop() {
        if (p1.pressed(OwlsGamepad.Button.A) && selected == 0) selected = 1;
        else if (p1.pressed(OwlsGamepad.Button.A) && selected == 1) selected = 0;

        if (p1.pressed(OwlsGamepad.Button.DPAD_LEFT)) selectedIncrement = Math.max(0, selectedIncrement - 1);
        else if (p1.pressed(OwlsGamepad.Button.DPAD_RIGHT)) selectedIncrement = Math.min(5, selectedIncrement + 1);

        if (selected == 0) {
            if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) pidf.i = pidf.i + increments[selectedIncrement];
            if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) pidf.i = pidf.i - increments[selectedIncrement];
        } else if (selected == 1) {
            if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) pidf.p = pidf.p + increments[selectedIncrement];
            if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) pidf.p = pidf.p - increments[selectedIncrement];
        }

        if (p1.pressed(OwlsGamepad.Button.B) && velocity == 0) velocity = 1500;
        else if (p1.pressed(OwlsGamepad.Button.B) && velocity != 0) velocity = 0;

        shooter.setPIDFCoefficients(pidf);
        shooter.shoot(velocity);
    }

    @Override
    protected void telemetry() {
        if (selected == 0) telemetry.addLine("Tuning I");
        if (selected == 1) telemetry.addLine("Tuning P");
        telemetry.addData("I", pidf.i);
        telemetry.addData("P", pidf.p);

        telemetry.addData("Real Velocity", shooter.getVelocity());
        telemetry.addData("Target Velocity", velocity);

        telemetry.addData("Increment", increments[selectedIncrement]);
    }
}
