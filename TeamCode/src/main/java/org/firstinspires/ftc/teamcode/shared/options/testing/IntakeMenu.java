package org.firstinspires.ftc.teamcode.shared.options.testing;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.mechanisms.intake.Intake;
import org.firstinspires.ftc.teamcode.shared.options.MenuLib;

public class IntakeMenu extends MenuLib.Menu {
    public IntakeMenu(MenuLib.MenuHost host, Robot robot, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "INTAKE");

        addOption(new MenuLib.DoubleOption(
                "Intake Power: ",
                0.0,
                0.05,
                0.0,
                1.0,
                2,
                robot.intake::setPower
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
