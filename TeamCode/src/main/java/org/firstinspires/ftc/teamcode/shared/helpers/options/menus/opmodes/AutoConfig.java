package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.opmodes;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoParams;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

public class AutoConfig extends MenuLib.Menu {

    private final AutoParams params;

    public AutoConfig(
            MenuLib.MenuHost host,
            AutoParams params,
            OwlsGamepad gamepad1,
            OwlsGamepad gamepad2,
            Telemetry telemetry
    ) {
        super(host, gamepad1, gamepad2, telemetry, "AUTO");

        this.params = params;

        addOption(MenuLib.Option.enumCycle(
                "Alliance: ",
                AutoParams.Alliances.class,
                () -> params.alliance,
                v -> params.alliance = v
        ));

        addOption(MenuLib.Option.enumCycle(
                "Strategy: ",
                AutoParams.Strategies.class,
                () -> params.strategy,
                v -> params.strategy = v
        ));

        addOption(MenuLib.Option.value(
                () -> "Spikes: " + params.spikes,
                () -> params.spikes = Math.max(params.spikes - 1, 0),
                () -> params.spikes= Math.min(params.spikes + 1, 3)
        ));

        addOption(MenuLib.Option.value(
                () -> "Gate: " + params.gate,
                () -> params.gate = !params.gate,
                () -> params.gate = !params.gate
        ));

        addOption(MenuLib.Option.value(
                () -> "Human Player: " + params.humanPlayer,
                () -> params.humanPlayer = !params.humanPlayer,
                () -> params.humanPlayer = !params.humanPlayer
        ));

        addOption(MenuLib.Option.value(
                () -> "Delay: " + params.delay + " second(s)",
                () -> params.delay--,
                () -> params.delay++
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(
                () -> "Ready",
                host::goBack
        ));
    }

    public AutoParams getParams() {
        return params;
    }
}
