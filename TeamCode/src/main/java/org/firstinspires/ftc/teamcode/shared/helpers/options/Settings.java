package org.firstinspires.ftc.teamcode.shared.helpers.options;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuHostImpl;
import org.firstinspires.ftc.teamcode.shared.helpers.options.menus.SettingsMenu;

@TeleOp(name = "Settings", group = "Settings")
@SuppressWarnings("unused")
public class Settings extends OwlsOpMode {
    MenuHostImpl host;
    @Override
    public void onInit() {
        host = new MenuHostImpl();
        host.setRoot(new SettingsMenu(host, robot, p1, p2, telemetry));

        overrideTelemetry = true;

        telemetry.addLine("Settings ready");
        telemetry.update();
    }

    @Override
    public void runLoop() {
        if (host.isActive) {
            host.update();
        } else {
            requestOpModeStop();
        }
    }
}
