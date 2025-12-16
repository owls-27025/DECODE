package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.opmodes;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

public class TeleOpMenu extends MenuLib.Menu {

    public TeleOpMenu(MenuLib.MenuHost host, Robot robot, OwlsGamepad gamepad1, OwlsGamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "TELEOP");

        addOption(MenuLib.Option.value(
            () -> String.format("Drive Speed: %.2f", Robot.Globals.driveSpeed),
            () -> Robot.Globals.driveSpeed = Math.max(0.0, Robot.Globals.driveSpeed - 0.05),
            () -> Robot.Globals.driveSpeed = Math.min(1.0, Robot.Globals.driveSpeed + 0.05)
        ));


        addOption(MenuLib.Option.value(
                () -> String.format("Slow Drive Speed: %.2f", Robot.Globals.slowDriveSpeed),
                () -> Robot.Globals.slowDriveSpeed = Math.max(0.0, Robot.Globals.slowDriveSpeed - 0.05),
                () -> Robot.Globals.slowDriveSpeed = Math.min(1.0, Robot.Globals.slowDriveSpeed + 0.05)
        ));

        addOption(MenuLib.Option.action(
                () -> "Right Stick Driving: " + Robot.Globals.isRightStick,
                Robot.Globals::flipStick));

        addOption(MenuLib.Option.action(() ->
                "Field Centric: " + Robot.Globals.isFieldCentric,
                Robot.Globals::flipFieldCentric));

        addOption(MenuLib.Option.value(
                () -> "Shooter Tolerance: " + Robot.Globals.shooterTolerance,
                () -> Robot.Globals.shooterTolerance = Math.max(0, Robot.Globals.shooterTolerance - 1),
                () -> Robot.Globals.shooterTolerance =  Math.min(50, Robot.Globals.shooterTolerance + 1)
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(
                () -> "Back",
                host::goBack
        ));
    }
}
