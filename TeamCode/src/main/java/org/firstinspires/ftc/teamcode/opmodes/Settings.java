package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.options.MenuHostImpl;
import org.firstinspires.ftc.teamcode.shared.options.opmodes.SettingsMainMenu;

@TeleOp(name = "Settings", group = "Settings")
public class Settings extends LinearOpMode {

    @Override
    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        Robot robot = new Robot(hardwareMap);

        GoBildaPinpointDriver pinpoint =
                hardwareMap.get(GoBildaPinpointDriver.class, robot.config.odometry.itemName);

        MenuHostImpl host = new MenuHostImpl();
        host.setRoot(new SettingsMainMenu(host, robot, gamepad1, gamepad2, telemetry, pinpoint));

        telemetry.addLine("Settings ready");
        telemetry.update();

        while (!isStopRequested() && opModeInInit()) {
            host.update();
            idle();
        }

        waitForStart();
        while (!isStopRequested() && opModeIsActive()) {
            idle();
        }
    }
}
