package org.firstinspires.ftc.teamcode.auto.actions;

import com.acmerobotics.roadrunner.Action;

public class RRActions {
    public Action shoot(int artifacts) {
        return new Shoot(artifacts);
    }

    public Action intake() {
        return new Intake();
    }

    public Action readMotif() { return new ReadMotif(); }

    public Action spinUpShooter() { return new SpinUpShooter(); }
}
