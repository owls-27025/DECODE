package com.example.meepmeeptesting;

import androidx.annotation.NonNull;

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
                .build();

        Pose2d initialPose = new Pose2d(-50, 50, Math.toRadians(123));

        TrajectoryActionBuilder goToShoot = myBot.getDrive().actionBuilder(initialPose)
                .splineToLinearHeading(new Pose2d(50, 15, Math.toRadians(150)), Math.toRadians(150));

        TrajectoryActionBuilder goToIntakeOne = goToShoot.endTrajectory().fresh()
                .turnTo(Math.toRadians(90))
                .strafeTo(new Vector2d(34.6, 25));


        TrajectoryActionBuilder intakeOne = goToIntakeOne.endTrajectory().fresh()
                .strafeTo(new Vector2d(34.6, 45), new VelConstraint() {
                    @Override
                    public double maxRobotVel(@NotNull Pose2dDual<Arclength> pose2dDual, @NotNull PosePath posePath, double v) {
                        return 5;
                    }
                });

        TrajectoryActionBuilder shootTwo = intakeOne.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(50, 15, Math.toRadians(150)), Math.toRadians(150));

        TrajectoryActionBuilder goToIntakeTwo = goToShoot.endTrajectory().fresh()
                .turnTo(Math.toRadians(90))
                .strafeTo(new Vector2d(12.5, 25));

        TrajectoryActionBuilder intakeTwo = goToIntakeTwo.endTrajectory().fresh()
                .strafeTo(new Vector2d(12.5, 45), new VelConstraint() {
                    @Override
                    public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                        return 5;
                    }
                });

        TrajectoryActionBuilder leave = goToShoot.endTrajectory().fresh()
                .strafeTo(new Vector2d(-55, 14));

        TrajectoryActionBuilder hitGate = intakeOne.endTrajectory().fresh()
                .strafeTo(new Vector2d(0, 50))
                .strafeTo(new Vector2d(0, 55), new VelConstraint() {
                    @Override
                    public double maxRobotVel(@NotNull Pose2dDual<Arclength> pose2dDual, @NotNull PosePath posePath, double v) {
                        return 6;
                    }
                });

        myBot.runAction(new SequentialAction(
                goToShoot.build(),
                goToIntakeOne.build(),
                intakeOne.build(),
                goToIntakeTwo.build(),
                intakeTwo.build()
        ));
