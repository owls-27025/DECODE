package org.firstinspires.ftc.teamcode.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.auto.actions.RRActions;
import org.firstinspires.ftc.teamcode.auto.actions.Shoot;
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
    public Action build(MecanumDrive drive, RRActions rractions) {
        Pose2d initialPose = getInitialPose();

        TrajectoryActionBuilder path1 = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(-11.5, 11.5));

        TrajectoryActionBuilder path2 = path1.endTrajectory().fresh()
                .waitSeconds(2)
                .turnTo(Math.toRadians(90));

        TrajectoryActionBuilder path3 = path2.endTrajectory().fresh()
                .lineToY(34)
                .waitSeconds(1)
                .lineToY(44, (pose2dDual, posePath, v) -> 8)
                .waitSeconds(1)
                .lineToY(50, (pose2dDual, posePath, v) -> 8)
                .strafeTo(new Vector2d(-11.5, 11.5))
                .turnTo(Math.toRadians(135));

        TrajectoryActionBuilder motifTurn = path1.endTrajectory().fresh()
                .turn(-360);

        return new SequentialAction(
//                rractions.spinUpShooter(),
                path1.build(),
                new ParallelAction(
                        rractions.readMotif(),
                        motifTurn.build()
                ),
                rractions.shoot(3),
                path2.build()
        );
    }
}
