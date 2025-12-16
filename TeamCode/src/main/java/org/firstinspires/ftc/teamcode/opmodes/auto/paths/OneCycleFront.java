package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.acmerobotics.roadrunner.*;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.shared.actions.RRActions;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;

import java.lang.Math;

public class OneCycleFront implements AutoPath {
    private final Robot.Globals.Alliances alliance;

    public OneCycleFront(Robot.Globals.Alliances alliance) {
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
        return "One Cycle (Front)";
    }

    @Override
    public Action build(MecanumDrive drive, RRActions rractions, Telemetry telemetry) {
        Pose2d initialPose = getInitialPose();

        if (alliance == Robot.Globals.Alliances.RED) {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(-35, 35, Math.toRadians(135)), Math.toRadians(135));

            TrajectoryActionBuilder motifTurn = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(225), new TurnConstraints(1, -1, 1))
                    .waitSeconds(5)
                    .turnTo(Math.toRadians(135));

            TrajectoryActionBuilder shootWait = motifTurn.endTrajectory().fresh()
                    .waitSeconds(2.5);

            TrajectoryActionBuilder goToIntakeOne = shootWait.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(-52, 20));

            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .lineToY(50, (pose2dDual, posePath, v) -> 5);

            TrajectoryActionBuilder goToIntakeTwo = shootWait.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(12, 30));


            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .lineToY(-50, (pose2dDual, posePath, v) -> 5)
                    .strafeTo(new Vector2d(-35, -20))
                    .turnTo(Math.toRadians(225));

            return new SequentialAction(
                    rractions.spinUpShooter(),
                    goToShoot.build(),
//                    new ParallelAction(
//                            rractions.readMotif(telemetry),
//                            motifTurn.build()
//                    ),
                    new ParallelAction(
                            rractions.spinUpShooter(),
                            rractions.shoot(3, 900)
                    )
//                    goToIntakeOne.build()
//                    new ParallelAction(
//                        rractions.intake(),
//                        intakeOne.build()
//                    )
//                    shootWait.build(),
//                    goToIntakeTwo.build(),
//                    intakeTwo.build(),
//                    shootWait.build()
            );
        } else {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(-35, -35, Math.toRadians(225)), Math.toRadians(225));

            TrajectoryActionBuilder motifTurn = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(135), new TurnConstraints(1, -1, 1))
                    .waitSeconds(5)
                    .turnTo(Math.toRadians(225));

            TrajectoryActionBuilder shootWait = motifTurn.endTrajectory().fresh()
                    .waitSeconds(2.5);

            TrajectoryActionBuilder goToIntakeOne = shootWait.endTrajectory().fresh()
                    .turnTo(Math.toRadians(-90))
                    .strafeTo(new Vector2d(-52, -20));

            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .turnTo(Math.toRadians(-90))
                    .lineToY(-50, (pose2dDual, posePath, v) -> 5);

            TrajectoryActionBuilder goToIntakeTwo = shootWait.endTrajectory().fresh()
                    .turnTo(Math.toRadians(-90))
                    .strafeTo(new Vector2d(12, -30));


            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .lineToY(50, (pose2dDual, posePath, v) -> 5)
                    .strafeTo(new Vector2d(-35, -20))
                    .turnTo(Math.toRadians(225));

            return new SequentialAction(
                    rractions.spinUpShooter(),
                    goToShoot.build(),
//                    new ParallelAction(
//                            rractions.readMotif(telemetry),
//                            motifTurn.build()
//                    ),
                    new ParallelAction(
                            rractions.spinUpShooter(),
                            rractions.shoot(3, 900)
                    )
//                    goToIntakeOne.build()
//                    new ParallelAction(
//                        rractions.intake(),
//                        intakeOne.build()
//                    ),
//                    shootWait.build()
//                    goToIntakeTwo.build(),
//                    intakeTwo.build(),
//                    shootWait.build()
            );
        }
    }
}