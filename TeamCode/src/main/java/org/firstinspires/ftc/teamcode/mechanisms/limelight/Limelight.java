package org.firstinspires.ftc.teamcode.mechanisms.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Limelight {
    public static Limelight3A limelight;

    // Tweak these for your robot (meters / degrees)
    private static final double CAMERA_MOUNT_ANGLE_DEG = 10.0; // placeholder
    private static final double CAMERA_HEIGHT_M = 0.40; // placeholder
    private static final double TARGET_HEIGHT_M = 0.90; // placeholder
    private static final double LAUNCHER_HEIGHT_M = 0.25; // placeholder
    private static final double GRAVITY = 9.80665;

    public static void init(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.start();
    }

    public static LLResult getResult() {
        return limelight.getLatestResult();
    }

    public static ShooterSolution calculateShooter() {
        LLResult r = getResult();
        if (r == null || !r.isValid()) return null;

        Double camX = null;
        Double camY = null;
        Double camZ = null;

        double tyDeg = r.getTy();
        double txDeg = r.getTx();
        double a_mount_rad = Math.toRadians(CAMERA_MOUNT_ANGLE_DEG);
        double a_y_rad = Math.toRadians(tyDeg);

        double deltaH = TARGET_HEIGHT_M - CAMERA_HEIGHT_M;
        double distForward;
        if (Math.abs(Math.tan(a_mount_rad + a_y_rad)) < 1e-6) {
            return null;
        }
        distForward = deltaH / Math.tan(a_mount_rad + a_y_rad);

        double planarDist = distForward / Math.cos(Math.toRadians(txDeg));
        double x = planarDist;
        double y = TARGET_HEIGHT_M - LAUNCHER_HEIGHT_M;

        double chosenAngleDeg = 45.0; // placeholder
        double chosenAngleRad = Math.toRadians(chosenAngleDeg);

        double denom = (x * Math.tan(chosenAngleRad) - y);
        if (denom <= 0) {
            // nothing
        } else {
            double vNeeded = Math.sqrt((GRAVITY * x * x) / (2 * Math.pow(Math.cos(chosenAngleRad), 2) * denom));
            ShooterSolution sol = new ShooterSolution();
            sol.distance = x;
            sol.yawDeg = txDeg;
            sol.angleCandidateDeg = chosenAngleDeg;
            sol.speedForAngle = vNeeded;
            return sol;
        }

        double v0 = 10.0;

        double A = (GRAVITY * x * x) / (2 * v0 * v0);
        double B = -x;
        double C = A + y;
        double discr = B * B - 4 * A * C;
        if (discr < 0) {
            return null; // no
        }
        double T1 = (-B + Math.sqrt(discr)) / (2 * A);
        double T2 = (-B - Math.sqrt(discr)) / (2 * A);
        double theta1 = Math.toDegrees(Math.atan(T1));
        double theta2 = Math.toDegrees(Math.atan(T2));

        ShooterSolution sol2 = new ShooterSolution();
        sol2.distance = x;
        sol2.yawDeg = txDeg;
        sol2.angleSolution1Deg = theta1;
        sol2.angleSolution2Deg = theta2;
        sol2.usedSpeed = v0;
        return sol2;
    }

    public static class ShooterSolution {
        public double distance;
        public double yawDeg;
        public double angleCandidateDeg;
        public double speedForAngle;
        public double usedSpeed;
        public double angleSolution1Deg;
        public double angleSolution2Deg;
    }
}
