package org.firstinspires.ftc.teamcode;

public class Helpers {
    public static double easeInOutSine(double x) {
        x = Math.max(-1.0, Math.min(1.0, x));
        double sign = Math.signum(x);
        x = Math.abs(x);
        double eased = Math.sin((Math.PI / 2) * x);
        return eased * sign;
    }
}
