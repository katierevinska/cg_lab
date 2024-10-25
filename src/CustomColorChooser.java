import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class CustomColorChooser extends JColorChooser {

    public CustomColorChooser(Color initialColor) {
        super(initialColor);
        removeAll();
        AbstractColorChooserPanel[] panels = getChooserPanels();
        if (panels.length > 0) {
            add(panels[0]);
        }
        setPreferredSize(new Dimension(500, 180));
    }

    public static Color showDialog(Component parent, String title, Color initialColor) {
        CustomColorChooser chooser = new CustomColorChooser(initialColor);
        int result = JOptionPane.showConfirmDialog(parent, chooser, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return chooser.getColor();
        }
        return null;
    }
}