package org.firstinspires.ftc.teamcode.opmodes.auto;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;

import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

public class AutoPath {
    private MecanumDrive drive;
    private RRActions actions;

    public AutoPath(RRActions actions) {
        this.actions = actions;
    }

    private TrajectoryActionBuilder action(
            Map<String, Function<TrajectoryActionBuilder, TrajectoryActionBuilder>> map,
            String name,
            TrajectoryActionBuilder last
    ) {
        Function<TrajectoryActionBuilder, TrajectoryActionBuilder> fn = map.get(name);
        if (fn == null) {
            throw new IllegalArgumentException("Missing action: " + name);
        }

        return fn.apply(last);
    }

    // ===================== FRONT =====================
    // FRONT ACTIONS LIBRARY
    public Map<String, Function<TrajectoryActionBuilder, TrajectoryActionBuilder>> frontActions;

    // FRONT PATHS
    public Path frontThree;

    public Path frontSix;

    public Path frontSixGate;

    // ===================== BACK =====================
    // BACK ACTIONS LIBRARY
    public Map<String, Function<TrajectoryActionBuilder, TrajectoryActionBuilder>> backActions;

    // BACK PATHS
    public Path backThree;

    // ===================== BUILD =====================
    public Action build(AutoParams params, Robot robot, HardwareMap hardwareMap) {
        Path path = null;

        Pose2d initialPose;
        switch (params.strategy) {
            case FRONT:
                initialPose = new Pose2d(-50, 50, Math.toRadians(125));
                break;
            case BACK:
                initialPose = new Pose2d(61.25, 11.5, Math.toRadians(180));
                break;
            default:
                throw new IllegalArgumentException("Unknown strategy: " + params.strategy);
        }

        drive = new MecanumDrive(robot, hardwareMap, initialPose);
        actions.setDrive(drive);
        drive.localizer.setPose(initialPose);
        drive.localizer.update();

        frontActions = Map.of(
                "SHOOT", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .setTangent(Math.toRadians(-45))
                                .splineToLinearHeading(
                                        new Pose2d(-35, 35, Math.toRadians(-225)),
                                        Math.toRadians(-45)
                                ),
                "SHOOT_LEAVE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .setTangent(Math.toRadians(270))
                                .splineToLinearHeading(new Pose2d(-57, 20, Math.toRadians(99)), Math.toRadians(270)),
                "SPIKE_ONE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .setTangent(Math.toRadians(270))
                                .splineToLinearHeading(new Pose2d(-11.5, 25, Math.toRadians(90)), Math.toRadians(90)),
                "INTAKE_ONE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .lineToY(50, (pose2dDual, posePath, v) -> 9),
                "GATE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .turnTo(Math.toRadians(180))
                                .strafeTo(new Vector2d(-2, 50))
                                .strafeTo(new Vector2d(-2, 58))
        );

        backActions = Map.of(
                "SHOOT", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .splineToLinearHeading(
                                        new Pose2d(54, 15, Math.toRadians(157)),
                                        Math.toRadians(157)
                                ),
                "LEAVE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .strafeTo(new Vector2d(54, 20))
                                .turnTo(Math.toRadians(270))

        );

        Action ft1 = action(frontActions, "SHOOT_LEAVE", drive.actionBuilder(drive.localizer.getPose())).build();

         frontThree = new Path(
                new Pose2d(-50, 50, Math.toRadians(125)),
                new SequentialAction(
                        actions.stop(),
                        ft1,
                        actions.shoot(3, 1050, Robot.Globals.Colors.GPP, 0)
                )
        );

         TrajectoryActionBuilder fs1 = action(frontActions, "SHOOT", drive.actionBuilder(drive.localizer.getPose()));
         TrajectoryActionBuilder fs2 = action(frontActions, "SPIKE_ONE", fs1.endTrajectory().fresh());
         TrajectoryActionBuilder fs3 = action(frontActions, "INTAKE_ONE", fs2.endTrajectory().fresh());
         TrajectoryActionBuilder fs4 = action(frontActions, "SHOOT_LEAVE", fs3.endTrajectory().fresh());

         frontSix = new Path(
                 new Pose2d(-50, 50, Math.toRadians(125)),
                 new SequentialAction(
                         actions.stop(),
                         fs1.build(),
                         actions.shoot(3, 1050, Robot.Globals.Colors.GPP, 0),
                         fs2.build(),
                         new ParallelAction(
                                 fs3.build(),
                                 actions.intake()
                         ),
                         fs4.build(),
                         actions.shoot(3, 1050, Robot.Globals.Colors.PPG, 0)
                 )
         );

         TrajectoryActionBuilder fsg1 = action(frontActions, "SHOOT", drive.actionBuilder(drive.localizer.getPose()));
         TrajectoryActionBuilder fsg2 = action(frontActions, "SPIKE_ONE", fsg1.endTrajectory().fresh());
         TrajectoryActionBuilder fsg3 = action(frontActions, "INTAKE_ONE", fsg2.endTrajectory().fresh());
         TrajectoryActionBuilder fsg4 = action(frontActions, "GATE", fsg3.endTrajectory().fresh());
         TrajectoryActionBuilder fsg5 = action(frontActions, "SHOOT_LEAVE", fsg4.endTrajectory().fresh());

        frontSixGate = new Path(
                new Pose2d(-50, 50, Math.toRadians(125)),
                new SequentialAction(
                        actions.stop(),
                        fsg1.build(),
                        actions.shoot(3, 1050, Robot.Globals.Colors.GPP, 0),
                        fsg2.build(),
                        new ParallelAction(
                                fsg3.build(),
                                actions.intake()
                        ),
                        fsg4.build(),
                        fsg5.build(),
                        actions.shoot(3, 1100, Robot.Globals.Colors.PPG, 0)
                )
        );

        TrajectoryActionBuilder bt1 = action(backActions, "SHOOT", drive.actionBuilder(drive.localizer.getPose()));
        TrajectoryActionBuilder bt2 = action(backActions, "LEAVE", bt1.endTrajectory().fresh());

         backThree = new Path(
                new Pose2d(61.25, 11.5, Math.toRadians(180)),
                new SequentialAction(
                        actions.stop(),
                        bt1.build(),
                        actions.shoot(3, 1450, Robot.Globals.Colors.GPP, 1),
                        bt2.build()
                )
        );

        switch (params.strategy) {
            case FRONT:
                switch (params.spikes) {
                    case 0:
                        path = frontThree;
                        break;
                    case 1:
                        if (!params.gate) {
                            path = frontSix;
                            break;
                        } else {
                            path = frontSixGate;
                            break;
                        }
                }
                break;
            case BACK:
                switch (params.spikes) {
                    case 0:
                        path = backThree;
                        break;
                }
        }

        return path.action;
    }

    private class Path {
        public Pose2d initialPose;
        public SequentialAction action;

        public Path(Pose2d initialPose, SequentialAction action) {
            this.initialPose = initialPose;
            this.action = action;
        }
    }
}