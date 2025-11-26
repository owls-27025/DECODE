package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.options.MenuLib;

public class SpindexerMenu extends MenuLib.Menu {

    public SpindexerMenu(MenuLib.MenuHost host, Gamepad gamepad, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad, telemetry, "SPINDEXER");

        addOption(new MenuLib.Option(
                "Move Spindexer Forwards",
                () -> SpindexerHelper.moveHalfPosition(true)
        ));

        addOption(new MenuLib.Option(
                "Move Spindexer Backwards",
                () -> SpindexerHelper.moveHalfPosition(false)
        ));

        addOption(new MenuLib.InfoOption(() ->
                ""));

        addOption(new MenuLib.InfoOption(() ->
                "Position: " + SpindexerHelper.SpindexerMotor.getCurrentPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "Relative Position: " + SpindexerHelper.SpindexerMotor.getCurrentPosition() % SpindexerHelper.TPR));
        addOption(new MenuLib.InfoOption(() ->
                "Error: " + Math.abs(SpindexerHelper.SpindexerMotor.getCurrentPosition() - SpindexerHelper.TPR)));
        addOption(new MenuLib.InfoOption(() ->
                        "Distance: " + ColorSensorHelper.colorSensor.getDistance(DistanceUnit.MM)));

        addOption(new MenuLib.InfoOption(() ->
                ""));

        addOption(new MenuLib.SubMenu(
                "Back",
                host,
                () -> new TestMenu(host, gamepad, telemetry, pinpoint)
        ));
    }
}
