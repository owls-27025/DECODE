package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.options.MenuLib;

public class SpindexerMenu extends MenuLib.Menu {

    public SpindexerMenu(MenuLib.MenuHost host, Gamepad gamepad, Telemetry telemetry) {
        super(host, gamepad, telemetry, "SPINDEXER");

        addOption(new MenuLib.Option(
                "Move Spindexer Forwards",
                () -> SpindexerHelper.moveHalfPosition(true)
        ));

        addOption(new MenuLib.Option(
                "Move Spindexer Backwards",
                () -> SpindexerHelper.moveHalfPosition(false)
        ));

//        telemetry.addData("Position: ", SpindexerHelper.SpindexerMotor.getCurrentPosition());
//        telemetry.addData("Relative Position: ", SpindexerHelper.SpindexerMotor.getCurrentPosition() % SpindexerHelper.TPR);
//        telemetry.addData("Error: ", Math.abs(SpindexerHelper.SpindexerMotor.getCurrentPosition() - SpindexerHelper.TPR));
//        telemetry.addData("Distance: ", ColorSensorHelper.colorSensor.getDistance(DistanceUnit.MM));

        addOption(new MenuLib.SubMenu(
                "Back",
                host,
                () -> new TestMenu(host, gamepad, telemetry)
        ));
    }
}
