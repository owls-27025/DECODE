package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.opmodes;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsController;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

public class AutoConfig extends MenuLib.Menu {

    public AutoConfig(MenuLib.MenuHost host, Robot robot, OwlsController gamepad1, OwlsController gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "AUTO");

        addOption(MenuLib.Option.enumCycle(
                "Alliance: ",
                Robot.Alliances.class,
                () -> robot.alliance,
                v -> robot.alliance = v
        ));

        addOption(MenuLib.Option.enumCycle(
                "Path: ",
                Robot.AutoStrategies.class,
                () -> robot.autoStrategy,
                v -> robot.autoStrategy = v
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(
                () -> "Ready",
                host::goBack
        ));
    }
}
