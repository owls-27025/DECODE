package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

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

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(-11.6, 25));

            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(-11.6, 50));

            TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(11.5, 25));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(11.5, 50));

            TrajectoryActionBuilder goToIntakeThree = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(34.6, 25));

            TrajectoryActionBuilder intakeThree = goToIntakeThree.endTrajectory().fresh()
                    .strafeTo(new Vector2d(34.6, 50));


            return new SequentialAction(
                    rractions.stop(),
                    goToShoot.build(),
                    rractions.shoot(3, 1050)
//                    goToIntakeOne.build(),
//                    new ParallelAction(
//                            intakeOne.build(),
//                            rractions.intake()
//                    ),
//                    goToShoot.build(),
//                    rractions.shoot(3, 1050)
//                    goToIntakeTwo.build(),
//                    new ParallelAction(
//                            intakeTwo.build(),
//                            rractions.intake()
//                    ),
//                    goToShoot.build(),
//                    rractions.shoot(3, 900),
//                    goToIntakeThree.build(),
//                    new ParallelAction(
//                            intakeThree.build(),
//                            rractions.intake()
//                    ),
//                    goToShoot.build(),
//                    rractions.shoot(3, 900)
            );
        } else {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(-35, -35, Math.toRadians(-135)), Math.toRadians(-135));

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(-90))
                    .strafeTo(new Vector2d(-11.6, -25));

            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(-11.6, -50));

            TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(-90))
                    .strafeTo(new Vector2d(11.5, -25));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(11.5, -50));

            TrajectoryActionBuilder goToIntakeThree = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(-90))
                    .strafeTo(new Vector2d(34.6, -25));

            TrajectoryActionBuilder intakeThree = goToIntakeThree.endTrajectory().fresh()
                    .strafeTo(new Vector2d(34.6, -50));


            return new SequentialAction(
                    rractions.stop(),
                    goToShoot.build(),
                    rractions.shoot(3, 1050)
//                    goToIntakeOne.build(),
//                    new ParallelAction(
//                            intakeOne.build()
//                            rractions.intake()
//                    ),
//                    goToShoot.build(),
//                    rractions.shoot(3, 900)
//                    goToIntakeTwo.build(),
//                    new ParallelAction(
//                            intakeTwo.build()
//                            rractions.intake()
//                    ),
//                    goToShoot.build()
//                    rractions.shoot(3, 900),
//                    goToIntakeThree.build(),
//                    new ParallelAction(
//                            intakeThree.build()
//                            rractions.intake()
//                    );
//                    goToShoot.build()
//                    rractions.shoot(3, 900)
            );
        }
    }
}
