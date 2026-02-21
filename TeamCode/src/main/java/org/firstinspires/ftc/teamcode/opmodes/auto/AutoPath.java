package org.firstinspires.ftc.teamcode.opmodes.auto;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    private TrajectoryActionBuilder makeBuilder(Pose2d start, AutoParams params) {
        if (params.alliance == AutoParams.Alliances.BLUE) {
            return drive.actionBuilder(start,
                    pose -> new Pose2dDual<>(pose.position.x, pose.position.y.unaryMinus(), pose.heading.inverse()), true);
        } else {
            return drive.actionBuilder(start);
        }
    }

    private static Pose2d blueMirror(AutoParams params, Pose2d pose) {
        if (params.alliance == AutoParams.Alliances.BLUE) {
            return new Pose2d(new Vector2d(pose.position.x, -pose.position.y), pose.heading.inverse());
        } else {
            return pose;
        }
    }

    // ===================== FRONT =====================
    // FRONT ACTIONS LIBRARY
    public Map<String, Function<TrajectoryActionBuilder, TrajectoryActionBuilder>> frontActions;

    // FRONT PATHS
    public Path frontThree;

    public Path frontSix;

    public Path frontSixGate;

    public Path frontNine;

    public Path frontNineGate;

    public Path frontTwelve;

    // ===================== BACK =====================
    // BACK ACTIONS LIBRARY
    public Map<String, Function<TrajectoryActionBuilder, TrajectoryActionBuilder>> backActions;

    // BACK PATHS
    public Path backThree;

    public Path backThreeHP;

    public Path backSix;

    // ===================== BUILD =====================
    public Action build(AutoParams params, Robot robot, HardwareMap hardwareMap) {

        Path path = null;
        Pose2d initialPose;
        switch (params.strategy) {
            case FRONT:
                initialPose = blueMirror(params, new Pose2d(-50, 50, Math.toRadians(125)));
                break;
            case BACK:
                initialPose = blueMirror(params, new Pose2d(61.25, 11.5, Math.toRadians(180)));
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
                                        Math.toRadians(-45),
                                        (pose2dDual, posePath, v) -> 90
                                ),
                "SHOOT_LEAVE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .setTangent(Math.toRadians(270))
                                .splineToLinearHeading(new Pose2d(-57, 20, Math.toRadians(99)), Math.toRadians(270),
                                        (pose2dDual, posePath, v) -> 90),
                "SPIKE_ONE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .setTangent(Math.toRadians(270))
                                .splineToLinearHeading(new Pose2d(-11.5, 25, Math.toRadians(90)), Math.toRadians(90),
                                        (pose2dDual, posePath, v) -> 90),
                "INTAKE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .lineToY(50, (pose2dDual, posePath, v) -> 12),
                "SPIKE_TWO", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .setTangent(Math.toRadians(270))
                                .splineToLinearHeading(new Pose2d(11.5, 25, Math.toRadians(90)), Math.toRadians(90),
                                        (pose2dDual, posePath, v) -> 90),
                "SPIKE_THREE", (TrajectoryActionBuilder  trajectory) ->
                        trajectory
                                .splineToLinearHeading(new Pose2d(32, 25, Math.toRadians(90)), Math.toRadians(90), (pose2dDual, posePath, v) -> 90),
                "GATE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .turnTo(Math.toRadians(180))
                                .strafeTo(new Vector2d(-3, 50),
                                        (pose2dDual, posePath, v) -> 90)
                                .strafeTo(new Vector2d(-3, 58),
                                        (pose2dDual, posePath, v) -> 90)
        );

        backActions = Map.of(
                "SHOOT", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .splineToLinearHeading(
                                        new Pose2d(54, 15, Math.toRadians(157)),
                                        Math.toRadians(157),
                                        (pose2dDual, posePath, v) -> 20
                                ),
                "LEAVE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .splineToLinearHeading(
                                        new Pose2d(54, 30, Math.toRadians(270)),
                                        Math.toRadians(270)
                                ),
                "HUMAN_PLAYER_AREA", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .splineToLinearHeading(new Pose2d(54, 58, Math.toRadians(90)), Math.toRadians(90))
                                .lineToY(50)
                                .lineToY(58)
                                .lineToY(50)
                                .lineToY(58),
                "SPIKE_THREE", (TrajectoryActionBuilder  trajectory) ->
                        trajectory
                                .splineToLinearHeading(new Pose2d(32, 25, Math.toRadians(90)), Math.toRadians(90), (pose2dDual, posePath, v) -> 20),
                "INTAKE", (TrajectoryActionBuilder trajectory) ->
                        trajectory
                                .lineToY(58, (pose2dDual, posePath, v) -> 9)

        );

        Action ft1 = action(frontActions, "SHOOT_LEAVE", makeBuilder(drive.localizer.getPose(), params)).build();

         frontThree = new Path(
                initialPose,
                new SequentialAction(
                        actions.stop(),
                        ft1,
                        actions.shoot(3, 1050, Robot.Globals.Colors.GPP, 0),
                        actions.stop()
                )
        );

         TrajectoryActionBuilder fs1 = action(frontActions, "SHOOT", makeBuilder(drive.localizer.getPose(), params));
         TrajectoryActionBuilder fs2 = action(frontActions, "SPIKE_ONE", fs1.endTrajectory().fresh());
         TrajectoryActionBuilder fs3 = action(frontActions, "INTAKE", fs2.endTrajectory().fresh());
         TrajectoryActionBuilder fs4 = action(frontActions, "SHOOT_LEAVE", fs3.endTrajectory().fresh());

         frontSix = new Path(
                 initialPose,
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
                         actions.shoot(3, 1050, Robot.Globals.Colors.PPG, 0),
                         actions.stop()
                 )
         );

         TrajectoryActionBuilder fsg1 = action(frontActions, "SHOOT", makeBuilder(drive.localizer.getPose(), params));
         TrajectoryActionBuilder fsg2 = action(frontActions, "SPIKE_ONE", fsg1.endTrajectory().fresh());
         TrajectoryActionBuilder fsg3 = action(frontActions, "INTAKE", fsg2.endTrajectory().fresh());
         TrajectoryActionBuilder fsg4 = action(frontActions, "GATE", fsg3.endTrajectory().fresh());
         TrajectoryActionBuilder fsg5 = action(frontActions, "SHOOT_LEAVE", fsg4.endTrajectory().fresh());

        frontSixGate = new Path(
                initialPose,
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
                        actions.shoot(3, 1100, Robot.Globals.Colors.PPG, 0),
                        actions.stop()
                )
        );

        TrajectoryActionBuilder fn1 = action(frontActions, "SHOOT", makeBuilder(drive.localizer.getPose(), params));
        TrajectoryActionBuilder fn2 = action(frontActions, "SPIKE_ONE", fn1.endTrajectory().fresh());
        TrajectoryActionBuilder fn3 = action(frontActions, "INTAKE", fn2.endTrajectory().fresh());
        TrajectoryActionBuilder fn4 = action(frontActions, "SHOOT", fn3.endTrajectory().fresh());
        TrajectoryActionBuilder fn5 = action(frontActions, "SPIKE_TWO", fn4.endTrajectory().fresh());
        TrajectoryActionBuilder fn6 = action(frontActions, "INTAKE", fn5.endTrajectory().fresh());
        TrajectoryActionBuilder fn7 = action(frontActions, "SHOOT_LEAVE", fn6.endTrajectory().fresh());

        frontNine = new Path(
                initialPose,
                new SequentialAction(
                        actions.stop(),
                        fn1.build(),
                        actions.shoot(3, 1050, Robot.Globals.Colors.GPP, 0),
                        fn2.build(),
                        new ParallelAction(
                                fn3.build(),
                                actions.intake()
                        ),
                        fn4.build(),
                        actions.shoot(3, 1050, Robot.Globals.Colors.PPG, 0),
                        fn5.build(),
                        new ParallelAction(
                                fn6.build(),
                                actions.intake()
                        ),
                        fn7.build(),
                        actions.shoot(3, 1100, Robot.Globals.Colors.PPG, 0),
                        actions.stop()
                )
        );

        TrajectoryActionBuilder fng1 = action(frontActions, "SHOOT", makeBuilder(drive.localizer.getPose(), params));
        TrajectoryActionBuilder fng2 = action(frontActions, "SPIKE_ONE", fng1.endTrajectory().fresh());
        TrajectoryActionBuilder fng3 = action(frontActions, "INTAKE", fng2.endTrajectory().fresh());
        TrajectoryActionBuilder fng4 = action(frontActions, "GATE", fng3.endTrajectory().fresh());
        TrajectoryActionBuilder fng5 = action(frontActions, "SHOOT", fng4.endTrajectory().fresh());
        TrajectoryActionBuilder fng6 = action(frontActions, "SPIKE_TWO", fng5.endTrajectory().fresh());
        TrajectoryActionBuilder fng7 = action(frontActions, "INTAKE", fng6.endTrajectory().fresh());
        TrajectoryActionBuilder fng8 = action(frontActions, "SHOOT_LEAVE", fng7.endTrajectory().fresh());

        frontNineGate = new Path(
                initialPose,
                new SequentialAction(
                        actions.stop(),
                        fng1.build(),
                        actions.shoot(3, 1050, Robot.Globals.Colors.GPP, 0),
                        fng2.build(),
                        new ParallelAction(
                                fng3.build(),
                                actions.intake()
                        ),
                        fng4.build(),
                        fng5.build(),
                        actions.shoot(3, 1050, Robot.Globals.Colors.PPG, 0),
                        fng6.build(),
                        new ParallelAction(
                                fng7.build(),
                                actions.intake()
                        ),
                        fng8.build(),
                        actions.shoot(3, 1100, Robot.Globals.Colors.PPG, 0),
                        actions.stop()
                )
        );

        TrajectoryActionBuilder ftw1 = action(frontActions, "SHOOT", makeBuilder(drive.localizer.getPose(), params));
        TrajectoryActionBuilder ftw2 = action(frontActions, "SPIKE_ONE", ftw1.endTrajectory().fresh());
        TrajectoryActionBuilder ftw3 = action(frontActions, "INTAKE", ftw2.endTrajectory().fresh());
        TrajectoryActionBuilder ftw4 = action(frontActions, "SHOOT", ftw3.endTrajectory().fresh());
        TrajectoryActionBuilder ftw5 = action(frontActions, "SPIKE_TWO", ftw4.endTrajectory().fresh());
        TrajectoryActionBuilder ftw6 = action(frontActions, "INTAKE", ftw5.endTrajectory().fresh());
        TrajectoryActionBuilder ftw7 = action(frontActions, "SHOOT", ftw6.endTrajectory().fresh());
        TrajectoryActionBuilder ftw8 = action(frontActions, "SPIKE_THREE", ftw7.endTrajectory().fresh());
        TrajectoryActionBuilder ftw9 = action(frontActions, "INTAKE", ftw8.endTrajectory().fresh());
        TrajectoryActionBuilder ftw10 = action(frontActions, "SHOOT_LEAVE", ftw9.endTrajectory().fresh());

        frontTwelve = new Path(
                initialPose,
                new SequentialAction(
                        actions.stop(),
                        ftw1.build(),
                        actions.shoot(3, 1050, Robot.Globals.Colors.GPP, 0),
                        ftw2.build(),
                        new ParallelAction(
                                ftw3.build(),
                                actions.intake()
                        ),
                        ftw4.build(),
                        actions.shoot(3, 1050, Robot.Globals.Colors.PPG, 0),
                        ftw5.build(),
                        new ParallelAction(
                                ftw6.build(),
                                actions.intake()
                        ),
                        ftw7.build(),
                        actions.shoot(3, 1100, Robot.Globals.Colors.PPG, 0),
                        ftw8.build(),
                        new ParallelAction(
                                ftw9.build(),
                                actions.intake()
                        ),
                        ftw10.build(),
                        actions.shoot(3, 1100, Robot.Globals.Colors.PPG, 0),
                        actions.stop()
                )
        );

        TrajectoryActionBuilder bt1 = action(backActions, "SHOOT", makeBuilder(drive.localizer.getPose(), params));
        TrajectoryActionBuilder bt2 = action(backActions, "LEAVE", bt1.endTrajectory().fresh());

         backThree = new Path(
                 initialPose,
                new SequentialAction(
                        actions.stop(),
                        bt1.build(),
                        actions.shoot(3, 1375, Robot.Globals.Colors.GPP, 1),
                        bt2.build(),
                        actions.stop()
                )
        );

        TrajectoryActionBuilder bthp1 = action(backActions, "SHOOT", makeBuilder(drive.localizer.getPose(), params));
        TrajectoryActionBuilder bthp2 = action(backActions, "HUMAN_PLAYER_AREA", bthp1.endTrajectory().fresh());
        TrajectoryActionBuilder bthp3 = action(backActions, "SHOOT", bthp2.endTrajectory().fresh());
        TrajectoryActionBuilder bthp4 = action(backActions, "LEAVE", bthp3.endTrajectory().fresh());


         backThreeHP = new Path(
                 initialPose,
                 new SequentialAction(
                         actions.stop(),
                         bthp1.build(),
                         actions.shoot(3, 1375, Robot.Globals.Colors.GPP, 1),
                         new ParallelAction(
                            bthp2.build(),
                                 actions.intake(5)
                         ),
                         bthp3.build(),
                         actions.shoot(3, 1375, Robot.Globals.Colors.PGP, 1),
                         bthp4.build(),
                         actions.stop()
                 )
         );

        TrajectoryActionBuilder bs1 = action(backActions, "SHOOT", makeBuilder(drive.localizer.getPose(), params));
        TrajectoryActionBuilder bs2 = action(backActions, "SPIKE_THREE", bs1.endTrajectory().fresh());
        TrajectoryActionBuilder bs3 = action(backActions, "INTAKE", bs2.endTrajectory().fresh());
        TrajectoryActionBuilder bs4 = action(backActions, "SHOOT", bs3.endTrajectory().fresh());
        TrajectoryActionBuilder bs5 = action(backActions, "LEAVE", bs4.endTrajectory().fresh());

        backSix = new Path(
                initialPose,
                new SequentialAction(
                        actions.stop(),
                        bs1.build(),
                        actions.shoot(3, 1375, Robot.Globals.Colors.GPP, 1),
                        bs2.build(),
                        new ParallelAction(
                                bs3.build(),
                                actions.intake()
                        ),
                        bs4.build(),
                        actions.shoot(3, 1375, Robot.Globals.Colors.GPP, 1),
                        bs5.build(),
                        actions.stop()
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
                    case 2:
                        if (!params.gate) {
                            path = frontNine;
                            break;
                        } else {
                            path = frontNineGate;
                            break;
                        }
                    case 3:
                        path = frontTwelve;
                        break;
                }
                break;
            case BACK:
                switch (params.spikes) {
                    case 0:
                        if (!params.humanPlayer) {
                            path = backThree;
                            break;
                        } else {
                            path = backThreeHP;
                            break;
                        }
                    case 1:
                        path = backSix;
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