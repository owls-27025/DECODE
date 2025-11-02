package org.firstinspires.ftc.teamcode.mechanisms.spindexer.intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Config;

public class IntakeHelper {
    public static DcMotor intake;

    public static void init(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, Config.intake);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public static void start() {
        intake.setPower(0.5);
    }

    public static void stop() {
        intake.setPower(0);
    }
}