package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import org.jetbrains.annotations.NotNull;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-50, 50, Math.toRadians(125)))
                // prep shoot
                .strafeTo(new Vector2d(-35, 35))

//                // read motif
//                .turnTo(Math.toRadians(135), new TurnConstraints(1, -1, 1))
//                .turnTo(Math.toRadians(225))

                // shoot preload
                .waitSeconds(2.5)

                .splineToLinearHeading(new Pose2d(-52, 10, Math.toRadians(90)), Math.toRadians(90))

                // intake artifacts from first spike mark
//                .splineToLinearHeading(new Pose2d(-11.5, -10, Math.toRadians(-90)), Math.toRadians(-90))
//                .turnTo(Math.toRadians(-90))
//                .lineToY(-45, (pose2dDual, posePath, v) -> 10)
//                .splineToLinearHeading(new Pose2d(-35, -35, Math.toRadians(235)), Math.toRadians(235))
//
//                // shoot three artifacts
//                .waitSeconds(2.5)
//
//                // intake artifacts from second spike mark
//                .splineToLinearHeading(new Pose2d(12, -30, Math.toRadians(-90)), Math.toRadians(-90))
//                .turnTo(Math.toRadians(-90))
//                .lineToY(-45, (pose2dDual, posePath, v) -> 10)
//                .splineToLinearHeading(new Pose2d(-35, -35, Math.toRadians(235)), Math.toRadians(235))
//
//                // shoot three artifacts
//                .waitSeconds(2.5)
//
//                // intake artifacts from third spike mark
//                .splineToLinearHeading(new Pose2d(35, -30, Math.toRadians(-90)), Math.toRadians(-90))
//                .turnTo(Math.toRadians(-90))
//                .lineToY(-45, (pose2dDual, posePath, v) -> 10)
////                .splineToLinearHeading(new Pose2d(-35, 35, Math.toRadians(135)), Math.toRadians(135))
////
////                // shoot three artifacts
////                .waitSeconds(2.5)

                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}