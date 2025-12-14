package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import org.firstinspires.ftc.teamcode.shared.mechanisms.distance.Distance;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.shared.mechanisms.intake.Intake;
import org.firstinspires.ftc.teamcode.shared.mechanisms.light.Light;
import org.firstinspires.ftc.teamcode.shared.mechanisms.limelight.Limelight;
import org.firstinspires.ftc.teamcode.shared.mechanisms.shooter.Shooter;
import org.firstinspires.ftc.teamcode.shared.mechanisms.spindexer.Spindexer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("RedundantThrows")
public class OwlsOpMode extends LinearOpMode {
    protected Robot robot;
    protected Spindexer spindexer;
    protected Shooter shooter;
    protected Intake intake;
    protected Light light;
    protected Drivetrain drivetrain;
    protected Distance distance;
    protected Limelight limelight;

    protected FtcDashboard dash;
    protected List<Action> runningActions;

    protected OwlsGamepad p1;
    protected OwlsGamepad p2;

    protected boolean overrideTelemetry;

    @Override
    public final void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry);

        spindexer = robot.spindexer;
        shooter = robot.shooter;
        intake = robot.intake;
        limelight = robot.limelight;
        drivetrain = robot.drivetrain;
        distance = robot.distance;
        light = robot.light;

        dash = FtcDashboard.getInstance();
        runningActions = new ArrayList<>();

        p1 = new OwlsGamepad(gamepad1);
        p2 = new OwlsGamepad(gamepad2);

        telemetry = new MultipleTelemetry(telemetry, dash.getTelemetry());
        overrideTelemetry = false;

        try {
            onInit();

            while (opModeInInit() && !isStopRequested()) {
                p1.update();
                p2.update();
                robot.update();
                initLoop();
                idle();
            }

            waitForStart();
            if (isStopRequested()) return;

            onStart();

            while (opModeIsActive() && !isStopRequested()) {
                p1.update();
                p2.update();
                robot.update();
                runLoop();
                if (!overrideTelemetry) {
                    telemetry();
                    telemetry.update();
                }
                idle();
            }
        } finally {
            onStop();
        }
    }


    protected void onInit() {}

    protected void onStart() {}

    protected void onStop() {}

    protected void initLoop() {}

    protected void runLoop() {}

    protected void telemetry() {}
}
