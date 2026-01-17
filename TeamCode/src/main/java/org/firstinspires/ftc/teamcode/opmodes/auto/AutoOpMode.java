package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.AutoPath;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.Leave;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.OneCycleBack;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.OneCycleFront;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.ThreeCycleBack;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.ThreeCycleFront;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuHostImpl;
import org.firstinspires.ftc.teamcode.shared.helpers.options.menus.opmodes.AutoConfig;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class AutoOpMode extends OwlsOpMode {

    private enum InitState { MENU, READY }
    private InitState initState = InitState.MENU;

    private MenuHostImpl menuHost;

    private AutoPath path;
    private MecanumDrive drive;
    private RRActions rr;

    private boolean built = false;

    private final ElapsedTime delayTimer = new ElapsedTime();

    @Override
    public void initLoop() {
        if (initState == InitState.MENU && menuHostIsUninitialized()) {
            menuHost = new MenuHostImpl();
            menuHost.setRoot(new AutoConfig(menuHost, robot, p1, p2, telemetry));
        }

        if (initState == InitState.MENU) {
            menuHost.update();
            telemetry.update();

            if (!menuHost.isActive) {
                initState = InitState.READY;
            }
            return;
        }

        if (!built) {
            buildAutoFromRobotConfig();
            built = true;
        }

        telemetry.addLine("Auto ready");
        telemetry.addData("Path", path.getName());
        telemetry.addData("Alliance", Robot.Globals.alliance);
        telemetry.addData("Delay Auto", Robot.Globals.delayAuto);
        telemetry.addData("Motif", Robot.Globals.motif);
        telemetry.addData("Hit Gate", Robot.Globals.gate);
        telemetry.update();
    }

    @Override
    public void onStart() {
        if (!built) buildAutoFromRobotConfig();

        delayTimer.reset();
        shooter.shoot(path.defaultVelocity());

        if (Robot.Globals.delayAuto != 0) {
            delayTimer.reset();
            while (delayTimer.time(TimeUnit.SECONDS) < Robot.Globals.delayAuto);
        }

        Actions.runBlocking(rr.withSubsystems(path.build(drive, rr, telemetry)));
    }

    private void buildAutoFromRobotConfig() {
        switch (Robot.Globals.autoStrategy) {
            case ONECYCLEFRONT: path = new OneCycleFront(Robot.Globals.alliance); break;
            case ONECYCLEBACK:  path = new OneCycleBack(Robot.Globals.alliance);  break;
            case THREECYCLEFRONT: path = new ThreeCycleFront(Robot.Globals.alliance); break;
            case THREECYCLEBACK: path = new ThreeCycleBack(Robot.Globals.alliance); break;
            case LEAVE:
            default:            path = new Leave(Robot.Globals.alliance);         break;
        }

        Pose2d initialPose = path.getInitialPose();
        drive = new MecanumDrive(robot, hardwareMap, initialPose);
        rr = new RRActions(robot);
        robot.stop = true;
    }

    private boolean menuHostIsUninitialized() {
        return menuHost == null;
    }
}