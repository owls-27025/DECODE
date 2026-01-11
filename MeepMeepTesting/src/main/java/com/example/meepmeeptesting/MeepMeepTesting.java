package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Arclength;
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
                .build();

        TrajectoryActionBuilder goToShoot = myBot.getDrive().actionBuilder(new Pose2d(-55, 14, Math.toRadians(0)))
                .splineToLinearHeading(new Pose2d(-40, 40, Math.toRadians(140)), Math.toRadians(140));

        TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                .turnTo(Math.toRadians(90))
                .strafeTo(new Vector2d(-10, 16));

        TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                .strafeTo(new Vector2d(-10, 24))
                .strafeTo(new Vector2d(-10, 40));

        TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                .turnTo(Math.toRadians(90))
                .strafeTo(new Vector2d(12.5, 16));

        TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                .strafeTo(new Vector2d(12.5, 24))
                .strafeTo(new Vector2d(12.5, 40));

        TrajectoryActionBuilder leave = goToShoot.endTrajectory().fresh()
                .strafeTo(new Vector2d(-55, 14));

        TrajectoryActionBuilder hitGate = intakeOne.endTrajectory().fresh()
                .strafeTo(new Vector2d(0, 50))
                .strafeTo(new Vector2d(0, 55));

        myBot.runAction(new SequentialAction(
                goToShoot.build()
        ));


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}