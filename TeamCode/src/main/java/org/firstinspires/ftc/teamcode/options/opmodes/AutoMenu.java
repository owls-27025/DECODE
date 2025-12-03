package org.firstinspires.ftc.teamcode.options.opmodes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;

public class AutoMenu extends MenuLib.Menu {

    public AutoMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad1, gamepad2, telemetry, "TEST MENU");

        // change side
        addOption(new MenuLib.Option(
                () -> "Side: " + Globals.side,
                Globals::flipSide
        ));

        // change alliance
        addOption(new MenuLib.Option(
                () -> "Alliance: " + Globals.alliance,
                Globals::flipAlliance
        ));

        // change strategy
        addOption(new MenuLib.Option(
                () -> "Strategy: " + Globals.autoStrategy,
                Globals::cycleStrategy
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
