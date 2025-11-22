package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.*;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;

import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;

import java.lang.Math;

@Autonomous(name = "Autonomous")
public class AutoV1 extends LinearOpMode {

    public class Subsystems {

        public class Shoot1 implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.shootAuto(1);
                return false;
            }
        }
        public Action shoot1() {
            return new Shoot1();
        }

        public class Shoot2 implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.shootAuto(2);
                return false;
            }
        }
        public Action shoot2() {
            return new Shoot2();
        }

        public class Shoot3 implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.shootAuto(3);
                return false;
            }
        }
        public Action shoot3() {
            return new Shoot3();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Subsystems subsystems = new Subsystems();

        Pose2d initialPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                .splineTo(new Vector2d(10, 10), Math.toRadians(0));

        waitForStart();

        Actions.runBlocking(new SequentialAction(
                tab1.build(),
                subsystems.shoot3()
            )
        );
    }
}