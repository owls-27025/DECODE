package org.firstinspires.ftc.teamcode.opmodes.hedwig;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.helpers.roadrunner.MecanumDrive;

@Autonomous
public class Test extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                .lineToY(30)
                .turn(Math.toRadians(180))
                .lineToY(0)
                .turn(Math.toRadians(180))
                .strafeTo(new Vector2d(-30, 0))
                .strafeTo(new Vector2d(0, 0))
                .setTangent(Math.toRadians(153))
                .splineTo(new Vector2d(-30, 15), Math.toRadians(153))
                .setTangent(Math.toRadians(-27))
                .splineTo(new Vector2d(0, 0), Math.toRadians(-27));

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        tab1.build()
                )
        );
    }
}