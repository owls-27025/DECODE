package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.testing;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

public class DrivetrainMenu extends MenuLib.Menu {
    public DrivetrainMenu(MenuLib.MenuHost host, Robot robot, OwlsGamepad gamepad1, OwlsGamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "DRIVETRAIN");

        addOption(MenuLib.Option.action(() -> "Reset IMU", robot.drivetrain::resetIMU));

        addOption(MenuLib.Option.info(() -> ""));
        addOption(MenuLib.Option.info(() -> "FL Position: " + robot.drivetrain.getFLPos()));
        addOption(MenuLib.Option.info(() -> "FR Position: " + robot.drivetrain.getFRPos()));
        addOption(MenuLib.Option.info(() -> "BL Position: " + robot.drivetrain.getBLPos()));
        addOption(MenuLib.Option.info(() -> "BR Position: " + robot.drivetrain.getBRPos()));
        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.info(() -> "Odometry Heading: " + robot.drivetrain.getOdometryHeading()));
        addOption(MenuLib.Option.info(() -> "Odometry X: " + robot.drivetrain.getOdometryX()));
        addOption(MenuLib.Option.info(() -> "Odometry Y: " +  robot.drivetrain.getOdometryY()));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.info(() -> "Control Hub Heading: " + robot.drivetrain.getIMUHeading()));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(() -> "Back", host::goBack));
    }
}
