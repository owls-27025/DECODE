package meepmeeptesting.paths;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import meepmeeptesting.MeepMeepTesting;
import java.lang.Math;

public class ThreeCycleBack implements MeepMeepTesting.MeepMeepPath {

    public String getName() {
        return "Three Cycle Back";
    }

    public Pose2d getInitialPose(MeepMeepTesting.Alliance alliance) {
        return alliance == MeepMeepTesting.Alliance.BLUE ? new Pose2d(61.25, -11.5, Math.toRadians(180)) : new Pose2d(61.25, 11.5, Math.toRadians(180));
    }

    public Action buildRed(RoadRunnerBotEntity bot, Pose2d pose) {
        DriveShim drive = bot.getDrive();
        Pose2d initialPose = getInitialPose(MeepMeepTesting.Alliance.RED);

        TrajectoryActionBuilder goToShoot = drive.actionBuilder(initialPose)
                .splineToLinearHeading(new Pose2d(54, 15, Math.toRadians(157)), Math.toRadians(157));

        TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(35.7, 25, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                .strafeTo(new Vector2d(35.7, 50), (pose2dDual, posePath, v) -> 9);

        TrajectoryActionBuilder goToShootTwo = intakeOne.endTrajectory().fresh()
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(55, 16, Math.toRadians(155)), Math.toRadians(270));

        TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(12.5, 25, Math.toRadians(90)), Math.toRadians(135));

        TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                .strafeTo(new Vector2d(12.5, 50), (pose2dDual, posePath, v) -> 9);

        TrajectoryActionBuilder goToShootThree = intakeTwo.endTrajectory().fresh()
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(55, 16, Math.toRadians(155)), Math.toRadians(270));

        TrajectoryActionBuilder leave = goToShootThree.endTrajectory().fresh()
                .strafeTo(new Vector2d(45, 20));


        return new SequentialAction(
                goToShoot.build(),
                goToIntakeOne.build(),
                intakeOne.build(),
                goToShootTwo.build(),
                goToIntakeTwo.build(),
                intakeTwo.build(),
                goToShootThree.build(),
                leave.build()
        );
    }

    public Action buildBlue(RoadRunnerBotEntity bot, Pose2d pose) {
        TrajectoryActionBuilder goToShoot = bot.getDrive().actionBuilder(pose)
                .splineToLinearHeading(new Pose2d(54, -15, Math.toRadians(-157)), Math.toRadians(-157));

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
                goToShoot.build(),
                goToIntakeOne.build(),
                intakeOne.build(),
                shootTwo.build(),
                goToIntakeTwo.build(),
                intakeTwo.build(),
                shootTwo.build(),
                leave.build()
        );
    }
}
