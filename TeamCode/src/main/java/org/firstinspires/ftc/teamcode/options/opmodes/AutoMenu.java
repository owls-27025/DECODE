package org.firstinspires.ftc.teamcode.options.opmodes;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.Robot;

public class AutoMenu extends MenuLib.Menu {

    public AutoMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, Robot robot) {
        super(host, gamepad1, gamepad2, telemetry, "AUTO");

        // change alliance
        addOption(new MenuLib.Option(
                () -> "Alliance: " + robot.alliance,
                () -> robot.alliance = (robot.alliance == Robot.Alliances.RED ? Robot.Alliances.BLUE : Robot.Alliances.RED)
        ));

        // change strategy
        addOption(new MenuLib.Option(
                () -> "Strategy: " + robot.autoStrategy,
                () -> {
                    Robot.AutoStrategies[] vals = Robot.AutoStrategies.values();
                    robot.autoStrategy = vals[(robot.autoStrategy.ordinal() + 1) % vals.length];
                }
        ));


        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
