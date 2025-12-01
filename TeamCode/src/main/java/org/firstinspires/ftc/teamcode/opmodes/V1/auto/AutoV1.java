package org.firstinspires.ftc.teamcode.opmodes.V1.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.helpers.BaseOpMode;
import org.firstinspires.ftc.teamcode.opmodes.V1.actions.RRActions;
import org.firstinspires.ftc.teamcode.opmodes.V1.auto.paths.AutoPath;
import org.firstinspires.ftc.teamcode.opmodes.V1.auto.paths.FourCycle;
import org.firstinspires.ftc.teamcode.opmodes.V1.auto.paths.Leave;

@Autonomous(name = "Auto", group = "OpModes")
public class AutoV1 extends BaseOpMode {

    private AutoPath path;
    private MecanumDrive drive;
    private RRActions rrActions;

    @Override
    protected void onInit() {
        switch (robot.autoStrategy) {
            case FOURCYCLE:
                path = new FourCycle(robot, robot.alliance);
                break;
            case LEAVE:
            default:
                path = new Leave(robot, robot.alliance);
                break;
        }

        Pose2d initialPose = path.getInitialPose();
        drive = new MecanumDrive(hardwareMap, initialPose, config);

        rrActions = new RRActions(robot);

        telemetry.addLine("Auto ready");
        telemetry.addData("Path initial pose", initialPose);
        telemetry.update();
    }

    @Override
    protected void onStart() {
        if (isStopRequested()) return;

        Actions.runBlocking(
                path.build(drive, rrActions)
        );
    }

    @Override
    protected void onLoop() {
        // nothing
    }
}
