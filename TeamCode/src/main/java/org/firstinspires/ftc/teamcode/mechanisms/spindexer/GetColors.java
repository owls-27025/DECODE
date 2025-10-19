package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;

import static org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper.SpindexerMotor;

@TeleOp(name = "Spindexer Test (Find Color Positions)", group = "Spindexer")
public class GetColors extends OpMode {

    private static final int TPR = 537;
    private static final int ARRIVAL_TOL = 8;
    private static final long SETTLE_MS = 500;
    private enum State { IDLE, MOVING, FINISHING, SAMPLING, DONE }
    private State state = State.IDLE;

    private Gamepad cur1, prev1;
    public String[] colors = new String[3];

    private int samplesTaken = 0;
    private long arrivedAtMs = 0L;

    @Override
    public void init() {
        ColorSensorHelper.init(hardwareMap);
        SpindexerHelper.init(hardwareMap);

        cur1  = new Gamepad();
        prev1 = new Gamepad();

        for (int i = 0; i < 3; i++) colors[i] = "-";
    }

    @Override
    public void loop() {
        prev1.copy(cur1);
        cur1.copy(gamepad1);
        
        if (pressed(cur1.a, prev1.a) && state == State.IDLE) {
            samplesTaken = 0;
            state = State.MOVING;
            SpindexerHelper.moveToNextPosition();
        }
        
        int ticks = SpindexerMotor.getCurrentPosition();
        int position = SpindexerHelper.findPosition(ticks, TPR);
        boolean atTarget = Math.abs(SpindexerMotor.getTargetPosition() - ticks) <= ARRIVAL_TOL;

        switch (state) {
            case MOVING:
                if (atTarget) {
                    arrivedAtMs = System.currentTimeMillis();
                    state = State.FINISHING;
                }
                break;

            case FINISHING:
                if (System.currentTimeMillis() - arrivedAtMs >= SETTLE_MS) {
                    state = State.SAMPLING;
                }
                break;

            case SAMPLING:
                String c = ColorSensorHelper.getColor();
                colors[position] = (c != null) ? c : "unknown";
                samplesTaken++;

                if (samplesTaken >= 3) {
                    state = State.DONE;
                } else {
                    state = State.MOVING;
                    SpindexerHelper.moveToNextPosition();
                }
                break;

            case DONE:
            case IDLE:
            default:
                break;
        }

        telemetry.addData("State", state);
        telemetry.addData("Ticks", ticks);
        telemetry.addData("Target", SpindexerMotor.getTargetPosition());
        telemetry.addData("At Target", atTarget);
        telemetry.addData("position", position);
        telemetry.addData("Colors", String.format("[0]=%s  [1]=%s  [2]=%s", colors[0], colors[1], colors[2]));
        telemetry.addData("Current Color", ColorSensorHelper.getColor());
        telemetry.update();
    }

    private boolean pressed(boolean now, boolean before) {
        return now && !before;
    }
}
