package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.limelight.Limelight;
import org.firstinspires.ftc.teamcode.options.Globals;
import org.jetbrains.annotations.NotNull;

@Autonomous(name = "Auto (Leave)", group = "OpModes")
public class Backup extends LinearOpMode {

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

        public class ReadMotif implements Action {
            int motif;

            public ReadMotif() { motif = 0; }

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (Limelight.getMotif() != 0) {
                    motif = Limelight.getMotif();
                    return true;
                }
                return false;
            }
        }

        public Action readMotif() { return new ReadMotif(); }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.init(hardwareMap, telemetry);
//        Limelight.init(hardwareMap);

        Subsystems subsystems = new Subsystems();

        Pose2d initialRedPose = new Pose2d(60, 10, Math.toRadians(0));
        Pose2d initialBluePose = new Pose2d(60, -10, Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialRedPose);
        TrajectoryActionBuilder path = drive.actionBuilder(initialRedPose)
                .strafeTo(new Vector2d(57, 30));

        if (Globals.alliance == Globals.Alliances.BLUE) {
            drive = new MecanumDrive(hardwareMap, initialBluePose);
            path = drive.actionBuilder(initialBluePose)
                    .strafeTo(new Vector2d(57, -30));
        } else if (Globals.alliance == Globals.Alliances.RED) {
            drive = new MecanumDrive(hardwareMap, initialRedPose);
            path = drive.actionBuilder(initialRedPose)
                    .strafeTo(new Vector2d(57, 30));
        }
        
        waitForStart();

        Actions.runBlocking(new SequentialAction(
                    path.build()
                )
        );
    }
}