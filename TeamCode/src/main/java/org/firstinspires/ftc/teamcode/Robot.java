package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.Configuration;
import org.firstinspires.ftc.teamcode.helpers.Controls;
import org.firstinspires.ftc.teamcode.mechanisms.distanceSensor.DistanceSensor;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.intake.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.light.Light;
import org.firstinspires.ftc.teamcode.mechanisms.limelight.Limelight;
import org.firstinspires.ftc.teamcode.mechanisms.shooter.Shooter;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;

public class Robot {
    private final Controls controls;

    public final DistanceSensor distanceSensor;
    public final Intake intake;
    public final Shooter shooter;
    public final Spindexer spindexer;
    public final Drivetrain drivetrain;
    public final Light light;
    public final Limelight limelight;

    public double SpindexerSpeed = 0.5;
    public boolean isRightStick = false;
    public double driveSpeed = 1.0;
    public double slowDriveSpeed = 0.35;
    public boolean isFieldCentric = false;
    public double shooterVelocity = 1100;
    public double shooterTolerance = 15;
    public int artifactCount = 3;
    public int forcedArtifacts = 3;
    public boolean isHumanIntake;

    public enum AutoStrategies {
        LEAVE,
        FOURCYCLE
    }
    public AutoStrategies autoStrategy;

    public enum Alliances {
        RED,
        BLUE
    }
    public Alliances alliance;

    public Robot(HardwareMap hw, Telemetry telemetry, Controls controls, Configuration config) {
        this.controls = controls;

        distanceSensor = new DistanceSensor(hw, telemetry, config.distance);
        intake         = new Intake(hw, telemetry, config.intake);
        shooter        = new Shooter(hw, telemetry, config.shooter, this);
        spindexer      = new Spindexer(hw, telemetry, config.spindexerMotor, config.spindexerServo, this);
        drivetrain     = new Drivetrain(hw, telemetry, config, this);
        light          = new Light(hw, telemetry, config.light);
        limelight      = new Limelight(hw, telemetry, config.limelight);
    }

    public void loop(Gamepad gamepad1, Gamepad gamepad2) {
        drivetrain.drive(controls);
    }
}
