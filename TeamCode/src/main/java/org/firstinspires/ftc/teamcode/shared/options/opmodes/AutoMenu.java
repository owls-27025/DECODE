package org.firstinspires.ftc.teamcode.shared.options.opmodes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.options.MenuLib;

public class AutoMenu extends MenuLib.Menu {

    public AutoMenu(MenuLib.MenuHost host, Robot robot, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad1, gamepad2, telemetry, "AUTO");

        addOption(new MenuLib.Option(
                () -> "Side: " + robot.side,
                robot::flipSide
        ));

        addOption(new MenuLib.Option(
                () -> "Alliance: " + robot.alliance,
                robot::flipAlliance
        ));

        addOption(new MenuLib.Option(
                () -> "Strategy: " + robot.autoStrategy,
                robot::cycleStrategy
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
