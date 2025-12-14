package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.opmodes;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsController;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

public class TeleOpMenu extends MenuLib.Menu {

    public TeleOpMenu(MenuLib.MenuHost host, Robot robot, OwlsController gamepad1, OwlsController gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "TELEOP");

        addOption(MenuLib.Option.value(
            () -> String.format("Drive Speed: %.2f", robot.driveSpeed),
            () -> robot.driveSpeed = Math.max(0.0, robot.driveSpeed - 0.05),
            () -> robot.driveSpeed = Math.min(1.0, robot.driveSpeed + 0.05)
        ));


        addOption(MenuLib.Option.value(
                () -> String.format("Slow Drive Speed: %.2f", robot.slowDriveSpeed),
                () -> robot.slowDriveSpeed = Math.max(0.0, robot.slowDriveSpeed - 0.05),
                () -> robot.slowDriveSpeed = Math.min(1.0, robot.slowDriveSpeed + 0.05)
        ));

        addOption(MenuLib.Option.action(
                () -> "Right Stick Driving: " + robot.isRightStick,
                robot::flipStick));

        addOption(MenuLib.Option.action(() ->
                "Field Centric: " + robot.isFieldCentric,
                robot::flipFieldCentric));

        addOption(MenuLib.Option.value(
                () -> "Shooter Tolerance: " + robot.shooterTolerance,
                () -> robot.shooterTolerance = Math.max(0, robot.shooterTolerance - 1),
                () -> robot.shooterTolerance =  Math.min(50, robot.shooterTolerance + 1)
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(
                () -> "Back",
                host::goBack
        ));
    }
}
