import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    ColorPanel colorPanel;
    Color currentColor;

    public static void main(String[] args) {
        Main application = new Main("Lab1 Color Converter");
        application.setSize(900, 380);
        application.setVisible(true);
        application.setResizable(false);
        application.setLocation(380,200);
    }

    public Main(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentColor = new Color(255, 255, 255);
        colorPanel = new ColorPanel(currentColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(colorPanel, gbc);

        JPanel swatchPanel = new JPanel();
        swatchPanel.setLayout(new GridLayout(1, 3));
        RGB rgb = new RGB(currentColor, colorPanel);
        HSL hsl = new HSL(currentColor, colorPanel);
        CMYK cmyk = new CMYK(currentColor, colorPanel);
        swatchPanel.add(rgb);
        swatchPanel.add(hsl);
        swatchPanel.add(cmyk);
        colorPanel.registerSwatch(rgb);
        colorPanel.registerSwatch(cmyk);
        colorPanel.registerSwatch(hsl);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(swatchPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton changeColor = rgb.createButton();
        add(changeColor, gbc);
    }
}
