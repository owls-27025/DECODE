package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.Configuration.ItemConfig;
import org.firstinspires.ftc.teamcode.helpers.BaseSubsystem;
import org.firstinspires.ftc.teamcode.Robot;

import java.util.stream.DoubleStream;

public class Spindexer extends BaseSubsystem {
    public DcMotor spindexerMotor;
    private Servo spindexerServo;

    public final double[] intakePositions = new double[]{1.0, 3.0, 5.0};
    public final double[] shootPositions  = new double[]{0.0, 2.0, 4.0};

    public final int halfSlot = 145;
    private final int slot = halfSlot * 2;

    private Robot robot;

    public Spindexer(HardwareMap hardwareMap,
                     Telemetry telemetry,
                     ItemConfig motorCfg,
                     ItemConfig servoCfg,
                     Robot robot) {

        super(hardwareMap, telemetry, motorCfg.itemActive);

        this.robot = robot;

        ifActive(() -> {
            spindexerMotor = hardwareMap.get(DcMotor.class, motorCfg.itemName);
            spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            spindexerMotor.setTargetPosition(0);
            spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            spindexerMotor.setPower(this.robot.getSpindexerSpeed());
            spindexerMotor.setDirection(DcMotor.Direction.FORWARD);
        });

        if (servoCfg.itemActive) {
            spindexerServo = hardwareMap.get(Servo.class, servoCfg.itemName);
        }
    }

    public boolean isBusy() {
        if (!active || spindexerMotor == null) return false;
        return spindexerMotor.isBusy();
    }

    public void setMotorDirection(DcMotor.Direction dir) {
        if (!active || spindexerMotor == null) return;
        spindexerMotor.setDirection(dir);
    }

    public int findPosition() {
        if (!active || spindexerMotor == null) return 0;
        int ticks = spindexerMotor.getCurrentPosition();
        return (ticks / halfSlot) % 6;
    }

    public void moveToNextPosition() {
        if (!active || spindexerMotor == null) return;
        int current = spindexerMotor.getTargetPosition();
        int target = current + slot;
        spindexerMotor.setTargetPosition(target);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spindexerMotor.setPower(robot.getSpindexerSpeed());
    }

    public void moveHalfPosition(boolean forward) {
        if (!active || spindexerMotor == null) return;
        int current = spindexerMotor.getTargetPosition();
        int target = forward ? current + halfSlot : current - halfSlot;
        spindexerMotor.setTargetPosition(target);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spindexerMotor.setPower(robot.getSpindexerSpeed());
    }

    public void moveServo(double pos) {
        if (spindexerServo == null) return;
        spindexerServo.setPosition(pos);
    }

    public void moveSpindexerTo(int targetPosition){
        if (!active || spindexerMotor == null) return;
        spindexerMotor.setTargetPosition(targetPosition);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spindexerMotor.setPower(robot.getSpindexerSpeed());
    }

    public void intakePosition() {
        if (!active || spindexerMotor == null) return;

        int currPositionTicks = spindexerMotor.getCurrentPosition();
        int targetPosition;

        double currPos = findExactPosition(currPositionTicks);
        if (DoubleStream.of(intakePositions).anyMatch(n -> n == currPos)) {
            targetPosition = currPositionTicks;
        } else {
            int nextHalfSlotTicks = (currPositionTicks + halfSlot - currPositionTicks % halfSlot);
            double nextPos = ((double) nextHalfSlotTicks / halfSlot) % 6;

            if (DoubleStream.of(intakePositions).anyMatch(n -> n == nextPos)) {
                targetPosition = nextHalfSlotTicks;
            } else {
                targetPosition = nextHalfSlotTicks + halfSlot;
            }
        }

        moveSpindexerTo(targetPosition);
    }

    public void shootPosition() {
        if (!active || spindexerMotor == null) return;

        int currPositionTicks = spindexerMotor.getCurrentPosition();
        int targetPosition;

        double currPos = findExactPosition(currPositionTicks);
        if (DoubleStream.of(shootPositions).anyMatch(n -> n == currPos)) {
            targetPosition = currPositionTicks;
        } else {
            int nextHalfSlotTicks = (currPositionTicks + halfSlot - currPositionTicks % halfSlot);
            double nextPos = ((double) nextHalfSlotTicks / halfSlot) % 6;

            if (DoubleStream.of(shootPositions).anyMatch(n -> n == nextPos)) {
                targetPosition = nextHalfSlotTicks;
            } else {
                targetPosition = nextHalfSlotTicks + halfSlot;
            }
        }

        moveSpindexerTo(targetPosition);
    }

    public double findExactPosition(int ticks) {
        return (double) (ticks / halfSlot) % 6;
    }
}