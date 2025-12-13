package org.firstinspires.ftc.teamcode.shared.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.options.MenuLib;

public class DrivetrainMenu extends MenuLib.Menu {
    public DrivetrainMenu(MenuLib.MenuHost host, Robot robot, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "DRIVETRAIN");

        addOption(new MenuLib.Option("Reset IMU", robot.drivetrain::resetIMU));

        addOption(new MenuLib.InfoOption(() -> ""));
        addOption(new MenuLib.InfoOption(() -> "FL Position: " + robot.drivetrain.getFLPos()));
        addOption(new MenuLib.InfoOption(() -> "FR Position: " + robot.drivetrain.getFRPos()));
        addOption(new MenuLib.InfoOption(() -> "BL Position: " + robot.drivetrain.getBLPos()));
        addOption(new MenuLib.InfoOption(() -> "BR Position: " + robot.drivetrain.getBRPos()));

        addOption(new MenuLib.InfoOption(() -> ""));
        addOption(new MenuLib.Option("Back", host::goBack));
    }
}
