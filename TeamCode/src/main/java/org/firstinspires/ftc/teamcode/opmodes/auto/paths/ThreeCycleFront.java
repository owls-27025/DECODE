package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;

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
    public double defaultVelocity() {
        return 1100;
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
        return "Three Cycle (Front)";
    }

    @Override
    public Action build(MecanumDrive drive, RRActions rractions, Telemetry telemetry) {
        Pose2d initialPose = getInitialPose();

        Robot.Globals.back = false;

        if (alliance == Robot.Globals.Alliances.RED) {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(-35, 35, Math.toRadians(140)), Math.toRadians(140));

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(-10, 16));

            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(-10, 24))
                    .strafeTo(new Vector2d(-10, 40), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                            return 5;
                        }
                    });

            TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(12.5, 16));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(12.5, 24))
                    .strafeTo(new Vector2d(12.5, 40), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                            return 5;
                        }
                    });

            TrajectoryActionBuilder goToIntakeThree = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(34.6, 25));

            TrajectoryActionBuilder intakeThree = goToIntakeThree.endTrajectory().fresh()
                    .strafeTo(new Vector2d(34.6, 50));

            TrajectoryActionBuilder leave = goToShoot.endTrajectory().fresh()
                    .strafeTo(new Vector2d(-55, 14));


            return new SequentialAction(
                    rractions.stop(),
                    new ParallelAction(
                            goToShoot.build()
                    ),
                    rractions.shoot(3, 1100),
                    goToIntakeOne.build(),
                    new ParallelAction(
                            intakeOne.build(),
                            rractions.intake()
                    ),
                    goToShoot.build(),
                    rractions.shoot(3, 1100),
                    goToIntakeTwo.build(),
                    new ParallelAction(
                            intakeTwo.build(),
                            rractions.intake()
                    )
            );
        } else {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(-35, -35, Math.toRadians(225)), Math.toRadians(225));

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(-90))
                    .strafeTo(new Vector2d(-10, -16));

            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(-10, -24))
                    .strafeTo(new Vector2d(-10, -40), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                            return 5;
                        }
                    });

            TrajectoryActionBuilder hitGate = intakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(0, -50))
                    .strafeTo(new Vector2d(0, -55), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                            return 6;
                        }
                    });

            TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(-90))
                    .strafeTo(new Vector2d(12.5, -16));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(12.5, -24))
                    .strafeTo(new Vector2d(12.5, -40), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                            return 5;
                        }
                    });

            TrajectoryActionBuilder goToIntakeThree = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(-90))
                    .strafeTo(new Vector2d(34.6, -25));

            TrajectoryActionBuilder intakeThree = goToIntakeThree.endTrajectory().fresh()
                    .strafeTo(new Vector2d(34.6, -40), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                            return 5;
                        }
                    });

            TrajectoryActionBuilder leave = goToShoot.endTrajectory().fresh()
                    .strafeTo(new Vector2d(-55, -14));

            if (!Robot.Globals.gate) {
                return new SequentialAction(
                        rractions.stop(),
                        goToShoot.build(),
                        rractions.shoot(3, 1150),
                        goToIntakeOne.build(),
                        new ParallelAction(
                                intakeOne.build(),
                                rractions.intake()
                        ),
                        goToShoot.build(),
                        rractions.shoot(3, 1150),
                        goToIntakeTwo.build(),
                        new ParallelAction(
                                intakeTwo.build(),
                                rractions.intake()
                        )
                );
            } else {
                return new SequentialAction(
                        rractions.stop(),
                        new ParallelAction(
                                goToShoot.build()
//                                rractions.getMotif()
                        ),
                        rractions.shoot(3, 1150),
                        goToIntakeOne.build(),
                        new ParallelAction(
                                intakeOne.build(),
                                rractions.intake()
                        ),
                        hitGate.build(),
                        goToShoot.build(),
                        rractions.shoot(3, 1150),
                        goToIntakeTwo.build(),
                        new ParallelAction(
                                intakeTwo.build(),
                                rractions.intake()
                        )
                );
            }
        }
    }
}
