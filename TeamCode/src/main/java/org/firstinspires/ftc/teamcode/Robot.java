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
    // ----------------------------
    // Config
    // ----------------------------
    public Configuration configuration = new Configuration();

    public static class Configuration {
        public static HardwareMap hw;

        public static final ArrayList<ConfigItem> configItems = new ArrayList<>();

        public static final ConfigItem distance       = item("distance", true);

        public static final ConfigItem FR             = item("FR", true);
        public static final ConfigItem FL             = item("FL", true);
        public static final ConfigItem BR             = item("BR", true);
        public static final ConfigItem BL             = item("BL", true);

        public static final ConfigItem intake         = item("intake", true);
        public static final ConfigItem spindexerMotor = item("spindexer", true);
        public static final ConfigItem spindexerServo = item("flap", true);

        public static final ConfigItem shooter        = item("flywheel", true);
        public static final ConfigItem shooter1        = item("test1", true);

        public static final ConfigItem odometry       = item("odometry", true);
        public static final ConfigItem light          = item("light", true);
        public static final ConfigItem imu            = item("imu", true);
        public static final ConfigItem limelight      = item("limelight", true);
        public static final ConfigItem leftSweeper    = item("left sweeper", true);
        public static final ConfigItem rightSweeper   = item("right sweeper", true);

        public static final ConfigItem hood           = item("hood", true);
        public static final ConfigItem yaw            = item("yaw", true);
        public static final ConfigItem pitch          = item("pitch", true);

        public static final ConfigItem intakeServo    = item("intake servo", true);

        @SuppressWarnings("SameParameterValue")
        private static ConfigItem item(String name, boolean active) {
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

        public static <T extends HardwareDevice> T registerItem(Class<T> type, ConfigItem cfg) {
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
    public boolean intakeReverseCompleted;
    public boolean spindexerReady;
    public boolean shooterReady;
    public boolean startShoot;
    public boolean manualShoot;
    public boolean startIntake;
    public boolean intakeComplete;
    public boolean leftRequested;
    public boolean rightRequested;
    public boolean forceStop;
    public boolean queueStop;
    public Globals.Colors colors;
    public boolean sort;
    public boolean haltAfterShoot;

    public static class Globals {
        // ----------------------------
        // Constants
        // ----------------------------
        public static int delayAuto = 0;

        public static double currentSpeed = 1.0;

        public static double spindexerSpeed = 0.85;
        public static double driveSpeed = 1.0;
        public static double slowDriveSpeed = 0.30;

        public static int shooterVelocity = 1050;
        public static int shooterLowTolerance = 100;
        public static int shooterHighTolerance = 150;

        public static int forcedArtifacts = 1;

        public static boolean isFieldCentric = false;
        public static boolean isRightStick = false;

        public static int humanWaitMs = 750;
        public static int spindexerShootTimeTicks = 15;

        public static int tpr = 145;

        public enum Alliances { RED, BLUE }
        public static Alliances alliance = Alliances.BLUE;

        public enum Sides { GOAL, WALL }
        public static Sides side = Sides.GOAL;

        public enum AutoStrategies {
            LEAVE("Leave"),
            THREECYCLEBACK("Three Cycle (Back)"),
            THREECYCLEFRONT("Three Cycle (Front)"),
            TWOCYCLEBACK("Two Cycle (Back)");

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

        public static AutoStrategies autoStrategy = AutoStrategies.THREECYCLEFRONT;

        public enum Colors {
            PPG(0),
            PGP(1),
            GPP(2);

            public final int index;
            Colors(int index) { this.index = index; }
        }
        public static Colors motif;

        public static double flapDownPos = 0.35;
        public static double intakeSpeed = 1.0;

        public static boolean debug = true;

        public static boolean back = false;
        public static boolean gate = false;
        public static int cycles = 3;

        // ----------------------------
        // Helpers
        // ----------------------------
        public static void flipStick() { isRightStick = !isRightStick; }

        public static void flipFieldCentric() { isFieldCentric = !isFieldCentric; }

        public static double easeInOutSine(double x) {
            x = Math.max(-1.0, Math.min(1.0, x));
            double sign = Math.signum(x);
            x = Math.abs(x);
            double eased = Math.sin((Math.PI / 2) * x);
            return eased * sign;
        }
    }

    public Telemetry telemetry;

    // ----------------------------
    // Constructor
    // ----------------------------
    public Robot(HardwareMap hw, Telemetry telemetry) {
        Configuration.hw = hw;

        // state variables
        artifactCount = 0;
        isHumanIntake = false;
        intakeReversed = false;
        spindexerReady = false;
        startShoot = false;
        manualShoot = false;
        startIntake = false;
        intakeComplete = false;
        leftRequested = false;
        rightRequested = false;
        forceStop = false;

        // mechanism instances
        spindexer = new Spindexer(configuration);
        shooter = new Shooter(configuration);
        drivetrain = new Drivetrain(configuration);
        light = new Light(configuration);
        limelight = new Limelight(configuration);
        distance = new Distance(configuration);
        intake = new Intake(configuration);

        // telemetry
        this.telemetry = telemetry;
    }

    public void update() {
        drivetrain.update();
    }
}
