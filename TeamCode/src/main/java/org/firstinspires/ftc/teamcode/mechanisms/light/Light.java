package org.firstinspires.ftc.teamcode.mechanisms.light;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.Configuration.ItemConfig;
import org.firstinspires.ftc.teamcode.helpers.BaseSubsystem;

public class Light extends BaseSubsystem {
    private Servo light;
    public String color = null;

    public Light(HardwareMap hardwareMap,
                 Telemetry telemetry,
                 ItemConfig cfg) {
        super(hardwareMap, telemetry, cfg.itemActive);

        ifActive(() -> {
            light = hardwareMap.get(Servo.class, cfg.itemName);
        });
    }

    public void red() {
        ifActive(() -> {
            light.setPosition(0.3);
            color = "Red";
        });
    }

    public void yellow() {
        ifActive(() -> {
            light.setPosition(0.388);
            color = "Yellow";
        });
    }

    public void green() {
        ifActive(() -> {
            light.setPosition(0.5);
            color = "Green";
        });
    }

    public void cycle() {
        if (!active || light == null) return;

        if ("Red".equals(color)) {
            yellow();
        } else if ("Yellow".equals(color)) {
            green();
        } else if ("Green".equals(color)) {
            red();
        } else {
            red();
        }
    }
}
