package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.*;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.actions.RRActions;
import org.firstinspires.ftc.teamcode.auto.paths.AutoPath;
import org.firstinspires.ftc.teamcode.auto.paths.OneCycleFront;
import org.firstinspires.ftc.teamcode.auto.paths.Leave;
import org.firstinspires.ftc.teamcode.auto.paths.OneCycleBack;
import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.limelight.Limelight;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;

@Autonomous(name = "Auto", group = "OpModes")
public class V1 extends LinearOpMode {

    @Override
    public void runOpMode() {
        Subsystems.init(hardwareMap, telemetry);
        Limelight.init(hardwareMap);

        AutoPath path;


        switch (Globals.autoStrategy) {
            case ONECYCLEFRONT:
                path = new OneCycleFront(Globals.alliance);
                break;
            case ONECYCLEBACK:
                path = new OneCycleBack(Globals.alliance);
                break;
            case LEAVE:
            default:
                path = new Leave(Globals.alliance);
                break;
        }

        Pose2d initialPose = path.getInitialPose();

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        RRActions RRActions = new RRActions();

        telemetry.addLine("Auto ready");
        telemetry.addData("Path", path.getName());
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                path.build(drive, RRActions, telemetry)
        );
    }
}
