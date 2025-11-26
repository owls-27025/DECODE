package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.options.MenuLib;
import org.firstinspires.ftc.teamcode.options.Settings;

public class TestMenu extends MenuLib.Menu {

    public TestMenu(MenuLib.MenuHost host, Gamepad gamepad, Telemetry telemetry) {
        super(host, gamepad, telemetry, "TEST MENU");

        addOption(new MenuLib.SubMenu(
                "Spindexer",
                host,
                () -> new SpindexerMenu(host, gamepad, telemetry)
        ));

        addOption(new MenuLib.SubMenu(
                "Shooter",
                host,
                () -> new ShooterMenu(host, gamepad, telemetry)
        ));

        addOption(new MenuLib.Option(
                "Back",
                host::goToMainMenu
        ));
    }
}
