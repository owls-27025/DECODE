package org.firstinspires.ftc.teamcode.options;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.options.testing.TestMenu;

import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp
public class Settings extends LinearOpMode implements MenuLib.MenuHost {
    private MenuLib.Menu mainMenu;
    private MenuLib.Menu currentMenu;

    @Override
    public void runOpMode() {
        Subsystems.init(hardwareMap, telemetry);
        Drivetrain.init(hardwareMap);

        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Configuration.odometry.itemName);

        AtomicBoolean isFinished = new AtomicBoolean(false);

        mainMenu = new MenuLib.Menu(this, gamepad1, telemetry, "SETTINGS") {
            {
                addOption(new MenuLib.Option(
                        () -> "Side: " + Globals.side,
                        Globals::flipSide
                ));

                addOption(new MenuLib.Option(
                        () -> "Alliance: " + Globals.alliance,
                        Globals::flipAlliance
                ));

                addOption(new MenuLib.DoubleOption(
                        "Spindexer Speed: ",
                        Globals.SpindexerSpeed,
                        0.05,
                        0.0,
                        1.0,
                        2,
                        value -> Globals.SpindexerSpeed = value
                ));

                addOption(new MenuLib.DoubleOption(
                        "Drive Speed: ",
                        Globals.DriveSpeed,
                        0.05,
                        0.0,
                        1.0,
                        2,
                        value -> Globals.DriveSpeed = value
                ));

                addOption(new MenuLib.DoubleOption(
                        "Slow Drive Speed: ",
                        Globals.SlowDriveSpeed,
                        0.05,
                        0.0,
                        1.0,
                        2,
                        value -> Globals.SlowDriveSpeed = value
                ));

                addOption(new MenuLib.IntOption(
                        "Forced Artifacts: ",
                        Globals.ForcedArtifacts,
                        1,
                        1,
                        3,
                        value -> Globals.ForcedArtifacts = value
                ));

                addOption(new MenuLib.Option(() ->
                        "Right Stick Driving: " + Globals.isRightStick,
                        Globals::flipStick));

                addOption(new MenuLib.Option(() ->
                        "Field Centric: " + Globals.isFieldCentric,
                        Globals::flipFieldCentric));

                addOption(new MenuLib.InfoOption(() ->
                        ""));

                addOption(new MenuLib.SubMenu(
                        "Testing",
                        Settings.this,
                        () -> new TestMenu(Settings.this, gamepad1, telemetry, pinpoint)
                ));

                addOption(new MenuLib.Option(
                        "Exit",
                        () -> {
                            isFinished.set(true);
                        }
                ));
            }
        };

        mainMenu.onSelected();
        currentMenu = mainMenu;

        waitForStart();

        while (opModeIsActive() && !isFinished.get()) {
            if (currentMenu != null) {
                currentMenu.loop();
            }
        }

        telemetry.addLine("exiting...");
        telemetry.update();
        sleep(1500);
    }


    @Override
    public void setCurrentMenu(MenuLib.Menu menu) {
        currentMenu = menu;
    }

    @Override
    public void goToMainMenu() {
        mainMenu.onSelected();
        currentMenu = mainMenu;
    }
}
