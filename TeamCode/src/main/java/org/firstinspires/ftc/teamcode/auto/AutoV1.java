package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;

@Autonomous(name = "Autonomous")
public class AutoV1 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // once tuning finished:
        // MechanumDrive drive = new MechanumDrive(hardwareMap, new Pose2d(0, 0, 0)
        Subsystems.shootAuto(3);
    }
}