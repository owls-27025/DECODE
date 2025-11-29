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

        mainMenu = new MenuLib.Menu(this, gamepad1, gamepad2, telemetry, "SETTINGS") {
            {
                // add side change
                addOption(new MenuLib.Option(
                        () -> "Side: " + Globals.side,
                        Globals::flipSide
                ));

                // add alliance change
                addOption(new MenuLib.Option(
                        () -> "Alliance: " + Globals.alliance,
                        Globals::flipAlliance
                ));

                // change spindexer speed
                addOption(new MenuLib.DoubleOption(
                        "Spindexer Speed: ",
                        Globals.SpindexerSpeed,
                        0.05,
                        0.0,
                        1.0,
                        2,
                        value -> Globals.SpindexerSpeed = value
                ));

                // change driving speed
                addOption(new MenuLib.DoubleOption(
                        "Drive Speed: ",
                        Globals.DriveSpeed,
                        0.05,
                        0.0,
                        1.0,
                        2,
                        value -> Globals.DriveSpeed = value
                ));

                // change slow driving speed
                addOption(new MenuLib.DoubleOption(
                        "Slow Drive Speed: ",
                        Globals.SlowDriveSpeed,
                        0.05,
                        0.0,
                        1.0,
                        2,
                        value -> Globals.SlowDriveSpeed = value
                ));

                // change number of forced artifacts for y button
                addOption(new MenuLib.IntOption(
                        "Forced Artifacts: ",
                        Globals.ForcedArtifacts,
                        1,
                        1,
                        3,
                        value -> Globals.ForcedArtifacts = value
                ));

                // toggle right stick driving
                addOption(new MenuLib.Option(() ->
                        "Right Stick Driving: " + Globals.isRightStick,
                        Globals::flipStick));

                // toggle field centric
                addOption(new MenuLib.Option(() ->
                        "Field Centric: " + Globals.isFieldCentric,
                        Globals::flipFieldCentric));

                // shooter tolerance setting
                addOption(new MenuLib.IntOption(
                        "Shooter Tolerance: ",
                        15,
                        1,
                        0,
                        15,
                        value -> Globals.ShooterTolerance = value
                ));

                addOption(new MenuLib.IntOption(
                        "Human Player Intake: ",
                        750,
                        250,
                        100,
                        2000,
                        value -> Globals.humanWait = value
                ));

                // blank line
                addOption(new MenuLib.InfoOption(() ->
                        ""));

                // go to testing submenu
                addOption(new MenuLib.SubMenu(
                        "Testing",
                        Settings.this,
                        () -> new TestMenu(Settings.this, gamepad1, gamepad2, telemetry, pinpoint)
                ));

                // exit opmode
                addOption(new MenuLib.Option(
                        "Exit",
                        () -> {
                            isFinished.set(true);
                        }
                ));
            }
        };

        // run menu code
        mainMenu.onSelected();
        currentMenu = mainMenu;

        waitForStart();

        while (opModeIsActive() && !isFinished.get()) {
            if (currentMenu != null) {
                currentMenu.loop();
            }
        }

        // run when exiting
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
