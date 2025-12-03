package org.firstinspires.ftc.teamcode.auto.paths;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.auto.actions.RRActions;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;

public interface AutoPath {
    Pose2d getInitialPose();
    Action build(MecanumDrive drive, RRActions rractions);
}
