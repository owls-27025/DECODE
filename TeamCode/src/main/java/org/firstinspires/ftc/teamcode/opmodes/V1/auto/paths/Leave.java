package org.firstinspires.ftc.teamcode.opmodes.V1.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.V1.auto.actions.RRActions;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;

public class Leave implements AutoPath {
    private Robot robot;
    private final Robot.Alliances alliance;

    public Leave(Robot robot, Robot.Alliances alliance) {
        this.robot = robot;
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Robot.Alliances.RED) {
            return new Pose2d(60, 10, Math.toRadians(0));
        } else {
            return new Pose2d(60, -10, Math.toRadians(0));
        }
    }

    @Override
    public Action build(MecanumDrive drive, RRActions rractions) {
        Pose2d initialPose = getInitialPose();

        return drive.actionBuilder(initialPose)
                .strafeTo(alliance == Robot.Alliances.RED
                        ? new Vector2d(57, 30)
                        : new Vector2d(57, -30))
                .build();
    }
}
