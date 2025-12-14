package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.shared.mechanisms.distance.Distance;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.shared.mechanisms.intake.Intake;
import org.firstinspires.ftc.teamcode.shared.mechanisms.light.Light;
import org.firstinspires.ftc.teamcode.shared.mechanisms.limelight.Limelight;
import org.firstinspires.ftc.teamcode.shared.mechanisms.shooter.Shooter;
import org.firstinspires.ftc.teamcode.shared.mechanisms.spindexer.Spindexer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Proxy;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Robot {
    public final HardwareMap hw;

    // ----------------------------
    // Config
    // ----------------------------
    public final Config config = new Config();

    public static class Config {
        public final ArrayList<ConfigItem> configItems = new ArrayList<>();

        public final ConfigItem distance       = item("distance", true);

        public final ConfigItem FR             = item("FR", true);
        public final ConfigItem FL             = item("FL", true);
        public final ConfigItem BR             = item("BR", true);
        public final ConfigItem BL             = item("BL", true);

        public final ConfigItem intake         = item("intake", true);
        public final ConfigItem spindexerMotor = item("spindexer", true);
        public final ConfigItem spindexerServo = item("flap", true);
        public final ConfigItem shooter        = item("shooter", true);

        public final ConfigItem odometry       = item("odometry", true);
        public final ConfigItem light          = item("light", true);
        public final ConfigItem imu            = item("imu", true);
        public final ConfigItem limelight      = item("limelight", true);

        @SuppressWarnings("SameParameterValue")
        private ConfigItem item(String name, boolean active) {
            ConfigItem c = new ConfigItem(name, active);
            configItems.add(c);
            return c;
        }

        public static class ConfigItem {
            public final String itemName;
            public boolean itemActive;

            public ConfigItem(String key, boolean isActive) {
                this.itemName = key;
                this.itemActive = isActive;
            }
        }
    }

    public <T extends HardwareDevice> T registerItem(Class<T> type, Config.ConfigItem cfg) {
        if (cfg == null || !cfg.itemActive) return noop(type);

        try {
            return hw.get(type, cfg.itemName);
        } catch (Exception e) {
            cfg.itemActive = false;
            return noop(type);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T noop(Class<T> iface) {
        if (!iface.isInterface()) return null;

        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[]{iface},
                (proxy, method, args) -> {
                    Class<?> r = method.getReturnType();

                    if (r == boolean.class) return false;
                    if (r == byte.class) return (byte) 0;
                    if (r == short.class) return (short) 0;
                    if (r == int.class) return 0;
                    if (r == long.class) return 0L;
                    if (r == float.class) return 0f;
                    if (r == double.class) return 0d;
                    if (r == char.class) return '\0';

                    return null;
                }
        );
    }

    // ----------------------------
    // Subsystems
    // ----------------------------
    public final Spindexer spindexer;
    public final Intake intake;
    public final Distance distance;
    public final Drivetrain drivetrain;
    public final Shooter shooter;
    public final Light light;
    public final Limelight limelight;

    // ----------------------------
    // Robot State
    // ----------------------------
    public int artifactCount;
    public boolean isHumanIntake;

    public boolean intakeReversed;

    // ----------------------------
    // Constants
    // ----------------------------
    public double currentSpeed;

    public double spindexerSpeed;
    public double driveSpeed;
    public double slowDriveSpeed;

    public int shooterVelocity;
    public int shooterTolerance;

    public int forcedArtifacts;

    public boolean isFieldCentric;
    public boolean isRightStick;

    public int humanWaitMs;
    public int spindexerShootTimeTicks;

    public int tpr;

    public enum Alliances { RED, BLUE }
    public Alliances alliance;

    public enum Sides { GOAL, WALL }
    public Sides side;

    public enum AutoStrategies {
        LEAVE("Leave"),
        ONECYCLEFRONT("One Cycle Front"),
        ONECYCLEBACK("One Cycle Back");

        public final String displayName;

        AutoStrategies(String displayName) {
            this.displayName = displayName;
        }

        @NotNull
        @Override
        public String toString() {
            return displayName;
        }
    }

    public AutoStrategies autoStrategy;

    public enum Colors {
        PPG(0),
        PGP(1),
        GPP(2);

        public final int index;
        Colors(int index) { this.index = index; }
    }
    public Colors motif;

    public double servoDownPos;
    public double intakeSpeed;

    // ----------------------------
    // Helpers
    // ----------------------------
    public void flipStick() { isRightStick = !isRightStick; }

    public void flipFieldCentric() { isFieldCentric = !isFieldCentric; }

    public void flipAlliance() {
        alliance = (alliance == Alliances.RED) ? Alliances.BLUE : Alliances.RED;
    }

    public void flipSide() {
        side = (side == Sides.GOAL) ? Sides.WALL : Sides.GOAL;
    }

    public void cycleStrategy() {
        switch (autoStrategy) {
            case LEAVE:         autoStrategy = AutoStrategies.ONECYCLEFRONT; break;
            case ONECYCLEFRONT:  autoStrategy = AutoStrategies.ONECYCLEBACK;  break;
            case ONECYCLEBACK:   autoStrategy = AutoStrategies.LEAVE;         break;
        }
    }

    public static double easeInOutSine(double x) {
        x = Math.max(-1.0, Math.min(1.0, x));
        double sign = Math.signum(x);
        x = Math.abs(x);
        double eased = Math.sin((Math.PI / 2) * x);
        return eased * sign;
    }

    public Telemetry telemetry;

    // ----------------------------
    // Constructor
    // ----------------------------
    public Robot(HardwareMap hw, Telemetry telemetry) {
        this.hw = hw;

        artifactCount = 0;
        isHumanIntake = false;
        intakeReversed = false;

        currentSpeed = 1.0;
        spindexerSpeed = 0.5;
        driveSpeed = 1.0;
        slowDriveSpeed = 0.35;
        shooterVelocity = 1050;
        shooterTolerance = 50;
        forcedArtifacts = 1;
        isFieldCentric = false;
        isRightStick = false;
        humanWaitMs = 750;
        spindexerShootTimeTicks = 15;
        tpr = 145;
        alliance = Alliances.RED;
        side = Sides.GOAL;
        autoStrategy = AutoStrategies.ONECYCLEFRONT;
        motif = Colors.PPG;
        servoDownPos = 0.52;
        intakeSpeed = 0.6;

        spindexer = new Spindexer(this);
        shooter = new Shooter(this);
        drivetrain = new Drivetrain(this);
        light = new Light(this);
        limelight = new Limelight(this);
        distance = new Distance(this);
        intake = new Intake(this);

        this.telemetry = telemetry;
    }

    public void update() {
        drivetrain.update();
    }
}
