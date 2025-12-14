package org.firstinspires.ftc.teamcode.shared.helpers.options.libraries;

import java.util.ArrayDeque;
import java.util.Deque;

public class MenuHostImpl implements MenuLib.MenuHost {
    private final Deque<MenuLib.Menu> stack = new ArrayDeque<>();
    private MenuLib.Menu root;
    private MenuLib.Menu current;

    public boolean isActive = true;

    public void setRoot(MenuLib.Menu rootMenu) {
        this.root = rootMenu;
        this.current = rootMenu;
        stack.clear();
        if (this.current != null) this.current.onSelected();
    }

    public void update() {
        if (current != null) current.loop();
    }

    @Override
    public void setCurrentMenu(MenuLib.Menu menu) {
        if (menu == null) return;
        if (current != null) stack.push(current);
        current = menu;
        current.onSelected();
    }

    @Override
    public void goToMainMenu() {
        if (root == null) return;
        stack.clear();
        current = root;
        current.onSelected();
    }

    @Override
    public void goBack() {
        if (current == root) {
            isActive = false;
            return;
        }

        if (!stack.isEmpty()) {
            current = stack.pop();
            current.onSelected();
        } else {
            goToMainMenu();
        }
    }
}
