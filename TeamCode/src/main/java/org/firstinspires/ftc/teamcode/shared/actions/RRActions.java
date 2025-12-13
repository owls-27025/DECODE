package org.firstinspires.ftc.teamcode.shared.actions;

import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Robot;

public class RRActions {
    private final Robot robot;

    public RRActions(Robot robot) {
        this.robot = robot;
    }

    public Action shoot(int artifacts, int velocity) {
        return new Shoot(robot, artifacts, velocity);
    }

    public Action intake() {
        return new Intake(robot);
    }

    public Action readMotif() { return new ReadMotif(robot); }

    public Action spinUpShooter() { return new SpinUpShooter(robot); }

    public Action humanPlayerIntake() { return new HumanPlayerIntake(robot); }
}
