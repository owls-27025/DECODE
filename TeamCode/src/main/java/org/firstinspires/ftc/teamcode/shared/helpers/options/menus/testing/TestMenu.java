package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.testing;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

public class TestMenu extends MenuLib.Menu {

    public TestMenu(MenuLib.MenuHost host, Robot robot, OwlsGamepad gamepad1, OwlsGamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "TESTING");

        addOption(MenuLib.Option.submenu(
                "Spindexer",
                host,
                () -> new SpindexerMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(MenuLib.Option.submenu(
                "Shooter",
                host,
                () -> new ShooterMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(MenuLib.Option.submenu(
                "Intake",
                host,
                () -> new IntakeMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(MenuLib.Option.submenu(
                "Drivetrain",
                host,
                () -> new DrivetrainMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(MenuLib.Option.submenu(
                "Limelight",
                host,
                () -> new LimelightMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(
                () -> "Back",
                host::goBack
        ));
    }
}
