package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MenuLib {

    public interface MenuHost {
        void setCurrentMenu(Menu menu);
        void goToMainMenu();
        void goBack();
    }

    public static class Option {
        private final Supplier<String> labelSupplier;
        private final Runnable action;
        private final boolean selectable;

        public Option(String label, Runnable action) {
            this(() -> label, action, true);
        }

        public Option(Supplier<String> labelSupplier, Runnable action) {
            this(labelSupplier, action, true);
        }

        public Option(Supplier<String> labelSupplier, Runnable action, boolean selectable) {
            this.labelSupplier = labelSupplier;
            this.action = action;
            this.selectable = selectable;
        }

        public boolean isSelectable() {
            return selectable;
        }

        public String getLabel() { return labelSupplier.get(); }
        public void run() { action.run(); }
        public void onLeft() {}
        public void onRight() {}
    }


    public static class InfoOption extends Option {
        public InfoOption(String label) {
            super(() -> label, () -> {}, false);
        }

        public InfoOption(Supplier<String> labelSupplier) {
            super(labelSupplier, () -> {}, false);
        }
    }


    public static class SubMenu extends Option {
        public SubMenu(String label, MenuHost host, Supplier<Menu> submenuSupplier) {
            super(label, () -> {
                Menu submenu = submenuSupplier.get();
                submenu.onSelected();
                host.setCurrentMenu(submenu);
            });
        }
    }

    public static class IntOption extends Option {
        private final String labelPrefix;
        private int value;
        private final int step;
        private final int min;
        private final int max;
        private final Consumer<Integer> onChange;

        public IntOption(String labelPrefix,
                         int initialValue,
                         int step,
                         int min,
                         int max,
                         Consumer<Integer> onChange) {

            super(() -> labelPrefix + initialValue, () -> {});
            this.labelPrefix = labelPrefix;
            this.value = initialValue;
            this.step = step;
            this.min = min;
            this.max = max;
            this.onChange = onChange;
        }

        @Override
        public String getLabel() {
            return labelPrefix + value;
        }

        @Override
        public void onLeft() {
            int newValue = value - step;
            if (newValue < min) newValue = min;
            if (newValue != value) {
                value = newValue;
                if (onChange != null) onChange.accept(value);
            }
        }

        @Override
        public void onRight() {
            int newValue = value + step;
            if (newValue > max) newValue = max;
            if (newValue != value) {
                value = newValue;
                if (onChange != null) onChange.accept(value);
            }
        }

        public int getValue() {
            return value;
        }
    }

    public static class DoubleOption extends Option {
        private final String labelPrefix;
        private double value;
        private final double step;
        private final double min;
        private final double max;
        private final Consumer<Double> onChange;
        private final int decimalPlaces;

        public DoubleOption(String labelPrefix,
                            double initialValue,
                            double step,
                            double min,
                            double max,
                            int decimalPlaces,
                            Consumer<Double> onChange) {

            super(() -> labelPrefix + initialValue, () -> {});
            this.labelPrefix = labelPrefix;
            this.value = initialValue;
            this.step = step;
            this.min = min;
            this.max = max;
            this.decimalPlaces = Math.max(0, decimalPlaces);
            this.onChange = onChange;
        }

        @Override
        public String getLabel() {
            String fmt = "%." + decimalPlaces + "f";
            return labelPrefix + String.format(fmt, value);
        }

        @Override
        public void onLeft() {
            double newValue = value - step;
            if (newValue < min) newValue = min;
            if (Math.abs(newValue - value) > 1e-9) {
                value = newValue;
                if (onChange != null) onChange.accept(value);
            }
        }

        @Override
        public void onRight() {
            double newValue = value + step;
            if (newValue > max) newValue = max;
            if (Math.abs(newValue - value) > 1e-9) {
                value = newValue;
                if (onChange != null) onChange.accept(value);
            }
        }

        public double getValue() {
            return value;
        }
    }

    public static abstract class Menu {
        protected final MenuHost host;
        protected final Gamepad gamepad1;
        protected final Gamepad gamepad2;
        protected final Telemetry telemetry;
        private final String title;

        private final List<Option> options = new ArrayList<>();
        private int pointer = 0;
        private static final double STICK_DEADZONE = 0.25;  // how far the stick needs to be pushed
        private static final long INITIAL_REPEAT_DELAY_NANOS = 800_000_000L;
        private static final long REPEAT_INTERVAL_NANOS = 200_000_000L;

        // up/down/left/right repeat state
        private boolean upHeld = false;
        private boolean downHeld = false;
        private boolean leftHeld = false;
        private boolean rightHeld = false;
        private long upPressStartTime;
        private long downPressStartTime;
        private long leftPressStartTime;
        private long rightPressStartTime;
        private long upLastRepeatTime;
        private long downLastRepeatTime;
        private long leftLastRepeatTime;
        private long rightLastRepeatTime;

        public Menu(MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, String title) {
            this.host = host;
            this.gamepad1 = gamepad1;
            this.gamepad2 = gamepad2;
            this.telemetry = telemetry;
            this.title = title;
        }

        protected void addOption(Option option) {
            options.add(option);
        }

        public void onSelected() {
            pointer = 0;
        }

        public void loop() {
            if (options.isEmpty()) {
                telemetry.clearAll();
                telemetry.addLine("====== " + title + " ======");
                telemetry.addLine("No options in this menu.");
                telemetry.update();
                return;
            }

            boolean inhibitButtons = false;

            // when start is held with a or b buttons
            if ((gamepad1.start && (gamepad1.a || gamepad1.b)) || (gamepad2.start && (gamepad2.a || gamepad2.b))) {
                inhibitButtons = true;
            }

            // release inhibit once buttons are lifted
            if ((!gamepad1.a && !gamepad1.b) || (!gamepad2.a && !gamepad2.b)) {
                inhibitButtons = false;
            }

            // inputs
            double lsx1 = gamepad1.left_stick_x;
            double lsy1 = gamepad1.left_stick_y;
            double lsx2 = gamepad2.left_stick_x;
            double lsy2 = gamepad2.left_stick_y;

            boolean upActive =
                    gamepad1.dpad_up || gamepad2.dpad_up ||
                            (lsy1 < -STICK_DEADZONE) || (lsy2 < -STICK_DEADZONE);

            boolean downActive =
                    gamepad1.dpad_down || gamepad2.dpad_down ||
                            (lsy1 > STICK_DEADZONE) || (lsy2 > STICK_DEADZONE);

            boolean leftActive =
                    gamepad1.dpad_left || gamepad2.dpad_left ||
                            (lsx1 < -STICK_DEADZONE) || (lsx2 < -STICK_DEADZONE);

            boolean rightActive =
                    gamepad1.dpad_right || gamepad2.dpad_right ||
                            (lsx1 > STICK_DEADZONE) || (lsx2 > STICK_DEADZONE);

            long now = System.nanoTime();

            // up
            if (upActive) {
                if (!upHeld) {
                    upHeld = true;
                    upPressStartTime = now;
                    upLastRepeatTime = now;

                    do {
                        pointer = (pointer - 1 + options.size()) % options.size();
                    } while (!options.get(pointer).isSelectable());

                } else {
                    if (now - upPressStartTime >= INITIAL_REPEAT_DELAY_NANOS &&
                            now - upLastRepeatTime >= REPEAT_INTERVAL_NANOS) {

                        upLastRepeatTime = now;

                        do {
                            pointer = (pointer - 1 + options.size()) % options.size();
                        } while (!options.get(pointer).isSelectable());
                    }
                }
            }


            // down
            if (downActive) {
                if (!downHeld) {
                    downHeld = true;
                    downPressStartTime = now;
                    downLastRepeatTime = now;

                    do {
                        pointer = (pointer + 1) % options.size();
                    } while (!options.get(pointer).isSelectable());

                } else {
                    if (now - downPressStartTime >= INITIAL_REPEAT_DELAY_NANOS &&
                            now - downLastRepeatTime >= REPEAT_INTERVAL_NANOS) {

                        downLastRepeatTime = now;

                        do {
                            pointer = (pointer + 1) % options.size();
                        } while (!options.get(pointer).isSelectable());
                    }
                }
            }


            // left
            if (leftActive) {
                if (!leftHeld) {
                    leftHeld = true;
                    leftPressStartTime = now;
                    leftLastRepeatTime = now;
                    options.get(pointer).onLeft();
                } else {
                    if (now - leftPressStartTime >= INITIAL_REPEAT_DELAY_NANOS &&
                            now - leftLastRepeatTime >= REPEAT_INTERVAL_NANOS) {
                        leftLastRepeatTime = now;
                        options.get(pointer).onLeft();
                    }
                }
            } else {
                leftHeld = false;
            }

            // right
            if (rightActive) {
                if (!rightHeld) {
                    rightHeld = true;
                    rightPressStartTime = now;
                    rightLastRepeatTime = now;
                    options.get(pointer).onRight();
                } else {
                    if (now - rightPressStartTime >= INITIAL_REPEAT_DELAY_NANOS &&
                            now - rightLastRepeatTime >= REPEAT_INTERVAL_NANOS) {
                        rightLastRepeatTime = now;
                        options.get(pointer).onRight();
                    }
                }
            } else {
                rightHeld = false;
            }

            // a button
            if ((gamepad1.aWasPressed() || gamepad2.aWasPressed()) && !inhibitButtons) {
                if (options.get(pointer).isSelectable())
                    options.get(pointer).run();
            }

            // b button
            if ((gamepad1.bWasPressed() || gamepad2.bWasPressed()) && !inhibitButtons) {
                host.goBack();
            }

            // telemetry/rendering
            telemetry.clearAll();
            telemetry.addLine("====== " + title + " ======");
            telemetry.addLine();

            for (int i = 0; i < options.size(); i++) {
                String prefix = (i == pointer) ? "> " : "  ";
                telemetry.addLine(prefix + options.get(i).getLabel());
            }

            telemetry.update();
        }
    }
}
