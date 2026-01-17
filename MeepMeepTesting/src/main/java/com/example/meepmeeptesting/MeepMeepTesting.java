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
                .setStartPose(new Pose2d(-50, -50, Math.toRadians(225)))
                .build();

//        TrajectoryActionBuilder goToShoot = myBot.getDrive().actionBuilder(myBot.getPose())
//                .splineToLinearHeading(new Pose2d(-35, 35, Math.toRadians(135)), Math.toRadians(135));
//
//        TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
//                .turnTo(Math.toRadians(90))
//                .strafeTo(new Vector2d(-11.5, 20));
//
//        TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
//                .strafeTo(new Vector2d(-11.5, 35))
//                .strafeTo(new Vector2d(-11.5, 50), (pose2dDual, posePath, v) -> 9);
//
//        TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
//                .turnTo(Math.toRadians(90))
//                .strafeTo(new Vector2d(11.5, 20));
//
//        TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
//                .strafeTo(new Vector2d(11.5, 35))
//                .strafeTo(new Vector2d(11.5, 50), (pose2dDual, posePath, v) -> 9);
//
//        TrajectoryActionBuilder goToLeaveShoot = intakeTwo.endTrajectory().fresh()
//                .splineToLinearHeading(new Pose2d(-57, 15, Math.toRadians(-270)), Math.toRadians(-270));

        TrajectoryActionBuilder goToShoot = myBot.getDrive().actionBuilder(myBot.getPose())
                .splineToLinearHeading(new Pose2d(-35, -35, Math.toRadians(225)), Math.toRadians(225));

        TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                .turnTo(Math.toRadians(-90))
                .strafeTo(new Vector2d(-10, -16));

        TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                .strafeTo(new Vector2d(-10, -24))
                .strafeTo(new Vector2d(-10, -40), (pose2dDual, posePath, v) -> 9);

        TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                .turnTo(Math.toRadians(-90))
                .strafeTo(new Vector2d(12.5, -16));

        TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                .strafeTo(new Vector2d(12.5, -24))
                .strafeTo(new Vector2d(12.5, -40), (pose2dDual, posePath, v) -> 9);

        TrajectoryActionBuilder goToLeaveShoot = intakeTwo.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(-57, -15, Math.toRadians(270)), Math.toRadians(270));

        myBot.runAction(new SequentialAction(
                goToShoot.build(),
                goToIntakeOne.build(),
                intakeOne.build(),
                goToShoot.build(),
                goToIntakeTwo.build(),
                intakeTwo.build(),
                goToLeaveShoot.build()
        ));


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}