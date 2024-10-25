import javax.swing.*;
import java.awt.*;

public class CMYK extends JPanel implements Swatch {
    private ColorPanel colorArea;
    private Color ownColor;
    private JSlider[] sliders = new JSlider[4];
    private JTextField[] textFields = new JTextField[4];
    private String[] labels = {"C", "M", "Y", "K"};
    private boolean updating = false;

    public CMYK(Color color, ColorPanel colorpanel) {
        super();
        this.colorArea = colorpanel;
        this.ownColor = color;
        double[] cmykValues = rgbToCmyk(color);
        initComponents(cmykValues);
        setupListeners();
        updateUI(cmykValues);
    }

    private double[] rgbToCmyk(Color color) {
        double R = color.getRed() / 255.0;
        double G = color.getGreen() / 255.0;
        double B = color.getBlue() / 255.0;
        double K = Math.min(1 - R, Math.min(1 - G, 1 - B));
        double C = (K == 1) ? 0 : (1 - R - K) / (1 - K);
        double M = (K == 1) ? 0 : (1 - G - K) / (1 - K);
        double Y = (K == 1) ? 0 : (1 - B - K) / (1 - K);
        return new double[]{C * 100, M * 100, Y * 100, K * 100};
    }

    private void initComponents(double[] cmykValues) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel cmyk = new JLabel("CMYK:");
        add(cmyk);
        for (int i = 0; i < sliders.length; i++) {
            sliders[i] = new JSlider(0, 100, (int) cmykValues[i]);
            textFields[i] = new JTextField(3);
            textFields[i].setText(String.valueOf((int) cmykValues[i]));
            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.add(new JLabel(labels[i]));
            row.add(textFields[i]);
            row.add(sliders[i]);
            add(row);
        }
    }

    private void setupListeners() {
        for (int i = 0; i < sliders.length; i++) {
            int index = i;
            sliders[i].addChangeListener(e -> {
                if (!updating) {
                    updating = true;
                    int value = sliders[index].getValue();
                    textFields[index].setText(String.valueOf(value));
                    updateColorFromSliders();
                    updating = false;
                }
            });
            textFields[i].addActionListener(e -> {
                if (!updating) {
                    validateAndSetTextFieldValue(index);
                }
            });
        }
    }

    private void validateAndSetTextFieldValue(int index) {
        try {
            int value = Integer.parseInt(textFields[index].getText());
            if (value < 0 || value > 100) {
                throw new NumberFormatException("Value must be between 0 and 100");
            }
            updating = true;
            sliders[index].setValue(value);
            updating = false;
            updateColorFromSliders();
        } catch (NumberFormatException e) {
            textFields[index].setText(String.valueOf(sliders[index].getValue()));
            JOptionPane.showMessageDialog(this, "Please enter a value between 0 and 100", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Color color) {
        ownColor = color;
        double[] cmykValues = rgbToCmyk(color);
        updateUI(cmykValues);
    }

    private void updateUI(double[] cmykValues) {
        for (int i = 0; i < sliders.length; i++) {
            if (!textFields[i].getText().equals(String.valueOf((int) cmykValues[i]))) {
                sliders[i].setValue((int) cmykValues[i]);
                textFields[i].setText(String.valueOf((int) cmykValues[i]));
            }
        }
    }

    private void updateColorFromSliders() {
        double C = sliders[0].getValue() / 100.0;
        double M = sliders[1].getValue() / 100.0;
        double Y = sliders[2].getValue() / 100.0;
        double K = sliders[3].getValue() / 100.0;
        int R = (int) (255 * (1 - C) * (1 - K));
        int G = (int) (255 * (1 - M) * (1 - K));
        int B = (int) (255 * (1 - Y) * (1 - K));
        ownColor = new Color(R, G, B);
        colorArea.setColor(ownColor);
        colorArea.repaint();
    }
}
