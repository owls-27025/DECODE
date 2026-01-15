package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.auto.RRActions;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.jetbrains.annotations.NotNull;

public class ThreeCycleBack implements AutoPath {
    private final Robot.Globals.Alliances alliance;

    public ThreeCycleBack(Robot.Globals.Alliances alliance) {
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Robot.Globals.Alliances.RED) {
            return new Pose2d(55, 10, Math.toRadians(180));
        } else {
            return new Pose2d(55, -10, Math.toRadians(180));
        }
    }

    @Override
    public double defaultVelocity() {
        return 1550;
    }

    @Override
    public String getName() {
        return "Three Cycle (Back)";
    }

    @Override
    public Action build(MecanumDrive drive, RRActions rractions, Telemetry telemetry) {
        Pose2d initialPose = getInitialPose();

        Robot.Globals.back = true;

        if (alliance == Robot.Globals.Alliances.RED) {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(50, 15, Math.toRadians(150)), Math.toRadians(150));

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(34.6, 25));


            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(34.6, 45), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NotNull Pose2dDual<Arclength> pose2dDual, @NotNull PosePath posePath, double v) {
                            return 5;
                        }
                    });

            TrajectoryActionBuilder shootTwo = intakeOne.endTrajectory().fresh()
                    .splineToLinearHeading(new Pose2d(50, 15, Math.toRadians(150)), Math.toRadians(150));

            TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(90))
                    .strafeTo(new Vector2d(12.5, 25));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(12.5, 45), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                            return 5;
                        }
                    });

            TrajectoryActionBuilder leave = shootTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(45, 20));


            return new SequentialAction(
                    rractions.stop(),
                    new ParallelAction(
                            goToShoot.build(),
                            rractions.getMotif()
                    ),
                    rractions.shoot(3, 1450, Robot.Globals.Colors.GPP, 1.0),
                    goToIntakeOne.build(),
                    new ParallelAction(
                            intakeOne.build(),
                            rractions.intake()
                    ),
                    shootTwo.build(),
                    rractions.shoot(3, 1450, Robot.Globals.Colors.GPP, 1.0)
            );


        } else {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(51, -15, Math.toRadians(200)), Math.toRadians(200));

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(270))
                    .strafeTo(new Vector2d(34.6, -25));


            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(34.6, -45), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NotNull Pose2dDual<Arclength> pose2dDual, @NotNull PosePath posePath, double v) {
                            return 5;
                        }
                    });

            TrajectoryActionBuilder shootTwo = intakeOne.endTrajectory().fresh()
                    .splineToLinearHeading(new Pose2d(51, -15, Math.toRadians(200)), Math.toRadians(200));

            TrajectoryActionBuilder leave = shootTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(45, -20));

            TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(270))
                    .strafeTo(new Vector2d(12.5, -25));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(12.5, -45), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                            return 5;
                        }
                    });

            TrajectoryActionBuilder goToIntakeThree = goToShoot.endTrajectory().fresh()
                    .turnTo(Math.toRadians(270))
                    .strafeTo(new Vector2d(34.6, -25));

            TrajectoryActionBuilder intakeThree = goToIntakeThree.endTrajectory().fresh()
                    .strafeTo(new Vector2d(34.6, -40), new VelConstraint() {
                        @Override
                        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                            return 5;
                        }
                    });


            return new SequentialAction(
                    rractions.stop(),
                    new ParallelAction(
                            goToShoot.build(),
                            rractions.getMotif()
                    ),
                    rractions.shoot(3, 1450, Robot.Globals.Colors.GPP, 1.0),
//                    goToIntakeOne.build(),
//                    new ParallelAction(
//                            intakeOne.build(),
//                            rractions.intake()
//                    ),
//                    shootTwo.build(),
//                    rractions.shoot(3, 1450, Robot.Globals.Colors.GPP, 1.0),
                    leave.build()
            );
        }
    }
}
