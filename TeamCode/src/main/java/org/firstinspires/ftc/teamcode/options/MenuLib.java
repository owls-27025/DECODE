package org.firstinspires.ftc.teamcode.options;

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
    }

    public static class Option {
        private final Supplier<String> labelSupplier;
        private final Runnable action;

        public Option(String label, Runnable action) {
            this(() -> label, action);
        }

        public Option(Supplier<String> labelSupplier, Runnable action) {
            this.labelSupplier = labelSupplier;
            this.action = action;
        }

        public String getLabel() {
            return labelSupplier.get();
        }

        public void run() {
            action.run();
        }

        public void onLeft() {
            // nothing
        }

        public void onRight() {
            // nothing
        }
    }

    public static class InfoOption extends Option {

        public InfoOption(String label) {
            super(label, () -> {});
        }

        public InfoOption(Supplier<String> labelSupplier) {
            super(labelSupplier, () -> {});
        }

        @Override
        public void run() {}

        @Override
        public void onLeft() {}

        @Override
        public void onRight() {}
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
        protected final Gamepad gamepad;
        protected final Telemetry telemetry;
        private final String title;

        private final List<Option> options = new ArrayList<>();
        private int pointer = 0;

        public Menu(MenuHost host, Gamepad gamepad, Telemetry telemetry, String title) {
            this.host = host;
            this.gamepad = gamepad;
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

            if (!gamepad.start) {
                if (gamepad.dpadDownWasPressed()) {
                    pointer = (pointer + 1) % options.size();
                }
                if (gamepad.dpadUpWasPressed()) {
                    pointer = (pointer - 1 + options.size()) % options.size();
                }

                if (gamepad.dpadLeftWasPressed()) {
                    options.get(pointer).onLeft();
                }
                if (gamepad.dpadRightWasPressed()) {
                    options.get(pointer).onRight();
                }

                if (gamepad.aWasPressed()) {
                    options.get(pointer).run();
                }
            }

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