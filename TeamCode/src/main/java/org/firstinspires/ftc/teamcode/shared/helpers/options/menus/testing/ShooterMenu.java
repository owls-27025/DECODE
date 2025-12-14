package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.testing;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

public class ShooterMenu extends MenuLib.Menu {
    public ShooterMenu(MenuLib.MenuHost host, Robot robot, OwlsGamepad gamepad1, OwlsGamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "SHOOTER");

        addOption(MenuLib.Option.action(
                () -> "Start Motor",
                () -> robot.shooter.shoot(robot.shooterVelocity)
        ));

        addOption(MenuLib.Option.value(
                () -> "Target Velocity: " + robot.shooterVelocity,
                () -> {
                    robot.shooterVelocity -= 10;
                    robot.shooter.shoot(robot.shooterVelocity);
                },
                () -> {
                    robot.shooterVelocity += 10;
                    robot.shooter.shoot(robot.shooterVelocity);
                }
        ));

        addOption(MenuLib.Option.action(
                () -> "Stop Motor",
                () -> robot.shooter.shoot(0)
        ));

        addOption(MenuLib.Option.info(
                () -> "Current Velocity: " + robot.shooter.getVelocity()
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.value(
                () -> "Flap Position: .%2f" + robot.spindexer.getFlapPosition(),
                () -> robot.spindexer.setFlapPosition(robot.spindexer.getFlapPosition() - 0.05),
                () -> robot.spindexer.setFlapPosition(robot.spindexer.getFlapPosition() + 0.05)
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(
                () -> "Light Color: " + robot.light.get(),
                robot.light::cycle
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(() -> "Back", host::goBack));
    }
}
