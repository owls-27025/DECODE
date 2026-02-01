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
    public double defaultVelocity() {
        return 1100;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Robot.Globals.Alliances.RED) {
            return new Pose2d(-50, 50, Math.toRadians(125));
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
        drive.localizer.setPose(initialPose);

        Robot.Globals.back = false;

        if (alliance == Robot.Globals.Alliances.RED) {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(drive.localizer.getPose())
                    .setTangent(Math.toRadians(-45))
                    .splineToLinearHeading(new Pose2d(-35, 35, Math.toRadians(-225)), Math.toRadians(-45));

            TrajectoryActionBuilder goToIntakeOne = drive.actionBuilder(drive.localizer.getPose())
                    .setTangent(Math.toRadians(270))
                    .splineToLinearHeading(new Pose2d(-11.5, 25, Math.toRadians(90)), Math.toRadians(90));

            TrajectoryActionBuilder intakeOne = drive.actionBuilder(drive.localizer.getPose())
                    .strafeTo(new Vector2d(-11.5, 45), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder goToShootTwo = drive.actionBuilder(drive.localizer.getPose())
                    .setTangent(Math.toRadians(-45))
                    .splineToLinearHeading(new Pose2d(-35, 35, Math.toRadians(-225)), Math.toRadians(-45));

            TrajectoryActionBuilder goToIntakeTwo = drive.actionBuilder(drive.localizer.getPose())
                    .setTangent(Math.toRadians(270))
                    .splineToLinearHeading(new Pose2d(11.5, 25, Math.toRadians(90)), Math.toRadians(90));

            TrajectoryActionBuilder intakeTwo = drive.actionBuilder(drive.localizer.getPose())
                    .strafeTo(new Vector2d(11.5, 45), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder goToLeaveShoot = drive.actionBuilder(drive.localizer.getPose())
                    .setTangent(Math.toRadians(-90))
                    .splineToLinearHeading(new Pose2d(-57, 20, Math.toRadians(-270)), Math.toRadians(-90));

            return new SequentialAction(
//                    rractions.stop()
//                    new ParallelAction(
//                            goToShoot.build(),
//                        ),
//                    rractions.shoot(3, 1050, Robot.Globals.Colors.GPP),
//                    goToIntakeOne.build(),
//                    new ParallelAction(
//                            intakeOne.build(),
//                            rractions.intake()
//                    ),
//                    rractions.stop(),
//                    goToShootTwo.build(),
//                    rractions.shoot(3, 1050, Robot.Globals.Colors.PPG),
//                    goToIntakeTwo.build(),
//                    new ParallelAction(
//                            intakeTwo.build(),
//                            rractions.intake()
//                    ),
//                    rractions.stop(),
                    goToLeaveShoot.build(),
                    rractions.shoot(3, 1000, Robot.Globals.Colors.GPP)
            );
        } else {
            TrajectoryActionBuilder goToShoot = drive.actionBuilder(drive.localizer.getPose())
                    .setTangent(Math.toRadians(45))
                    .splineToLinearHeading(new Pose2d(-35, -35, Math.toRadians(225)), Math.toRadians(45));

            TrajectoryActionBuilder goToIntakeOne = drive.actionBuilder(drive.localizer.getPose())
                    .setTangent(Math.toRadians(-270))
                    .splineToLinearHeading(new Pose2d(-11.5, -25, Math.toRadians(-90)), Math.toRadians(-90));

            TrajectoryActionBuilder intakeOne = drive.actionBuilder(drive.localizer.getPose())
                    .strafeTo(new Vector2d(-11.5, -45), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder goToIntakeTwo = drive.actionBuilder(drive.localizer.getPose())
                    .setTangent(Math.toRadians(-270))
                    .splineToLinearHeading(new Pose2d(11.5, -25, Math.toRadians(-90)), Math.toRadians(-90));

            TrajectoryActionBuilder intakeTwo = drive.actionBuilder(drive.localizer.getPose())
                    .strafeTo(new Vector2d(11.5, -45), (pose2dDual, posePath, v) -> 9);

            TrajectoryActionBuilder goToLeaveShoot = drive.actionBuilder(drive.localizer.getPose())
                    .setTangent(Math.toRadians(90))
                    .splineToLinearHeading(new Pose2d(-57, -15, Math.toRadians(270)), Math.toRadians(90));

            return new SequentialAction(
                    rractions.stop(),
                    new ParallelAction(
//                            goToShoot.build(),
//                            rractions.getMotif()
                    ),
//                    rractions.shoot(3, 1050, Robot.Globals.Colors.GPP),
//                    goToIntakeOne.build(),
//                    new ParallelAction(
//                            intakeOne.build(),
//                            rractions.intake()
//                    ),
//                    rractions.stop(),
//                    goToShoot.build(),
//                    rractions.shoot(3, 1050, Robot.Globals.Colors.PPG),
//                    goToIntakeTwo.build(),
//                    new ParallelAction(
//                            intakeTwo.build(),
//                            rractions.intake()
//                    ),
//                    rractions.stop(),
                    goToLeaveShoot.build(),
                    rractions.shoot(3, 1000, Robot.Globals.Colors.GPP)
            );
        }
    }
}
