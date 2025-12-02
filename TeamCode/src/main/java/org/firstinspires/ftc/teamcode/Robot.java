package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

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

    private final DistanceSensor distanceSensor;
    public DistanceSensor getDistanceSensor() { return distanceSensor; }
    private final Intake intake;
    public Intake getIntake() { return intake; }
    private final Shooter shooter;
    public Shooter getShooter() { return shooter; }
    private final Spindexer spindexer;
    public Spindexer getSpindexer() { return spindexer; }
    private final Drivetrain drivetrain;
    public Drivetrain getDrivetrain() { return drivetrain; }
    private final Light light;
    public Light getLight() { return light; }
    private final Limelight limelight;
    public Limelight getLimelight() { return limelight; }

    private double spindexerSpeed = 0.5;
    public double getSpindexerSpeed() { return spindexerSpeed; }
    public void setSpindexerSpeed(double value) { spindexerSpeed = value; }
    private boolean isRightStick = false;
    public boolean getRightStick() { return isRightStick; }
    public void setRightStick(boolean value) { isRightStick = value; }
    private double driveSpeed = 1.0;
    public double getDriveSpeed() { return driveSpeed; }
    public void setDriveSpeed(double value) { driveSpeed = value; }
    private double slowDriveSpeed = 0.35;
    public double getSlowDriveSpeed() { return slowDriveSpeed; }
    public void setSlowDriveSpeed(double value) { slowDriveSpeed = value; }
    private boolean isFieldCentric = false;
    public boolean getFieldCentric() { return isFieldCentric; }
    public void setFieldCentric(boolean value) { isFieldCentric = value; }
    private double shooterVelocity = 1100;

    public double getShooterVelocity() { return shooterVelocity; }

    public void setShooterVelocity(double value) { shooterVelocity = value; }

    private int shooterTolerance = 15;
    public int getShooterTolerance() { return shooterTolerance; }
    public void setShooterTolerance(int value) { shooterTolerance = value; }
    private int artifactCount = 3;
    public int getArtifactCount() { return artifactCount; }
    public void setArtifactCount(int value) { artifactCount = value; }
    private int forcedArtifacts = 3;
    public int getForcedArtifacts() { return forcedArtifacts; }
    public void setForcedArtifacts(int value) { forcedArtifacts = value; }
    private boolean isHumanIntaking;
    public boolean getHumanIntaking() { return isHumanIntaking; }
    public void setHumanIntaking(boolean value) { isHumanIntaking = value; }

    public enum AutoStrategies {
        LEAVE,
        FOURCYCLE
    }
    private AutoStrategies autoStrategy;
    public AutoStrategies getAutoStrategy() { return autoStrategy; }
    public void setAutoStrategy(AutoStrategies value) { autoStrategy = value; }

    public enum Alliances {
        RED,
        BLUE
    }
    private Alliances alliance;
    public Alliances getAlliance() { return alliance; }
    public void setAlliance(Alliances value) { alliance = value; }

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
