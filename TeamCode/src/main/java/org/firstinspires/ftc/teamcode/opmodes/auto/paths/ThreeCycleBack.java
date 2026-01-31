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

public class ThreeCycleBack implements AutoPath {
    private final Robot.Globals.Alliances alliance;

    public ThreeCycleBack(Robot.Globals.Alliances alliance) {
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Robot.Globals.Alliances.RED) {
            return new Pose2d(61.25, 11.5, Math.toRadians(180));
        } else {
            return new Pose2d(61.25, -11.5, Math.toRadians(180));
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
                    .splineToLinearHeading(new Pose2d(54, 15, Math.toRadians(157)), Math.toRadians(157));

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .strafeTo(new Vector2d(35, 25))
                    .turnTo(Math.toRadians(90));

            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(35, 50), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder goToShootTwo = intakeOne.endTrajectory().fresh()
                    .setTangent(Math.toRadians(270))
                    .splineToLinearHeading(new Pose2d(55, 16, Math.toRadians(155)), Math.toRadians(270));

            TrajectoryActionBuilder goToIntakeTwo = goToShootTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(11.7, 25))
                    .turnTo(Math.toRadians(90));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(11.7, 50), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder goToShootThree = intakeTwo.endTrajectory().fresh()
                    .setTangent(Math.toRadians(270))
                    .splineToLinearHeading(new Pose2d(55, 16, Math.toRadians(155)), Math.toRadians(270));

            TrajectoryActionBuilder leave = goToShootThree.endTrajectory().fresh()
                    .strafeTo(new Vector2d(45, 20));

            return new SequentialAction(
                    rractions.stop(),
                    goToShoot.build(),
                    rractions.shoot(3, 1450, Robot.Globals.Colors.GPP, 1),
                    goToIntakeOne.build(),
                    new ParallelAction(
                            intakeOne.build(),
                            rractions.intake()
                    ),
                    goToShootTwo.build(),
                    rractions.shoot(3, 1450, Robot.Globals.Colors.GPP, 1),
                    goToIntakeTwo.build(),
                    new ParallelAction(
                            intakeTwo.build(),
                            rractions.intake()
                    ),
                    goToShootThree.build(),
                    rractions.shoot(3, 1450, Robot.Globals.Colors.PGP, 1),
                    leave.build()
            );


        } else {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                    .splineToLinearHeading(new Pose2d(54, -15, Math.toRadians(-157)), Math.toRadians(-157));

            TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                    .strafeTo(new Vector2d(35, -25))
                    .turnTo(Math.toRadians(-90));

            TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                    .strafeTo(new Vector2d(35, -50), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder goToShootTwo = intakeOne.endTrajectory().fresh()
                    .setTangent(Math.toRadians(-270))
                    .splineToLinearHeading(new Pose2d(55, -16, Math.toRadians(-155)), Math.toRadians(-270));

            TrajectoryActionBuilder goToIntakeTwo = goToShootTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(11.7, -25))
                    .turnTo(Math.toRadians(-90));

            TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                    .strafeTo(new Vector2d(11.7, -50), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder goToShootThree = intakeTwo.endTrajectory().fresh()
                    .setTangent(Math.toRadians(-270))
                    .splineToLinearHeading(new Pose2d(55, -16, Math.toRadians(-155)), Math.toRadians(-270));

            TrajectoryActionBuilder leave = goToShootThree.endTrajectory().fresh()
                    .strafeTo(new Vector2d(45, -20));

            return new SequentialAction(
                    rractions.stop(),
                    goToShoot.build(),
                    rractions.shoot(3, 1450, Robot.Globals.Colors.GPP, 1),
                    goToIntakeOne.build(),
                    new ParallelAction(
                            intakeOne.build(),
                            rractions.intake()
                    ),
                    goToShootTwo.build(),
                    rractions.shoot(3, 1450, Robot.Globals.Colors.GPP, 1),
                    goToIntakeTwo.build(),
                    new ParallelAction(
                            intakeTwo.build(),
                            rractions.intake()
                    ),
                    goToShootThree.build(),
                    rractions.shoot(3, 1450, Robot.Globals.Colors.PGP, 1),
                    leave.build()
            );
        }
    }
}