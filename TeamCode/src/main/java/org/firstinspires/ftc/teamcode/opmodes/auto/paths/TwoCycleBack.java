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

public class TwoCycleBack implements AutoPath {
    private final Robot.Globals.Alliances alliance;

    public TwoCycleBack(Robot.Globals.Alliances alliance) {
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Robot.Globals.Alliances.RED) {
            return new Pose2d(60, 11.7, Math.toRadians(180));
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
                    .splineToLinearHeading(new Pose2d(50, 15, Math.toRadians(155)), Math.toRadians(155));

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .setTangent(Math.toRadians(90))
                    .splineToLinearHeading(new Pose2d(35.7, 25, Math.toRadians(90)), Math.toRadians(90));


            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(35.7, 50), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder shootTwo = intakeOne.endTrajectory().fresh()
                    .setTangent(Math.toRadians(270))
                    .splineToLinearHeading(new Pose2d(55, 16, Math.toRadians(155)), Math.toRadians(270));

            TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                    .setTangent(Math.toRadians(90))
                    .splineToLinearHeading(new Pose2d(12.5, 25, Math.toRadians(90)), Math.toRadians(135));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(12.5, 50), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder leave = shootTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(45, 20));

            return new SequentialAction(
                    rractions.stop(),
                    goToShoot.build(),
                    rractions.shoot(3, 1550),
                    goToIntakeOne.build(),
                    new ParallelAction(
                            intakeOne.build(),
                            rractions.intake()
                    ),
                    shootTwo.build(),
                    rractions.shoot(3, 1550),
                    leave.build()
            );


        } else {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(50, -15, Math.toRadians(-155)), Math.toRadians(-155));

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .setTangent(Math.toRadians(-90))
                    .splineToLinearHeading(new Pose2d(35.7, -25, Math.toRadians(-90)), Math.toRadians(-90));


            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(35.7, -50), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder shootTwo = intakeOne.endTrajectory().fresh()
                    .setTangent(Math.toRadians(-270))
                    .splineToLinearHeading(new Pose2d(55, -16, Math.toRadians(-155)), Math.toRadians(-270));

            TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                    .setTangent(Math.toRadians(-90))
                    .splineToLinearHeading(new Pose2d(12.5, -25, Math.toRadians(-90)), Math.toRadians(-135));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(12.5, -50), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder leave = shootTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(45, -20));


            return new SequentialAction(
                    rractions.stop(),
                    goToShoot.build(),
                    rractions.shoot(3, 1550),
                    goToIntakeOne.build(),
                    new ParallelAction(
                            intakeOne.build(),
                            rractions.intake()
                    ),
                    shootTwo.build(),
                    rractions.shoot(3, 1550),
                    leave.build()
            );
        }
    }
}