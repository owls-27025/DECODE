package org.firstinspires.ftc.teamcode.shared.options.opmodes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.options.MenuLib;

public class SettingsMainMenu extends MenuLib.Menu {

    public SettingsMainMenu(
            MenuLib.MenuHost host,
            Robot robot,
            Gamepad gamepad1,
            Gamepad gamepad2,
            Telemetry telemetry,
            GoBildaPinpointDriver pinpoint
    ) {
        super(host, gamepad1, gamepad2, telemetry, "SETTINGS");

        addOption(new MenuLib.SubMenu(
                "TeleOp",
                host,
                new java.util.function.Supplier<MenuLib.Menu>() {
                    @Override public MenuLib.Menu get() {
                        return new TeleOpMenu(host, robot, gamepad1, gamepad2, telemetry, pinpoint);
                    }
                }
        ));

        addOption(new MenuLib.SubMenu(
                "Auto",
                host,
                new java.util.function.Supplier<MenuLib.Menu>() {
                    @Override public MenuLib.Menu get() {
                        return new AutoMenu(host, robot, gamepad1, gamepad2, telemetry, pinpoint);
                    }
                }
        ));

        addOption(new MenuLib.SubMenu(
                "Testing",
                host,
                new java.util.function.Supplier<MenuLib.Menu>() {
                    @Override public MenuLib.Menu get() {
                        return new org.firstinspires.ftc.teamcode.shared.options.testing.TestMenu(host, robot, gamepad1, gamepad2, telemetry, pinpoint);
                    }
                }
        ));

        addOption(new MenuLib.InfoOption(""));
        addOption(new MenuLib.Option("Exit", new Runnable() {
            @Override public void run() {
                host.goBack();
            }
        }));
    }
}
