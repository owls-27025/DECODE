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

        manager.add(new IntakeAction(robot));
        manager.add(new ShootAction(robot));
        manager.add(new SpindexerAction(robot));

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
            private int remaining = shots;
            private int lastCount = 0;

            @Override
            public boolean run(@NotNull TelemetryPacket packet) {
                if (!started) {
                    Robot.Globals.shooterVelocity = velocity;
                    robot.manualShoot = false;
                    robot.startShoot = true;

                    lastCount = robot.artifactCount;
                    started = true;
                }

                int now = robot.artifactCount;
                if (now < lastCount) {
                    remaining -= (lastCount - now);
                    lastCount = now;
                } else {
                    lastCount = now;
                }

                if (remaining <= 0) {
                    robot.startShoot = false;
                    return false;
                }

                if (robot.artifactCount <= 0) {
                    robot.startShoot = false;
                    return false;
                }

                return true;
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
}
