package org.firstinspires.ftc.teamcode.auto.paths;

import com.acmerobotics.roadrunner.*;

import org.firstinspires.ftc.teamcode.auto.actions.RRActions;
import org.firstinspires.ftc.teamcode.auto.actions.Shoot;
import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.jetbrains.annotations.NotNull;

import java.lang.Math;

public class OneCycle implements AutoPath {
    private final Globals.Alliances alliance;

    public OneCycle(Globals.Alliances alliance) {
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(-50, 50, Math.toRadians(135));
    }

    @Override
    public Action build(MecanumDrive drive, RRActions rractions) {
        Pose2d initialPose = getInitialPose();

        if (alliance == Globals.Alliances.RED) {
            TrajectoryActionBuilder path1 = drive.actionBuilder(initialPose)
                    .strafeTo(new Vector2d(-11.5, 11.5));


            TrajectoryActionBuilder motifTurn = path1.endTrajectory().fresh()
                    .turnTo(Math.toRadians(235))
                    .turnTo(Math.toRadians(135));

            TrajectoryActionBuilder path2 = motifTurn.endTrajectory().fresh()
                    .waitSeconds(2);

            return new SequentialAction(
                rractions.spinUpShooter(),
                    path1.build(),
                    new ParallelAction(
                            rractions.readMotif(),
                            motifTurn.build()
                    ),
                    rractions.shoot(3),
                    path2.build()
            );
        } else {
            TrajectoryActionBuilder path1 = drive.actionBuilder(initialPose, p -> new Pose2dDual<>(p.position.x, p.position.y.unaryMinus(), p.heading.plus(Math.PI)))
                    .strafeTo(new Vector2d(-11.5, 11.5));

            TrajectoryActionBuilder motifTurn = path1.endTrajectory().fresh()
                    .turnTo(Math.toRadians(235))
                    .turnTo(Math.toRadians(135));

            TrajectoryActionBuilder path2 = motifTurn.endTrajectory().fresh()
                    .waitSeconds(2);

            return new SequentialAction(
                rractions.spinUpShooter(),
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
}
