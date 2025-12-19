package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmodes.tele.TeleOpMode;
import org.firstinspires.ftc.teamcode.shared.helpers.options.Settings;

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
    }
}
