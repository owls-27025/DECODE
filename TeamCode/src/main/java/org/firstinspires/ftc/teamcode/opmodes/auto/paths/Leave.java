package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.auto.RRActions;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;

public class Leave implements AutoPath {

    private final Robot.Globals.Alliances alliance;

    @Override
    public double defaultVelocity() {
        return 0;
    }

    public Leave(Robot.Globals.Alliances alliance) {
        this.alliance = alliance;
    }

    @Override
    public Pose2d getInitialPose() {
        if (alliance == Robot.Globals.Alliances.RED) {
            return new Pose2d(55, 10, Math.toRadians(180));
        } else {
            return new Pose2d(55, -10, Math.toRadians(180));
        }
    }

//    @Override
//    public double defaultVelocity() {
//        return 0;
//    }

    @Override
    public String getName() {
        return "Leave";
    }

    @Override
    public Action build(MecanumDrive drive, RRActions rractions, Telemetry telemetry) {
        Pose2d initialPose = getInitialPose();

        return drive.actionBuilder(initialPose)
                .strafeTo(alliance == Robot.Globals.Alliances.RED
                        ? new Vector2d(53, 30)
                        : new Vector2d(53, -30))
                .build();
    }
}
