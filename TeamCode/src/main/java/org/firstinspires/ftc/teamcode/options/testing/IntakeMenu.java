package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.intake.IntakeHelper;
import org.firstinspires.ftc.teamcode.options.MenuLib;

public class IntakeMenu extends MenuLib.Menu {
    public IntakeMenu(MenuLib.MenuHost host, Gamepad gamepad, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad, telemetry, "INTAKE");

        addOption(new MenuLib.DoubleOption(
                "Intake Power",
                0.0,
                0.05,
                0.0,
                1.0,
                2,
                value -> IntakeHelper.intake.setPower(value)
        ));

        addOption(new MenuLib.InfoOption(() ->
                ""));

        addOption(new MenuLib.SubMenu(
                "Back",
                host,
                () -> new TestMenu(host, gamepad, telemetry, pinpoint)
        ));
    }
}
