package org.firstinspires.ftc.teamcode.opmodes.V1.actions;

import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.Robot;

public class RRActions {
    private final Robot robot;

    public RRActions(Robot robot) {
        this.robot = robot;
    }
    public Action shoot(int artifacts) {
        return new Shoot(robot, artifacts);
    }

    public Action intake() {
        return new Intake(robot);
    }

    public Action readMotif() {
        return new ReadMotif(robot);
    }

    public Action humanPlayerIntake() {
        return new HumanPlayerIntake(robot);
    }
}