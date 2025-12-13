package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.BaseOpMode;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.AutoPath;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.Leave;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.OneCycleBack;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.OneCycleFront;
import org.firstinspires.ftc.teamcode.shared.actions.RRActions;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;

@Autonomous(name = "Auto", group = "OpModes")
public class AutoOpMode extends BaseOpMode {
    private AutoPath path;
    private MecanumDrive drive;
    private RRActions rr;
    @Override
    public void initLoop() {
        switch (robot.autoStrategy) {
            case ONECYCLEFRONT:
                path = new OneCycleFront(robot.alliance);
                break;
            case ONECYCLEBACK:
                path = new OneCycleBack(robot.alliance);
                break;
            case LEAVE:
            default:
                path = new Leave(robot.alliance);
                break;
        }

        Pose2d initialPose = path.getInitialPose();
        drive = new MecanumDrive(robot, hardwareMap, initialPose);

        rr = new RRActions(robot);

        telemetry.addLine("Auto ready");
        telemetry.addData("Path", path.getName());
        telemetry.update();
    }
    public void runLoop() {
        Actions.runBlocking(path.build(drive, rr, telemetry));
    }
}
