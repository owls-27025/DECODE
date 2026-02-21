package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuHostImpl;
import org.firstinspires.ftc.teamcode.shared.helpers.options.menus.opmodes.AutoConfig;
import com.acmerobotics.roadrunner.SequentialAction;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class AutoOpMode extends OwlsOpMode {

    private enum InitState { MENU, READY }
    private InitState initState = InitState.MENU;

    private MenuHostImpl menuHost;

    private MecanumDrive drive;
    private RRActions rr;

    private boolean built = false;

    private final ElapsedTime delayTimer = new ElapsedTime();

    public AutoParams autoParams;
    public AutoConfig autoConfig;
    public AutoPath path;
    public Action action;

    @Override
    public void initLoop() {
        if (initState == InitState.MENU && menuHostIsUninitialized()) {
            menuHost = new MenuHostImpl();

            autoParams = new AutoParams();
            autoParams.alliance = AutoParams.Alliances.RED; // defaults
            autoParams.strategy = AutoParams.Strategies.FRONT;
            autoParams.gate = false;
            autoParams.spikes = 0;

            autoConfig = new AutoConfig(menuHost, autoParams, p1, p2, telemetry);
            menuHost.setRoot(autoConfig);
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
            rr = new RRActions(robot, telemetry);
            path = new AutoPath(rr);
            action = path.build(autoParams, robot, hardwareMap);
            built = true;
        }

        limelight.getMotif(autoParams);

        telemetry.addData("Alliance", autoParams.alliance);
        telemetry.addData("Strategy", autoParams.strategy);
        telemetry.addData("Gate", autoParams.gate);
        telemetry.addData("Spikes", autoParams.spikes);
        telemetry.addData("Delay", autoParams.delay + " seconds");
        telemetry.addData("Motif", Robot.Globals.motif);
        telemetry.update();
    }

    @Override
    public void onStart() {
        ElapsedTime runtime = new ElapsedTime();
        delayTimer.reset();

        if (autoParams.delay != 0) {
            delayTimer.reset();
            while (delayTimer.time(TimeUnit.SECONDS) < autoParams.delay);
        }

        Actions.runBlocking(rr.withSubsystems(action));

        shooter.shoot(0);
        spindexer.shootPosition();
        intake.stop();

    }

    private boolean menuHostIsUninitialized() {
        return menuHost == null;
    }
}