package meepmeeptesting.paths;

import com.acmerobotics.roadrunner.*;
import meepmeeptesting.MeepMeepTesting;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import java.lang.Math;

public class ThreeCycleFront implements MeepMeepTesting.MeepMeepPath {

    public String getName() {
        return "Three Cycle Front";
    }

    public Pose2d getInitialPose(MeepMeepTesting.Alliance alliance) {
        return alliance == MeepMeepTesting.Alliance.BLUE ? new Pose2d(-50, -50, Math.toRadians(235)) : new Pose2d(-50, 50, Math.toRadians(125));
    }

    public Action buildRed(RoadRunnerBotEntity bot, Pose2d pose) {
        TrajectoryActionBuilder goToShoot = bot.getDrive().actionBuilder(pose)
                .setTangent(Math.toRadians(-45))
                .splineToLinearHeading(new Pose2d(-35, 35, Math.toRadians(-225)), Math.toRadians(-45));

        TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(-11.5, 25, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                .strafeTo(new Vector2d(-11.5, 50), (pose2dDual, posePath, v) -> 9);

        TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(12, 25, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                .strafeTo(new Vector2d(12, 50), (pose2dDual, posePath, v) -> 9);

        TrajectoryActionBuilder goToLeaveShoot = intakeTwo.endTrajectory().fresh()
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(-57, 20, Math.toRadians(-270)), Math.toRadians(-90));

        return new SequentialAction(
                goToShoot.build(),
                goToIntakeOne.build(),
                intakeOne.build(),
                goToShoot.build(),
                goToIntakeTwo.build(),
                intakeTwo.build(),
                goToLeaveShoot.build()
        );
    }

    public Action buildBlue(RoadRunnerBotEntity bot, Pose2d pose) {
        TrajectoryActionBuilder goToShoot = bot.getDrive().actionBuilder(pose)
                .setTangent(Math.toRadians(45))
                .splineToLinearHeading(new Pose2d(-35, -35, Math.toRadians(225)), Math.toRadians(45));

        TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                .setTangent(Math.toRadians(-270))
                .splineToLinearHeading(new Pose2d(-11.5, -25, Math.toRadians(-90)), Math.toRadians(-90));

        TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                .strafeTo(new Vector2d(-11.5, -50), (pose2dDual, posePath, v) -> 9);

        TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                .setTangent(Math.toRadians(-270))
                .splineToLinearHeading(new Pose2d(12, -25, Math.toRadians(-90)), Math.toRadians(-90));

        TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                .strafeTo(new Vector2d(12, -50), (pose2dDual, posePath, v) -> 9);

        TrajectoryActionBuilder goToLeaveShoot = intakeTwo.endTrajectory().fresh()
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(-57, -15, Math.toRadians(270)), Math.toRadians(90));

        return new SequentialAction(
                goToShoot.build(),
                goToIntakeOne.build(),
                intakeOne.build(),
                goToShoot.build(),
                goToIntakeTwo.build(),
                intakeTwo.build(),
                goToLeaveShoot.build()
        );
    }
}
