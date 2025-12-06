package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.DistanceSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;

public class SpindexerMenu extends MenuLib.Menu {

    public SpindexerMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad1, gamepad2, telemetry, "SPINDEXER");

        addOption(new MenuLib.Option(
                "Move Spindexer Forwards",
                () -> SpindexerHelper.moveHalfPosition(true)
        ));

        addOption(new MenuLib.Option(
                "Move Spindexer Backwards",
                () -> SpindexerHelper.moveHalfPosition(false)
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.InfoOption(() ->
                "Position: " + SpindexerHelper.SpindexerMotor.getCurrentPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "Target Position: " + SpindexerHelper.SpindexerMotor.getTargetPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "Relative Position: " + SpindexerHelper.SpindexerMotor.getCurrentPosition() % SpindexerHelper.TPR));
        addOption(new MenuLib.InfoOption(() ->
                "Relative Target Position: " + SpindexerHelper.SpindexerMotor.getTargetPosition() % SpindexerHelper.TPR));
        addOption(new MenuLib.InfoOption(() ->
                        "Distance: " + DistanceSensorHelper.colorSensor.getDistance(DistanceUnit.MM)));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.DoubleOption(
                "Servo Down Position: ",
                Globals.servoDown,
                0.01,
                0.0,
                1.0,
                2,
                value -> Globals.servoDown = value
        ));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
