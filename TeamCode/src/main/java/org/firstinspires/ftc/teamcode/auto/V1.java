package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.*;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.actions.RRActions;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;

import java.lang.Math;

@Autonomous(name = "Auto", group = "OpModes")
public class V1 extends LinearOpMode {

    @Override
    public void runOpMode() {
        Subsystems.init(hardwareMap, telemetry);
//        Limelight.init(hardwareMap);

//        AutoPath path;
//
//        switch (Globals.autoStrategy) {
//            case LEAVE:
//                path = new Leave(Globals.alliance);
//                break;
//            case FOURCYCLE:
//                path = new FourCycle(Globals.alliance);
//            default:
//                path = new Leave(Globals.alliance);
//                break;
//        }

        Pose2d initialPose = new Pose2d(-50, 50, Math.toRadians(135));

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        RRActions RRActions = new RRActions();

//        telemetry.addLine("Auto ready");
//        telemetry.addData("Path: ", path.getInitialPose());
//        telemetry.update();

        TrajectoryActionBuilder path1 = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(-12, 12));

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        path1.build(),
                        RRActions.shoot(3)
                )
        );
    }
}
