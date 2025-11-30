package org.firstinspires.ftc.teamcode.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.jetbrains.annotations.NotNull;

public class FourCycle implements AutoPath {
    private final Globals.Alliances alliance;

    public FourCycle(Globals.Alliances alliance) {
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Globals.Alliances.RED) {
            return new Pose2d(-50, 50, Math.toRadians(135));
        } else {
            return new Pose2d(-50, 50, Math.toRadians(135));
        }
    }

    @Override
    public Action build(MecanumDrive drive) {
        Pose2d initialPose = getInitialPose();

        TrajectoryActionBuilder path1 = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(-12, 12));

        TrajectoryActionBuilder path2 = path1.endTrajectory().fresh()
                .waitSeconds(5)
                .turnTo(Math.toRadians(90))
                .lineToY(30);

        TrajectoryActionBuilder path3 = path2.endTrajectory().fresh()
                .lineToY(38, (pose2dDual, posePath, v) -> 8)
                .waitSeconds(1)
                .lineToY(44, (pose2dDual, posePath, v) -> 8)
                .waitSeconds(1)
                .lineToY(50, (pose2dDual, posePath, v) -> 8)
                .strafeTo(new Vector2d(drive.localizer.getPose().position.x, 12)) // TODO: replace drive.localizer.getPose() because no work
                .turnTo(Math.toRadians(135));

        return new SequentialAction(
                path1.build(),
                path2.build(),
                path3.build()
        );
    }
}
