package org.firstinspires.ftc.teamcode.opmodes.V1.auto.actions;

import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.teamcode.Robot;

public class RRActions {
    private Robot robot;

    public RRActions(Robot robot) {
        this.robot = robot;
    }
    public Action shoot(int artifacts) {
        return new Shoot(robot, artifacts);
    }

    public Action intake() {
        return new Intake(robot);
    }

    public Action readMotif() { return new ReadMotif(robot); }
}