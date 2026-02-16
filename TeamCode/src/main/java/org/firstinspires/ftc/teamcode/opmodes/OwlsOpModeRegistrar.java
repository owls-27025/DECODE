package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmodes.tele.TeleOpMode;
import org.firstinspires.ftc.teamcode.opmodes.testing.PIDTuning;
import org.firstinspires.ftc.teamcode.opmodes.testing.PIDTuningSpindexer;
import org.firstinspires.ftc.teamcode.opmodes.testing.ServoTest;
import org.firstinspires.ftc.teamcode.opmodes.testing.Settings;
import org.firstinspires.ftc.teamcode.opmodes.testing.ShooterSpinupStopwatch;
@SuppressWarnings("unused")
public class OwlsOpModeRegistrar {
    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        manager.register(
                new OpModeMeta.Builder()
                        .setName("Tele")
                        .setGroup("OpModes")
                        .setFlavor(OpModeMeta.Flavor.TELEOP)
                        .build(),
                TeleOpMode.class
        );

        manager.register(
                new OpModeMeta.Builder()
                        .setName("Auto")
                        .setGroup("OpModes")
                        .setFlavor(OpModeMeta.Flavor.AUTONOMOUS)
                        .build(),
                AutoOpMode.class
        );

        manager.register(
                new OpModeMeta.Builder()
                        .setName("Settings")
                        .setGroup("Settings")
                        .setFlavor(OpModeMeta.Flavor.TELEOP)
                        .build(),
                Settings.class
        );

        manager.register(
                new OpModeMeta.Builder()
                        .setName("PID Tuning")
                        .setGroup("Tests")
                        .setFlavor(OpModeMeta.Flavor.TELEOP)
                        .build(),
                PIDTuning.class
        );

        manager.register(
                new OpModeMeta.Builder()
                        .setName("Spindexer PID Tuning")
                        .setGroup("Tests")
                        .setFlavor(OpModeMeta.Flavor.TELEOP)
                        .build(),
                PIDTuningSpindexer.class
        );

        manager.register(
                new OpModeMeta.Builder()
                        .setName("Servo Test")
                        .setGroup("Test")
                        .setFlavor(OpModeMeta.Flavor.TELEOP)
                        .build(),
                ServoTest.class
        );
        manager.register(
                new OpModeMeta.Builder()
                        .setName("Shooter Spinup Stopwatch")
                        .setGroup("Tests")
                        .setFlavor(OpModeMeta.Flavor.TELEOP)
                        .build(),
                ShooterSpinupStopwatch.class
        );

    }
}
