package meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import meepmeeptesting.paths.Back;
import meepmeeptesting.paths.Front;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;
import javax.swing.*;
import java.awt.*;
import java.lang.Math;

public class MeepMeepTesting {

    public enum Alliance {
        RED("Red"),
        BLUE("Blue");

        private final String name;

        Alliance(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum PathSelector {
        FRONT(new Front()),
        BACK(new Back());
        /* ADD NEW PATHS WHEN CREATED */

        public final MeepMeepPath path;

        PathSelector(MeepMeepPath path) { this.path = path; }

        @Override
        public String toString() { return path.getName(); }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ignored) {}

        Selection sel = selectionMenu();

        MeepMeepPath path = sel.path.path;
        Alliance alliance = sel.alliance;

        MeepMeep meepMeep = new MeepMeep(800);
        meepMeep.setDarkMode(true);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setStartPose(path.getInitialPose(alliance))
                .setDimensions(18, 18)
                .setColorScheme(alliance == Alliance.BLUE ? new ColorSchemeBlueDark() : new ColorSchemeRedDark())
                .build();

        Action action = (alliance == Alliance.BLUE)
                ? path.buildBlue(myBot, path.getInitialPose(alliance))
                : path.buildRed(myBot, path.getInitialPose(alliance));

        myBot.runAction(action);

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

    private static Selection selectionMenu() {
        JComboBox<Alliance> allianceBox = new JComboBox<>(Alliance.values());
        JComboBox<PathSelector> pathBox = new JComboBox<>(PathSelector.values());

        allianceBox.setSelectedItem(Alliance.RED);
        pathBox.setSelectedItem(PathSelector.FRONT);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Alliance:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
        panel.add(allianceBox, c);

        c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Path:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
        panel.add(pathBox, c);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        UIManager.put("defaultFont", new Font("Inter", Font.PLAIN, 13));

        allianceBox.setPrototypeDisplayValue(Alliance.BLUE);
        pathBox.setPrototypeDisplayValue(PathSelector.FRONT);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "MeepMeep Path Selection",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            System.exit(0);
        }

        return new Selection(
                (Alliance) allianceBox.getSelectedItem(),
                (PathSelector) pathBox.getSelectedItem()
        );
    }

    private static class Selection {
        final Alliance alliance;
        final PathSelector path;
        Selection(Alliance alliance, PathSelector path) {
            this.alliance = alliance;
            this.path = path;
        }
    }

    public interface MeepMeepPath {
        String getName();
        Pose2d getInitialPose(Alliance alliance);
        Action buildRed(RoadRunnerBotEntity bot, Pose2d pose);
        Action buildBlue(RoadRunnerBotEntity bot, Pose2d pose);
    }
}
