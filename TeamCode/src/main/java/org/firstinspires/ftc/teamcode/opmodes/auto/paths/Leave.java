package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.shared.actions.RRActions;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;

public class Leave implements AutoPath {

    private final Robot.Alliances alliance;

    public Leave(Robot.Alliances alliance) {
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Robot.Alliances.RED) {
            return new Pose2d(60, 10, Math.toRadians(0));
        } else {
            return new Pose2d(60, -10, Math.toRadians(0));
        }
    }

    @Override
    public String getName() {
        return "Leave";
    }

    @Override
    public Action build(MecanumDrive drive, RRActions rractions, Telemetry telemetry) {
        Pose2d initialPose = getInitialPose();

        return drive.actionBuilder(initialPose)
                .strafeTo(alliance == Robot.Alliances.RED
                        ? new Vector2d(57, 30)
                        : new Vector2d(57, -30))
                .build();
    }
}
