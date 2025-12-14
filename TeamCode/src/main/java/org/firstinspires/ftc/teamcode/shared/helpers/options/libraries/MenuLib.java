package org.firstinspires.ftc.teamcode.shared.helpers.options.libraries;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsController;

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

    @SuppressWarnings("unused")
    public static class Option {
        private Supplier<String> labelSupplier = () -> "";
        private boolean selectable = true;

        private Runnable onSelect = () -> {};
        private Runnable onLeft   = () -> {};
        private Runnable onRight  = () -> {};
        private Runnable onBack   = () -> {};


        public String getLabel() { return labelSupplier.get(); }
        public boolean isSelectable() { return selectable; }

        public void select() { onSelect.run(); }
        public void left()   { onLeft.run(); }
        public void right()  { onRight.run(); }
        public void back()   { onBack.run(); }

        public static Option create() { return new Option(); }

        public Option label(String label) {
            this.labelSupplier = () -> label;
            return this;
        }

        public Option label(Supplier<String> labelSupplier) {
            this.labelSupplier = labelSupplier;
            return this;
        }

        public Option selectable(boolean selectable) {
            this.selectable = selectable;
            return this;
        }

        public Option onSelect(Runnable r) {
            this.onSelect = (r != null) ? r : () -> {};
            return this;
        }

        public Option onLeft(Runnable r) {
            this.onLeft = (r != null) ? r : () -> {};
            return this;
        }

        public Option onRight(Runnable r) {
            this.onRight = (r != null) ? r : () -> {};
            return this;
        }

        public Option onBack(Runnable r) {
            this.onBack = (r != null) ? r : () -> {};
            return this;
        }

        public static Option info(Supplier<String> label) {
            return Option.create()
                    .label(label)
                    .selectable(false);
        }

        public static Option action(Supplier<String> label, Runnable onSelect) {
            return Option.create()
                    .label(label)
                    .onSelect(onSelect)
                    .selectable(true);
        }

        public static Option submenu(String label, MenuHost host, Supplier<Menu> submenuSupplier) {
            return Option.create()
                    .label(label)
                    .onSelect(() -> {
                        Menu submenu = submenuSupplier.get();
                        submenu.onSelected();
                        host.setCurrentMenu(submenu);
                    });
        }

        public static Option value(
                Supplier<String> label,
                Runnable onLeft,
                Runnable onRight
        ) {
            return Option.create()
                    .label(label)
                    .onLeft(onLeft)
                    .onRight(onRight);
        }

        public static <E extends Enum<E>> Option enumCycle(
                String prefix,
                Class<E> enumClass,
                Supplier<E> getter,
                Consumer<E> setter
        ) {
            final E[] values = enumClass.getEnumConstants();

            Supplier<String> label = () -> prefix + getter.get();

            Runnable left = () -> {
                E cur = getter.get();
                int i = indexOf(values, cur);
                assert values != null;
                int next = (i - 1 + values.length) % values.length;
                setter.accept(values[next]);
            };

            Runnable right = () -> {
                E cur = getter.get();
                int i = indexOf(values, cur);
                assert values != null;
                int next = (i + 1) % values.length;
                setter.accept(values[next]);
            };

            return Option.value(label, left, right);
        }

        private static <T> int indexOf(T[] arr, T item) {
            if (item == null) return 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == item || arr[i].equals(item)) return i;
            }
            return 0;
        }
    }

    public static abstract class Menu {
        protected final MenuHost host;
        protected final OwlsController gamepad1;
        protected final OwlsController gamepad2;
        protected final Telemetry telemetry;
        private final String title;

        private final List<Option> options = new ArrayList<>();
        private int pointer = 0;

        private static final double STICK_DEADZONE = 0.25;
        private static final long INITIAL_REPEAT_DELAY_NANOS = 800_000_000L;
        private static final long REPEAT_INTERVAL_NANOS = 200_000_000L;

        private boolean upHeld, downHeld, leftHeld, rightHeld;
        private long upPressStartTime, downPressStartTime, leftPressStartTime, rightPressStartTime;
        private long upLastRepeatTime, downLastRepeatTime, leftLastRepeatTime, rightLastRepeatTime;

        public Menu(MenuHost host, OwlsController gamepad1, OwlsController gamepad2, Telemetry telemetry, String title) {
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

            double lsx1 = gamepad1.leftStickX();
            double lsy1 = gamepad1.leftStickY();
            double lsx2 = gamepad2.leftStickX();
            double lsy2 = gamepad2.leftStickY();

            boolean upActive =
                    gamepad1.held(OwlsController.Button.DPAD_UP) || gamepad2.held(OwlsController.Button.DPAD_UP) ||
                            (lsy1 < -STICK_DEADZONE) || (lsy2 < -STICK_DEADZONE);

            boolean downActive =
                    gamepad1.held(OwlsController.Button.DPAD_DOWN) || gamepad2.held(OwlsController.Button.DPAD_DOWN) ||
                            (lsy1 > STICK_DEADZONE) || (lsy2 > STICK_DEADZONE);

            boolean leftActive =
                    gamepad1.held(OwlsController.Button.DPAD_LEFT) || gamepad2.held(OwlsController.Button.DPAD_LEFT) ||
                            (lsx1 < -STICK_DEADZONE) || (lsx2 < -STICK_DEADZONE);

            boolean rightActive =
                    gamepad1.held(OwlsController.Button.DPAD_RIGHT) || gamepad2.held(OwlsController.Button.DPAD_RIGHT) ||
                            (lsx1 > STICK_DEADZONE) || (lsx2 > STICK_DEADZONE);

            long now = System.nanoTime();

            // up
            if (upActive) {
                if (!upHeld) {
                    upHeld = true;
                    upPressStartTime = now;
                    upLastRepeatTime = now;
                    movePointer(-1);
                } else if (now - upPressStartTime >= INITIAL_REPEAT_DELAY_NANOS &&
                        now - upLastRepeatTime >= REPEAT_INTERVAL_NANOS) {
                    upLastRepeatTime = now;
                    movePointer(-1);
                }
            } else {
                upHeld = false;
            }

            // down
            if (downActive) {
                if (!downHeld) {
                    downHeld = true;
                    downPressStartTime = now;
                    downLastRepeatTime = now;
                    movePointer(+1);
                } else if (now - downPressStartTime >= INITIAL_REPEAT_DELAY_NANOS &&
                        now - downLastRepeatTime >= REPEAT_INTERVAL_NANOS) {
                    downLastRepeatTime = now;
                    movePointer(+1);
                }
            } else {
                downHeld = false;
            }

            // left
            if (leftActive) {
                if (!leftHeld) {
                    leftHeld = true;
                    leftPressStartTime = now;
                    leftLastRepeatTime = now;
                    options.get(pointer).left();
                } else if (now - leftPressStartTime >= INITIAL_REPEAT_DELAY_NANOS &&
                        now - leftLastRepeatTime >= REPEAT_INTERVAL_NANOS) {
                    leftLastRepeatTime = now;
                    options.get(pointer).left();
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
                    options.get(pointer).right();
                } else if (now - rightPressStartTime >= INITIAL_REPEAT_DELAY_NANOS &&
                        now - rightLastRepeatTime >= REPEAT_INTERVAL_NANOS) {
                    rightLastRepeatTime = now;
                    options.get(pointer).right();
                }
            } else {
                rightHeld = false;
            }

            if (gamepad1.pressed(OwlsController.Button.A) || gamepad2.pressed(OwlsController.Button.A)) {
                if (options.get(pointer).isSelectable()) options.get(pointer).select();
            }

            if (gamepad1.pressed(OwlsController.Button.B) || gamepad2.pressed(OwlsController.Button.B)) {
                host.goBack();
            }

            // render
            telemetry.clearAll();
            telemetry.addLine("====== " + title + " ======");
            telemetry.addLine();

            for (int i = 0; i < options.size(); i++) {
                String prefix = (i == pointer) ? "> " : "  ";
                telemetry.addLine(prefix + options.get(i).getLabel());
            }
            telemetry.update();
        }

        private void movePointer(int delta) {
            if (options.isEmpty()) return;

            int attempts = 0;
            do {
                pointer = (pointer + delta + options.size()) % options.size();
                attempts++;
            } while (attempts <= options.size() && !options.get(pointer).isSelectable());
        }
    }
}
