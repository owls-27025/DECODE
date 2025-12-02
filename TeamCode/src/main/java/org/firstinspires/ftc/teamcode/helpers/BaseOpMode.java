package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanisms.distanceSensor.DistanceSensor;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.intake.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.light.Light;
import org.firstinspires.ftc.teamcode.mechanisms.shooter.Shooter;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;

public abstract class BaseOpMode extends LinearOpMode {
    protected Controls controls;
    protected Configuration config;
    protected Robot robot;

    protected Intake intake;
    protected Spindexer spindexer;
    protected Shooter shooter;
    protected Light light;
    protected DistanceSensor distanceSensor;
    protected Drivetrain drivetrain;

    @Override
    public final void runOpMode() {
        controls = new Controls();
        config = new Configuration();
        robot = new Robot(hardwareMap, telemetry, controls, config);

        spindexer = robot.getSpindexer();
        shooter = robot.getShooter();
        light = robot.getLight();
        intake = robot.getIntake();
        distanceSensor = robot.getDistanceSensor();
        drivetrain = robot.getDrivetrain();

        onInit();

        while (!isStarted() && !isStopRequested()) {
            controls.update(gamepad1, gamepad2);
            initLoop();
            telemetry.update();
        }

        waitForStart();
        if (isStopRequested()) {
            onStop();
            return;
        }

        onStart();

        while (opModeIsActive()) {
            controls.update(gamepad1, gamepad2);

            onLoop();

            this.telemetry();
            telemetry.update();
        }

        onStop();
    }

    protected void onInit() {}
    protected void initLoop() {}
    protected void onStart() {}
    protected abstract void onLoop();
    protected void onStop() {}
    protected void telemetry() {}
}
