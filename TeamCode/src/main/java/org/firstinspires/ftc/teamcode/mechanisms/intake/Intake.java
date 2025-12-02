package org.firstinspires.ftc.teamcode.mechanisms.intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.Configuration.ItemConfig;
import org.firstinspires.ftc.teamcode.helpers.BaseSubsystem;

public class Intake extends BaseSubsystem {
    public DcMotor intake;

    public Intake(HardwareMap hardwareMap,
                  Telemetry telemetry,
                  ItemConfig cfg) {
        super(hardwareMap, telemetry, cfg.itemActive);

        ifActive(() -> {
            intake = hardwareMap.get(DcMotor.class, cfg.itemName);
            intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        });
    }

    public void start() {
        ifActive(() -> intake.setPower(1));
    }
    public void stop() {
        ifActive(() -> intake.setPower(0));
    }

    public void reverse() {
        ifActive(() -> intake.setPower(-1));
    }

    public void setIntakePower(double power) { intake.setPower(power); }

}

