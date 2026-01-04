package org.firstinspires.ftc.teamcode.opmodes.auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.actions.ActionManager;
import org.firstinspires.ftc.teamcode.shared.actions.IntakeAction;
import org.firstinspires.ftc.teamcode.shared.actions.ShootAction;
import org.firstinspires.ftc.teamcode.shared.actions.SpindexerAction;
import org.jetbrains.annotations.NotNull;

public class RRActions {
    private final Robot robot;

    private final ActionManager manager = new ActionManager();
    private boolean subsystemsAdded = false;

    public RRActions(Robot robot) {
        this.robot = robot;
    }
    private void ensureSubsystems() {
        if (subsystemsAdded) return;

        manager.addAndReturn(new IntakeAction(robot));
        manager.addAndReturn(new ShootAction(robot));
        manager.addAndReturn(new SpindexerAction(robot));

        subsystemsAdded = true;
    }

    public Action withSubsystems(final Action inner) {
        return new Action() {
            @Override
            public boolean run(@NotNull TelemetryPacket packet) {
                ensureSubsystems();
                manager.run(packet);
                return inner.run(packet);
            }

            @Override
            public void preview(com.acmerobotics.dashboard.canvas.@NotNull Canvas canvas) {
                inner.preview(canvas);
            }
        };
    }

    public Action spinUpShooter() {
        return new Action() {
            @Override
            public boolean run(@NotNull TelemetryPacket packet) {
                return !robot.shooterReady;
            }
        };
    }

    public Action shoot(final int shots, final int velocity) {
        return new Action() {
            private boolean started = false;

            @Override
            public boolean run(@NotNull TelemetryPacket packet) {
                if (!started) {
                    robot.artifactCount = shots;
                    started = true;
                    Robot.Globals.shooterVelocity = velocity;
                    robot.startShoot = true;
                }

                return robot.artifactCount > 0;
            }
        };
    }

    public Action shoot(final int shots, final int velocity, Robot.Globals.Colors colors) {
        return new Action() {
            private boolean started = false;

            @Override
            public boolean run(@NotNull TelemetryPacket packet) {
                if (!started) {
                    robot.artifactCount = shots;
                    started = true;
                    Robot.Globals.shooterVelocity = velocity;
                    robot.startShoot = true;
                    robot.colors = colors;
                    robot.sort = true;
                }

                return robot.artifactCount > 0;
            }
        };
    }

    public Action intake() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                robot.startIntake = true;

                return robot.artifactCount < 3;
            }
        };
    }

    public Action stop() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                robot.stop = true;

                return false;
            }
        };
    }
}
