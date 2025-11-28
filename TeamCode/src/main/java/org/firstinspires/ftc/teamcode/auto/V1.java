package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.*;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;

@Autonomous(name = "Autonomous", group = "OpModes")
public class V1 extends LinearOpMode {

    public class Subsystems {

        public class Shoot implements Action {
            public int artifacts;
            public boolean initialized;

            public Shoot(int numArtifacts) {
                artifacts = numArtifacts;
                initialized = false;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.currentShootState = org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.ShootState.MOVING_TO_SHOOT_POSITION;
                    initialized = true;
                }
                return org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.shootAuto(artifacts);
            }
        }

        public Action shoot(int artifacts) {
            return new Shoot(artifacts);
        }

        public class Intake implements Action {
            public boolean initialized;

            public Intake() {
                initialized = false;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.currentState = org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.IntakeState.MOVING_TO_POSITION;
                    initialized = true;
                }
                return org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.intakeAuto();
            }
        }

        public Action intake() {
            return new Intake();
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