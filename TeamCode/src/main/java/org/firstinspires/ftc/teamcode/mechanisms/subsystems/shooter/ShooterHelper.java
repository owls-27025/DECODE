package org.firstinspires.ftc.teamcode.mechanisms.subsystems.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Config;

import java.util.Arrays;

public class ShooterHelper {
    public static DcMotorEx shooterMotor;

    public static void init(HardwareMap hardwareMap) {
        if (Config.shooter.itemActive) {
            shooterMotor = hardwareMap.get(DcMotorEx.class, Config.shooter.itemName);
            shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            shooterMotor.setDirection(DcMotor.Direction.REVERSE);
        }
    }

    public static void shoot() {
        shooterMotor.setVelocity(1000); // placeholder, once limelight implemented calculate velocity
    }

    public static void shoot(double velocity) {
        shooterMotor.setVelocity(velocity);
    }

    // motifs
    static String[] m1 = {"Purple", "Purple", "Green"};
    static String[] m2 = {"Purple", "Green", "Purple"};
    static String[] m3 = {"Green", "Purple", "Purple"};

    // color combinations
    static String[] i1 = {"Purple", "Purple", "Green"};
    static String[] i2 = {"Purple", "Green", "Purple"};
    static String[] i3 = {"Green", "Purple", "Purple"};
    static String[] i4 = {"Purple", "Purple", "Purple"};
    static String[] i5 = {"Green", "Green", "Purple"};
    static String[] i6 = {"Green", "Green", "Green"};
    static String[] i7 = {"Green", "Purple", "Green"};
    static String[] i8 = {"Purple", "Green", "Green"};

    // group them into arrays for lookup
    static String[][] motifs = {m1, m2, m3};
    static String[][] colorCombos = {i1, i2, i3, i4, i5, i6, i7, i8};

    public static int calculateShooter(String[] colors, String[] motif) {
        if(colors == null || colors.length < 3) {
            return 0;
        } else {
            int motifIndex = -1;
            int colorIndex = -1;

            // find which motif matches
            for (int i = 0; i < motifs.length; i++) {
                if (Arrays.equals(motifs[i], motif)) {
                    motifIndex = i;
                    break;
                }
            }

            // find which color combo matches
            for (int i = 0; i < colorCombos.length; i++) {
                if (Arrays.equals(colorCombos[i], colors)) {
                    colorIndex = i;
                    break;
                }
            }

            // offsets
            int[][] offsets = {
                    {0, 2, 1}, // m1
                    {1, 0, 2}, // m2
                    {2, 1, 0}  // m3
            };

            // if it works use offset, otherwise 0
            if (colorIndex < 3) {
                return offsets[motifIndex][colorIndex];
            } else {
                return 0;
            }
        }
    }
}
