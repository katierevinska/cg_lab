import javax.swing.*;
import java.awt.*;

public class HSL extends JPanel implements Swatch {
    JSlider HueComponent;
    JSlider SaturationComponent;
    JSlider LightnessComponent;
    JTextField hue;
    JTextField saturation;
    JTextField lightness;
    ColorPanel ownColorPanel;
    Color ownColor;
    static final int MAX_VALUE = 100;
    static final int MIN_VALUE = 100;

    public HSL(Color color, ColorPanel colorPanel) {
        super();
        ownColor = color;
        ownColorPanel = colorPanel;
        initializeComponents();
        update(color);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setupLayout();
        addEventListeners();
    }

    private void initializeComponents() {
        HueComponent = new JSlider(JSlider.HORIZONTAL, 360, 0);
        SaturationComponent = new JSlider(JSlider.HORIZONTAL, MAX_VALUE, MIN_VALUE);
        LightnessComponent = new JSlider(JSlider.HORIZONTAL, MAX_VALUE, MIN_VALUE);
        hue = new JTextField(3);
        saturation = new JTextField(3);
        lightness = new JTextField(3);
    }

    private void setupLayout() {
        JPanel row0 = createRowPanel("HSL:");
        JPanel row1 = createRowPanel("H", hue, HueComponent);
        JPanel row2 = createRowPanel("S", saturation, SaturationComponent);
        JPanel row3 = createRowPanel("L", lightness, LightnessComponent);

        add(row0);
        add(row1);
        add(row2);
        add(row3);
    }

    private JPanel createRowPanel(String labelText, JTextField textField, JSlider slider) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText);
        rowPanel.add(label);
        rowPanel.add(textField);
        rowPanel.add(slider);
        return rowPanel;
    }

    private JPanel createRowPanel(String labelText) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText);
        rowPanel.add(label);
        return rowPanel;
    }

    private void addEventListeners() {
        HueComponent.addChangeListener(e -> {
            hue.setText("" + HueComponent.getValue());
            updateColor();
        });

        SaturationComponent.addChangeListener(e -> {
            saturation.setText("" + SaturationComponent.getValue());
            updateColor();
        });

        LightnessComponent.addChangeListener(e -> {
            lightness.setText("" + LightnessComponent.getValue());
            updateColor();
        });

        hue.addActionListener(e -> validateAndSetHsl());
        saturation.addActionListener(e -> validateAndSetSaturation());
        lightness.addActionListener(e -> validateAndSetLightness());
    }

    private void validateAndSetHsl() {
        try {
            int hueValue = Integer.parseInt(hue.getText());
            if (hueValue < 0 || hueValue > 360) {
                showErrorDialog("Hue must be between 0 and 360.");
            } else {
                HueComponent.setValue(hueValue);
                updateColor();
            }
        } catch (NumberFormatException ex) {
            showErrorDialog("Please enter a valid number for Hue.");
        }
    }

    private void validateAndSetSaturation() {
        try {
            int saturationValue = Integer.parseInt(saturation.getText());
            if (saturationValue < 0 || saturationValue > 100) {
                showErrorDialog("Saturation must be between 0 and 100.");
            } else {
                SaturationComponent.setValue(saturationValue);
                updateColor();
            }
        } catch (NumberFormatException ex) {
            showErrorDialog("Please enter a valid number for Saturation.");
        }
    }

    private void validateAndSetLightness() {
        try {
            int lightnessValue = Integer.parseInt(lightness.getText());
            if (lightnessValue < 0 || lightnessValue > 100) {
                showErrorDialog("Lightness must be between 0 and 100.");
            } else {
                LightnessComponent.setValue(lightnessValue);
                updateColor();
            }
        } catch (NumberFormatException ex) {
            showErrorDialog("Please enter a valid number for Lightness.");
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(HSL.this, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }


    @Override
    public void update(Color color) {
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h, s, l;

        l = (max + min) / 2;

        if (max == min) {
            h = 0;
            s = 0;
        } else {
            float d = max - min;
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);

            if (max == r) {
                h = (g - b) / d + (g < b ? 6 : 0);
            } else if (max == g) {
                h = (b - r) / d + 2;
            } else {
                h = (r - g) / d + 4;
            }
            h *= 60;
        }

        int hueValue = Math.round(h);
        int saturationValue = Math.round(s * 100);
        int lightnessValue = Math.round(l * 100);

        hue.setText(Integer.toString(hueValue));
        saturation.setText(Integer.toString(saturationValue));
        lightness.setText(Integer.toString(lightnessValue));

        HueComponent.setValue(hueValue);
        SaturationComponent.setValue(saturationValue);
        LightnessComponent.setValue(lightnessValue);
    }

    private void updateColor() {
        int h = HueComponent.getValue();
        int s = SaturationComponent.getValue();
        int l = LightnessComponent.getValue();
        float hueValue = h / 360f;
        float saturationValue = s / 100f;
        float lightnessValue = l / 100f;

        if (saturationValue == 0) {
            int rgbValue = Math.round(lightnessValue * 255);
            ownColorPanel.removeSwatch(this);
            ownColor = new Color(rgbValue, rgbValue, rgbValue);
        } else {
            float q = lightnessValue < 0.5f ? lightnessValue * (1 + saturationValue) : lightnessValue + saturationValue - lightnessValue * saturationValue;
            float p = 2 * lightnessValue - q;
            float[] rgb = new float[3];
            rgb[0] = hslToRGB(p, q, hueValue + 1f / 3f);
            rgb[1] = hslToRGB(p, q, hueValue);
            rgb[2] = hslToRGB(p, q, hueValue - 1f / 3f);

            int red = Math.round(rgb[0] * 255);
            int green = Math.round(rgb[1] * 255);
            int blue = Math.round(rgb[2] * 255);

            ownColorPanel.removeSwatch(this);
            ownColor = new Color(red, green, blue);
        }
        ownColorPanel.setColor(ownColor);
        ownColorPanel.repaint();
        ownColorPanel.registerSwatch(this);
    }

    private float hslToRGB(float p, float q, float t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1f / 6f) return p + (q - p) * 6f * t;
        if (t < 1f / 2f) return q;
        if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6f;
        return p;
    }
}