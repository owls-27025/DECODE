package org.firstinspires.ftc.teamcode.shared.options;

import java.util.ArrayDeque;
import java.util.Deque;

public class MenuHostImpl implements MenuLib.MenuHost {
    private final Deque<MenuLib.Menu> stack = new ArrayDeque<MenuLib.Menu>();
    private MenuLib.Menu root;
    private MenuLib.Menu current;

    /** Set the root menu and make it current. */
    public void setRoot(MenuLib.Menu rootMenu) {
        this.root = rootMenu;
        this.current = rootMenu;
        stack.clear();
        if (this.current != null) this.current.onSelected();
    }

    /** Call this every init loop to render/update the current menu. */
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
        if (!stack.isEmpty()) {
            current = stack.pop();
            current.onSelected();
        } else {
            goToMainMenu();
        }
    }
}
