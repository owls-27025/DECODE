package com.example.meepmeeptesting;

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
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import org.jetbrains.annotations.NotNull;

import java.util.Vector;


public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setStartPose(new Pose2d(60, 11.7, Math.toRadians(180)))
                .build();

        TrajectoryActionBuilder goToShoot = myBot.getDrive().actionBuilder(myBot.getDrive().getPoseEstimate())
                .splineToLinearHeading(new Pose2d(50, 15, Math.toRadians(155)), Math.toRadians(155));

        TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
//                .turnTo(Math.toRadians(90))
//                .setTangent(Math.toRadians(275))
//                .splineToSplineHeading(new Pose2d(-12.5, 35, Math.toRadians(90)), Math.toRadians(50));
                .turnTo(Math.toRadians(90))
                .strafeTo(new Vector2d(34.6, 25));

        TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                .strafeTo(new Vector2d(-11.5, 35))
                .strafeTo(new Vector2d(-11.5, 50), new VelConstraint() {
                    @Override
                    public double maxRobotVel(@NotNull Pose2dDual<Arclength> pose2dDual, @NotNull PosePath posePath, double v) {
                        return 5;
                    }
                });

        TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                .turnTo(Math.toRadians(90))
                .setTangent(Math.toRadians(275))
                .splineToLinearHeading(new Pose2d(11.5, 35, Math.toRadians(90)), Math.toRadians(70));

        TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                .strafeTo(new Vector2d(11.5, 35))
                .strafeTo(new Vector2d(11.5, 50), new VelConstraint() {
                    @Override
                    public double maxRobotVel(@NotNull Pose2dDual<Arclength> pose2dDual, @NotNull PosePath posePath, double v) {
                        return 5;
                    }
                });

        myBot.runAction(new SequentialAction(
                goToShoot.build(),
                goToIntakeOne.build(),
                intakeOne.build(),
                goToShoot.build(),
                goToIntakeTwo.build(),
                intakeTwo.build()
        ));


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}