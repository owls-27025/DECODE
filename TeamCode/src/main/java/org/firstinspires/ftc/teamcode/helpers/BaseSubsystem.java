package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class BaseSubsystem {
    protected final HardwareMap hardwareMap;
    protected final Telemetry telemetry;
    protected final Configuration configuration;
    protected final boolean active;

    protected BaseSubsystem(HardwareMap hardwareMap,
                            Telemetry telemetry,
                            boolean active) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.active = active;
        this.configuration = new Configuration();
    }

    public boolean isActive() {
        return active;
    }

    public void update() {}

    protected void ifActive(Runnable action) {
        if (active) action.run();
    }
}
