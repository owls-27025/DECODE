package org.firstinspires.ftc.teamcode.options;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;

public class TestMenu extends MenuLib.Menu {

    public TestMenu(MenuLib.MenuHost host, Gamepad gamepad, Telemetry telemetry) {
        super(host, gamepad, telemetry, "TEST MENU");

        addOption(new MenuLib.Option(
                "Move Spindexer Forwards",
                () -> SpindexerHelper.moveHalfPosition(true)
        ));

        addOption(new MenuLib.Option(
                "Move Spindexer Backwards",
                () -> SpindexerHelper.moveHalfPosition(false)
        ));

        addOption(new MenuLib.Option(
                "Back",
                host::goToMainMenu
        ));
    }
}
