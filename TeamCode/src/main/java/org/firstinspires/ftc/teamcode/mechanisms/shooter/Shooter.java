package org.firstinspires.ftc.teamcode.mechanisms.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.Configuration.ItemConfig;
import org.firstinspires.ftc.teamcode.helpers.BaseSubsystem;
import org.firstinspires.ftc.teamcode.Robot;

import java.util.HashMap;
import java.util.Map;

public class Shooter extends BaseSubsystem {
    public DcMotorEx shooterMotor;
    private final Robot robot;

    public Shooter(HardwareMap hardwareMap,
                   Telemetry telemetry,
                   ItemConfig cfg,
                   Robot robot) {
        super(hardwareMap, telemetry, cfg.itemActive);

        this.robot = robot;

        ifActive(() -> {
            shooterMotor = hardwareMap.get(DcMotorEx.class, cfg.itemName);
            shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            shooterMotor.setDirection(DcMotor.Direction.REVERSE);
        });
    }

    public void shoot(double velocity) {
        ifActive(() -> shooterMotor.setVelocity(velocity));
    }

    public double getVelocity() {
        if (!active || shooterMotor == null) return 0.0;
        return shooterMotor.getVelocity();
    }

    private String encode(String[] colors) {
        if (colors == null || colors.length < 3) return "";
        StringBuilder sb = new StringBuilder(3);
        for (String c : colors) {
            if ("Purple".equalsIgnoreCase(c)) {
                sb.append('P');
            } else if ("Green".equalsIgnoreCase(c)) {
                sb.append('G');
            } else {
                sb.append('?');
            }
        }
        return sb.toString();
    }

    private static final Map<String, Integer> OFFSET_MAP = new HashMap<>();
    static {
        // motif PPG
        OFFSET_MAP.put("PPG|PPG", 0);
        OFFSET_MAP.put("PPG|PGP", 2);
        OFFSET_MAP.put("PPG|GPP", 1);

        // motif PGP
        OFFSET_MAP.put("PGP|PPG", 1);
        OFFSET_MAP.put("PGP|PGP", 0);
        OFFSET_MAP.put("PGP|GPP", 2);

        // motif GPP
        OFFSET_MAP.put("GPP|PPG", 2);
        OFFSET_MAP.put("GPP|PGP", 1);
        OFFSET_MAP.put("GPP|GPP", 0);
    }

    public int calculateShooter(String[] colors, String[] motif) {
        if (colors == null || motif == null || colors.length < 3 || motif.length < 3) {
            return 0;
        }

        String motifCode = encode(motif);
        String colorCode = encode(colors);
        String key = motifCode + "|" + colorCode;

        Integer offset = OFFSET_MAP.get(key);
        return (offset != null) ? offset : 0;
    }
}
