package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.auto.RRActions;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;

public class ThreeCycleFront implements AutoPath {
    private final Robot.Globals.Alliances alliance;

    public ThreeCycleFront(Robot.Globals.Alliances alliance) {
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Robot.Globals.Alliances.RED) {
            return new Pose2d(-50, 50, Math.toRadians(135));
        } else {
            return new Pose2d(-50, -50, Math.toRadians(235));
        }
    }

    @Override
    public String getName() {
        return "Three Cycel :3";
    }

    @Override
    public Action build(MecanumDrive drive, RRActions rractions, Telemetry telemetry) {
        Pose2d initialPose = getInitialPose();

        if (alliance == Robot.Globals.Alliances.RED) {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(-35, 35, Math.toRadians(135)), Math.toRadians(135));

            return new SequentialAction(
                    rractions.spinUpShooter(),
                    goToShoot.build(),
                    new ParallelAction(
                            rractions.spinUpShooter(),
                            rractions.shoot(3, 900)
                    )
            );
        } else {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(-35, -35, Math.toRadians(225)), Math.toRadians(225));

            return new SequentialAction(
                    rractions.spinUpShooter(),
                    goToShoot.build(),
                    new ParallelAction(
                            rractions.spinUpShooter(),
                            rractions.shoot(3, 900)
                    )
            );
        }
    }
}
