package org.firstinspires.ftc.teamcode.mechanisms.subsystems.intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Configuration;

public class IntakeHelper {
    public static DcMotor intake;

    public static void init(HardwareMap hardwareMap) {
        if (Configuration.intake.itemActive) {
            intake = hardwareMap.get(DcMotor.class, Configuration.intake.itemName);
            intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public static void start() {
        intake.setPower(1);
    }

    public static void stop() {
        intake.setPower(0);
    }
}