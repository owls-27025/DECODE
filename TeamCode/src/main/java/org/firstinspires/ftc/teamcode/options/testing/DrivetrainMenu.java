package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;

public class DrivetrainMenu extends MenuLib.Menu {
    public DrivetrainMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint, Robot robot) {
        super(host, gamepad1, gamepad2, telemetry, "DRIVETRAIN");

        Drivetrain drivetrain = robot.getDrivetrain();

        addOption(new MenuLib.InfoOption(() ->
                "FL Position: " + drivetrain.FL.getCurrentPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "FR Position: " + drivetrain.FR.getCurrentPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "BL Position: " + drivetrain.BL.getCurrentPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "BR Position: " + drivetrain.BR.getCurrentPosition()));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.InfoOption(() ->
                "X Encoder Position: " + pinpoint.getEncoderX()));
        addOption(new MenuLib.InfoOption(() ->
                "Y Encoder Position: " + pinpoint.getEncoderY()));
        addOption(new MenuLib.InfoOption(() ->
                "IMU Rotation (deg): " + pinpoint.getHeading(AngleUnit.DEGREES)));
        addOption(new MenuLib.InfoOption(() ->
                "Robot Position (in): " + pinpoint.getPosX(DistanceUnit.INCH) + ", " + pinpoint.getPosY(DistanceUnit.INCH)));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
