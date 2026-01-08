package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
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

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-50, 50, Math.toRadians(135)))
                .splineToLinearHeading(new Pose2d(-35, 35, Math.toRadians(135)), Math.toRadians(135))
                                .waitSeconds(5)
                .turnTo(Math.toRadians(90))
                .strafeTo(new Vector2d(-10, 20))
                .strafeTo(new Vector2d(-10, -40))

//                .turnTo(Math.toRadians(210))
//                .waitSeconds(5)
//                .turnTo(Math.toRadians(270))
//                .strafeTo(new Vector2d(34.6, -25))
//                .strafeTo(new Vector2d(34.6, -45), new VelConstraint() {
//                    @Override
//                    public double maxRobotVel(@NotNull Pose2dDual<Arclength> pose2dDual, @NotNull PosePath posePath, double v) {
//                        return 6;
//                    }
//                })
//                .turnTo(Math.toRadians(210))
//                .strafeTo(new Vector2d(55, -10))
//                .waitSeconds(5)
                // three cycle back ^^
                // three cycle front vv
//                .strafeTo(new Vector2d(-10, -50))
//                .strafeTo(new Vector2d(-35, -35))
//                .turnTo(Math.toRadians(-135))
//                .waitSeconds(5)
//                .turnTo(Math.toRadians(-90))
//                .strafeTo(new Vector2d(11.5, -25))
//                .strafeTo(new Vector2d(11.5, -50))
//                .strafeTo(new Vector2d(-35, -35))
//                .turnTo(Math.toRadians(-135))
//                .waitSeconds(5)
//                .turnTo(Math.toRadians(-90))
//                .strafeTo(new Vector2d(34.6, -25))
//                .strafeTo(new Vector2d(34.6, -50))
//                .strafeTo(new Vector2d(-35, -35))
//                .turnTo(Math.toRadians(-135))
//                .waitSeconds(5)
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}