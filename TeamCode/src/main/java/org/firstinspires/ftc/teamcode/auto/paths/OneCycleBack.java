package org.firstinspires.ftc.teamcode.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.auto.actions.RRActions;
import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;

public class OneCycleBack implements AutoPath {
    private final Globals.Alliances alliance;

    public OneCycleBack(Globals.Alliances alliance) {
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Globals.Alliances.RED) {
            return new Pose2d(60, 11.5, Math.toRadians(150));
        } else {
            return new Pose2d(60, -11.5, Math.toRadians(240));
        }
    }

    @Override
    public String getName() {
        return "One Cycle (Back)";
    }

    @Override
    public Action build(MecanumDrive drive, RRActions rractions, Telemetry telemetry) {
        Pose2d initialPose = getInitialPose();

        if (alliance == Globals.Alliances.RED) {
            TrajectoryActionBuilder leave = drive.actionBuilder(initialPose)
                    .strafeTo(new Vector2d(40, 30));

            return new SequentialAction(
                    rractions.spinUpShooter(),
                    rractions.shoot(1, 1400),
                    rractions.shoot(1, 1400),
                    rractions.shoot(1, 1400),
                    leave.build()
            );
        } else {
            TrajectoryActionBuilder leave = drive.actionBuilder(initialPose)
                    .strafeTo(new Vector2d(40, -30));

            return new SequentialAction(
                    rractions.spinUpShooter(),
                    rractions.shoot(1, 1400),
                    rractions.shoot(1, 1500),
                    rractions.shoot(1, 1550),
                    leave.build()
            );
        }
    }
}