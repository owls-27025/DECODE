package org.firstinspires.ftc.teamcode.auto.actions;

import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RRActions {
    public Action shoot(int artifacts, int velocity) {
        return new Shoot(artifacts, velocity);
    }

    public Action intake() {
        return new Intake();
    }

    public Action readMotif(Telemetry telemetry) { return new ReadMotif(telemetry); }

    public Action spinUpShooter() { return new SpinUpShooter(); }
}
