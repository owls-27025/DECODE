package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.BaseOpMode;
import org.firstinspires.ftc.teamcode.shared.options.MenuHostImpl;
import org.firstinspires.ftc.teamcode.shared.options.opmodes.SettingsMainMenu;

@TeleOp(name = "Settings", group = "Settings")
public class Settings extends BaseOpMode {
    MenuHostImpl host;
    @Override
    public void onInit() {
        GoBildaPinpointDriver pinpoint =
                hardwareMap.get(GoBildaPinpointDriver.class, robot.config.odometry.itemName);

        host = new MenuHostImpl();
        host.setRoot(new SettingsMainMenu(host, robot, gamepad1, gamepad2, telemetry, pinpoint));

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
