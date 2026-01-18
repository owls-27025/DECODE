package meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MeepMeepTesting {

    public enum Alliance {
        RED("Red"),
        BLUE("Blue");

        private final String name;

        Alliance(String name) { this.name = name; }

        @Override
        public String toString() { return name; }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception ignored) {}

        Selection sel = selectionMenu();

        MeepMeepPath path = sel.path();
        Alliance alliance = sel.alliance();

        MeepMeep meepMeep = new MeepMeep(800);
        meepMeep.setDarkMode(true);

        Pose2d startPose = path.getInitialPose(alliance);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setStartPose(startPose)
                .setDimensions(18, 18)
                .setColorScheme(alliance == Alliance.BLUE ? new ColorSchemeBlueDark() : new ColorSchemeRedDark())
                .build();

        Action action = (alliance == Alliance.BLUE)
                ? path.buildBlue(myBot, startPose)
                : path.buildRed(myBot, startPose);

        myBot.runAction(action);

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

    private static Selection selectionMenu() {
        UIManager.put("defaultFont", new Font("Inter", Font.PLAIN, 13));

        JComboBox<Alliance> allianceBox = new JComboBox<>(Alliance.values());
        allianceBox.setSelectedItem(Alliance.RED);
        allianceBox.setPrototypeDisplayValue(Alliance.BLUE);

        List<MeepMeepPath> paths = discoverPaths();
        if (paths.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No paths found in package: meepmeeptesting.paths\n\n",
                    "No Paths Found",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        JComboBox<MeepMeepPath> pathBox =
                new JComboBox<>(paths.toArray(new MeepMeepPath[0]));

        pathBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                if (value instanceof MeepMeepPath path) {
                    setText(path.getName());
                } else {
                    setText("");
                }

                return this;
            }
        });

        pathBox.setSelectedIndex(0);
        pathBox.setPrototypeDisplayValue(paths.get(0));

        int comboWidth = 220;

        pathBox.setPreferredSize(new Dimension(comboWidth, pathBox.getPreferredSize().height));
        allianceBox.setPreferredSize(new Dimension(comboWidth, allianceBox.getPreferredSize().height));


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

        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

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
                (MeepMeepPath) pathBox.getSelectedItem()
        );
    }

    private static List<MeepMeepPath> discoverPaths() {
        List<MeepMeepPath> out = new ArrayList<>();

        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .acceptPackages("meepmeeptesting.paths")
                .scan()) {

            for (Class<?> cls : scan.getClassesImplementing(MeepMeepPath.class.getName()).loadClasses()) {
                int mods = cls.getModifiers();

                if (cls.isInterface()) continue;
                if (Modifier.isAbstract(mods)) continue;

                try {
                    Object obj = cls.getDeclaredConstructor().newInstance();
                    out.add((MeepMeepPath) obj);
                } catch (NoSuchMethodException e) {
                    System.err.println("[MeepMeepTesting] Skipping " + cls.getName()
                            + " (no public no-arg constructor)");
                } catch (Throwable t) {
                    System.err.println("[MeepMeepTesting] Skipping " + cls.getName()
                            + " (failed to instantiate): " + t);
                }
            }
        } catch (Throwable t) {
            System.err.println("[MeepMeepTesting] Classpath scan failed: " + t);
        }

        out.sort(Comparator.comparing(MeepMeepPath::getName, String.CASE_INSENSITIVE_ORDER));
        return out;
    }

    private record Selection(Alliance alliance, MeepMeepPath path) {}

    public interface MeepMeepPath {
        String getName();
        Pose2d getInitialPose(Alliance alliance);
        Action buildRed(RoadRunnerBotEntity bot, Pose2d pose);
        Action buildBlue(RoadRunnerBotEntity bot, Pose2d pose);
    }
}
