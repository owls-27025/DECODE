package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.AutoPath;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.Leave;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.OneCycleBack;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.OneCycleFront;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.ThreeCycleFront;
import org.firstinspires.ftc.teamcode.shared.actions.ActionManager;
import org.firstinspires.ftc.teamcode.shared.actions.IntakeAction;
import org.firstinspires.ftc.teamcode.shared.actions.ShootAction;
import org.firstinspires.ftc.teamcode.shared.actions.SpindexerAction;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuHostImpl;
import org.firstinspires.ftc.teamcode.shared.helpers.options.menus.opmodes.AutoConfig;

@SuppressWarnings("unused")
public class AutoOpMode extends OwlsOpMode {

    private enum InitState { MENU, READY }
    private InitState initState = InitState.MENU;

    private MenuHostImpl menuHost;

    private AutoPath path;
    private MecanumDrive drive;
    private RRActions rr;

    private boolean built = false;

    private ActionManager actionManager = new ActionManager();

    private Action intakeAction;
    private Action shootAction;
    private Action spindexerAction;

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
        telemetry.update();
    }

    @Override
    public void onInit() {
        intakeAction = actionManager.addAndReturn(new IntakeAction(robot));
        shootAction = actionManager.addAndReturn(new ShootAction(robot));
        spindexerAction = actionManager.addAndReturn(new SpindexerAction(robot));
    }

    @Override
    public void onStart() {
        if (!built) buildAutoFromRobotConfig();

//        Actions.runBlocking(rr.withSubsystems(path.build(drive, rr, telemetry)));
        actionManager.addAndReturn(rr.shoot(3, 1050));
    }

    @Override
    public void runLoop() {
        TelemetryPacket packet = new TelemetryPacket();
        actionManager.run(packet);
        dash.sendTelemetryPacket(packet);
    }

    private void buildAutoFromRobotConfig() {
        switch (Robot.Globals.autoStrategy) {
            case ONECYCLEFRONT: path = new OneCycleFront(Robot.Globals.alliance); break;
            case ONECYCLEBACK:  path = new OneCycleBack(Robot.Globals.alliance);  break;
            case THREECYCLEFRONT: path = new ThreeCycleFront(Robot.Globals.alliance); break;
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
