package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;

public abstract class BaseOpMode extends LinearOpMode {
    protected Controls controls;
    protected Configuration config;
    protected Robot robot;

    @Override
    public final void runOpMode() {
        controls = new Controls();
        config = new Configuration();
        robot = new Robot(hardwareMap, telemetry, controls, config);

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
