package org.firstinspires.ftc.teamcode.shared.actions;

import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.mechanisms.intake.Intake;

public class RRActions {
    private final Robot robot;

    public RRActions(Robot robot) {
        this.robot = robot;
    }

    public Action shoot(int artifacts, int velocity) {
        return new ShootAction(robot);
    }

    public Action intake() {
        return new IntakeAction(robot);
    }

    public Action readMotif() { return new ReadMotif(robot); }

    public Action spinUpShooter() { return new SpinUpShooter(robot); }

    public Action humanPlayerIntake() { return new HumanPlayerIntake(robot); }
}
