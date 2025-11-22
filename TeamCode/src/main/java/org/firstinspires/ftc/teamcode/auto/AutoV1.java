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

        public class Shoot implements Action {
            public int artifacts;

            public Shoot(int numArtifacts) {
                artifacts = numArtifacts;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                try {
                    org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.shootAuto(artifacts);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        }
        public Action shoot(int artifacts) {
            return new Shoot(artifacts);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Subsystems subsystems = new Subsystems();

        Pose2d initialPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                .splineToConstantHeading(new Vector2d(10, 10), 0);

        waitForStart();

        Actions.runBlocking(new SequentialAction(
                tab1.build(),
                subsystems.shoot(3)
            )
        );
    }
}