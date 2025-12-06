package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.light.Light;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.helpers.Globals;

public class ShooterMenu extends MenuLib.Menu {
    public ShooterMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad1, gamepad2, telemetry, "SHOOTER");

        addOption(new MenuLib.Option(
                "Start Shooter Motor",
                () -> {
                    ShooterHelper.shooterMotor.setVelocity(Globals.ShooterVelocity);
                }
        ));

        addOption(new MenuLib.IntOption(
                "Shooter Velocity: ",
                1100,
                50,
                0,
                1600,
                value -> Globals.ShooterVelocity = value
        ));

        addOption(new MenuLib.DoubleOption(
                "Servo Position: ",
                0.0,
                0.01,
                0.0,
                1.0,
                2,
                SpindexerHelper::moveServo
        ));

        addOption(new MenuLib.Option(
                "Light Color: " + Light.color,
                Light::cycle
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
