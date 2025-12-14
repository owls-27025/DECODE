package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.testing;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

public class IntakeMenu extends MenuLib.Menu {
    public IntakeMenu(MenuLib.MenuHost host, Robot robot, OwlsGamepad gamepad1, OwlsGamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "INTAKE");

        addOption(MenuLib.Option.action(
                () -> "Start Intake",
                robot.intake::start
        ));

        addOption(MenuLib.Option.action(
                () -> "Reverse Intake",
                robot.intake::reverse
        ));

        addOption(MenuLib.Option.action(
                () -> "Stop Intake",
                robot.intake::stop
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(
                () -> "Back",
                host::goBack
        ));
    }
}
