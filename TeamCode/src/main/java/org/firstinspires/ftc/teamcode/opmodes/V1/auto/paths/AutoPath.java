package org.firstinspires.ftc.teamcode.opmodes.V1.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import org.firstinspires.ftc.teamcode.opmodes.V1.actions.RRActions;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;

public interface AutoPath {
    Pose2d getInitialPose();
    Action build(MecanumDrive drive, RRActions rractions);
}