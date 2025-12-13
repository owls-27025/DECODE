package org.firstinspires.ftc.teamcode.shared.actions;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionManager {
    private final List<Action> running = new ArrayList<Action>();

    public void add(Action action) {
        if (action != null) running.add(action);
    }

    public <T extends Action> T addAndReturn(T action) {
        add(action);
        return action;
    }

    public void run(TelemetryPacket packet) {
        Iterator<Action> it = running.iterator();
        while (it.hasNext()) {
            Action action = it.next();

            // optional: draw overlay if you use it
            action.preview(packet.fieldOverlay());

            boolean keepRunning = action.run(packet);
            if (!keepRunning) it.remove();
        }
    }

    public void cancel(Action action) {
        if (action == null) return;

        if (action instanceof BaseAction) {
            ((BaseAction) action).cancel();
        }
        running.remove(action);
    }

    public void cancelAll() {
        for (int i = 0; i < running.size(); i++) {
            Action a = running.get(i);
            if (a instanceof BaseAction) {
                ((BaseAction) a).cancel();
            }
        }
        running.clear();
    }

    public boolean isEmpty() {
        return running.isEmpty();
    }

    public int size() {
        return running.size();
    }
}
