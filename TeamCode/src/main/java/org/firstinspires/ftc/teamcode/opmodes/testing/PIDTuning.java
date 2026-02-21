package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.arcrobotics.ftclib.controller.PController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;

public class PIDTuning extends OwlsOpMode {
    PIDFController pidf;
    PIDFController pidf1;
    PIDFController pidf2;
    int selected;
    double velocity;
    double[] increments = {0.0001, 0.001, 0.01, 0.1, 1, 10};
    int selectedIncrement;

    @Override
    protected void onStart() {
        pidf1 = new PIDFController(0.001, 0, 0, 0.0004);
        pidf2 = new PIDFController(0.001, 0, 0, 0.0004);
        pidf = pidf1;
        selected = 0;
        velocity = 0;
        selectedIncrement = 0;
    }

    @Override
    protected void runLoop() {
        if (p1.pressed(OwlsGamepad.Button.X) && pidf == pidf1) pidf = pidf2;
        else if (p1.pressed(OwlsGamepad.Button.X) && pidf == pidf2) pidf = pidf1;

        if (p1.pressed(OwlsGamepad.Button.A) && selected == 0) selected = 1;
        else if (p1.pressed(OwlsGamepad.Button.A) && selected == 1) selected = 2;
        else if (p1.pressed(OwlsGamepad.Button.A) && selected == 2) selected = 3;
        else if (p1.pressed(OwlsGamepad.Button.A) && selected == 3) selected = 0;

        if (p1.pressed(OwlsGamepad.Button.DPAD_LEFT)) selectedIncrement = Math.max(0, selectedIncrement - 1);
        else if (p1.pressed(OwlsGamepad.Button.DPAD_RIGHT)) selectedIncrement = Math.min(5, selectedIncrement + 1);

        if (selected == 0) {
            if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) pidf.setP(pidf.getP() + increments[selectedIncrement]);
            if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) pidf.setP(pidf.getP() - increments[selectedIncrement]);
        } else if (selected == 1) {
            if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) pidf.setI(pidf.getI() + increments[selectedIncrement]);
            if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) pidf.setI(pidf.getI() - increments[selectedIncrement]);
        } else if (selected == 2) {
            if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) pidf.setD(pidf.getD() + increments[selectedIncrement]);
            if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) pidf.setD(pidf.getD() - increments[selectedIncrement]);
        } else if (selected == 3) {
            if (p1.pressed(OwlsGamepad.Button.DPAD_UP)) pidf.setF(pidf.getF() + increments[selectedIncrement]);
            if (p1.pressed(OwlsGamepad.Button.DPAD_DOWN)) pidf.setF(pidf.getF() - increments[selectedIncrement]);
        }

        if (p1.pressed(OwlsGamepad.Button.B) && velocity == 0) velocity = 1500;
        else if (p1.pressed(OwlsGamepad.Button.B) && velocity != 0) velocity = 0;

        double output = pidf.calculate(shooter.getVelocity(pidf == pidf1 ? 1 : 2), velocity);
        shooter.shoot(output, pidf == pidf1 ? 1 : 2);
    }

    @Override
    protected void telemetry() {
        if (pidf == pidf1) telemetry.addLine("Tuning motor 1");
        if (pidf == pidf2) telemetry.addLine("Tuning motor 2");

        if (selected == 0) telemetry.addLine("Tuning P");
        if (selected == 1) telemetry.addLine("Tuning I");
        if (selected == 2) telemetry.addLine("Tuning D");
        if (selected == 3) telemetry.addLine("Tuning F");

        telemetry.addData("P", pidf.getP());
        telemetry.addData("I", pidf.getI());
        telemetry.addData("D", pidf.getD());
        telemetry.addData("F", pidf.getF());

        telemetry.addData("Real Velocity", shooter.getVelocity(pidf == pidf1 ? 1 : 2));
        telemetry.addData("Target Velocity", velocity);
        telemetry.addData("Power", shooter.getPower(pidf == pidf1? 1 : 2));

        telemetry.addData("Increment", increments[selectedIncrement]);
    }
}