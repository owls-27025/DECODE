package org.firstinspires.ftc.teamcode.shared.helpers.options.menus;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsController;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;
import org.firstinspires.ftc.teamcode.shared.helpers.options.menus.opmodes.AutoMenu;
import org.firstinspires.ftc.teamcode.shared.helpers.options.menus.opmodes.TeleOpMenu;
import org.firstinspires.ftc.teamcode.shared.helpers.options.menus.testing.TestMenu;

public class SettingsMenu extends MenuLib.Menu {

    public SettingsMenu(
            MenuLib.MenuHost host,
            Robot robot,
            OwlsController gamepad1,
            OwlsController gamepad2,
            Telemetry telemetry
    ) {
        super(host, gamepad1, gamepad2, telemetry, "SETTINGS");

        addOption(MenuLib.Option.submenu("TeleOp", host,
                () -> new TeleOpMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(MenuLib.Option.submenu("Auto", host,
                () -> new AutoMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(MenuLib.Option.submenu("Testing", host,
                () -> new TestMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(MenuLib.Option.info(() -> "")); // spacer

        addOption(MenuLib.Option.action(
                () -> "Exit",
                host::goBack
        ));
    }
}
