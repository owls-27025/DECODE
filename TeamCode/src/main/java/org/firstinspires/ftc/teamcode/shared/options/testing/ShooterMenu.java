package org.firstinspires.ftc.teamcode.shared.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.options.MenuLib;

public class ShooterMenu extends MenuLib.Menu {
    public ShooterMenu(MenuLib.MenuHost host, Robot robot, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "SHOOTER");

        addOption(new MenuLib.Option(
                "Start Shooter Motor",
                () -> robot.shooter.shoot(robot.shooterVelocity)
        ));

        addOption(new MenuLib.IntOption(
                "Shooter Velocity: ",
                robot.shooterVelocity,
                50,
                0,
                1600,
                value -> robot.shooterVelocity = value
        ));

        addOption(new MenuLib.DoubleOption(
                "Flap Position: ",
                robot.servoDownPos,
                0.01,
                0.0,
                1.0,
                2,
                robot.spindexer::setFlapPosition
        ));

        addOption(new MenuLib.Option(
                () -> "Light Color: " + robot.light.get(),
                robot.light::cycle
        ));

        addOption(new MenuLib.InfoOption(() -> ""));
        addOption(new MenuLib.Option("Back", host::goBack));
    }
}
