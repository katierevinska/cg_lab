import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class RGB extends JPanel implements Swatch {
    static final int MIN_VALUE = 0;
    static final int MAX_VALUE = 255;
    static Color ownColor;
    static ColorPanel colorArea;

    JSlider RComponent;
    JSlider GComponent;
    JSlider BComponent;
    JTextField redValue;
    JTextField greenValue;
    JTextField blueValue;

    private boolean updatingFromSlider = false;
    private boolean updatingFromTextField = false;

    public RGB(Color color, ColorPanel colorpanel) {
        super();
        ownColor = color;
        colorArea = colorpanel;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        RComponent = createSlider(color.getRed(), e -> updateColorFromSlider(RComponent));
        GComponent = createSlider(color.getGreen(), e -> updateColorFromSlider(GComponent));
        BComponent = createSlider(color.getBlue(), e -> updateColorFromSlider(BComponent));

        redValue = createTextField(color.getRed(), e -> updateColorFromTextField(redValue));
        greenValue = createTextField(color.getGreen(), e -> updateColorFromTextField(greenValue));
        blueValue = createTextField(color.getBlue(), e -> updateColorFromTextField(blueValue));

        add(createRow(new JLabel("RGB:")));
        add(createRow(new JLabel("R"), redValue, RComponent));
        add(createRow(new JLabel("G"), greenValue, GComponent));
        add(createRow(new JLabel("B"), blueValue, BComponent));
    }

    private JSlider createSlider(int initialValue, ChangeListener listener) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, MIN_VALUE, MAX_VALUE, initialValue);
        slider.addChangeListener(listener);
        return slider;
    }

    private JTextField createTextField(int initialValue, ActionListener listener) {
        JTextField textField = new JTextField(3);
        textField.setText(String.valueOf(initialValue));
        textField.addActionListener(listener);
        return textField;
    }

    private JPanel createRow(Component... components) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        for (Component component : components) {
            row.add(component);
        }
        return row;
    }

    private void updateColorFromSlider(JSlider slider) {
        updatingFromSlider = true;
        if (slider == RComponent) {
            ownColor = new Color(slider.getValue(), ownColor.getGreen(), ownColor.getBlue());
        } else if (slider == GComponent) {
            ownColor = new Color(ownColor.getRed(), slider.getValue(), ownColor.getBlue());
        } else if (slider == BComponent) {
            ownColor = new Color(ownColor.getRed(), ownColor.getGreen(), slider.getValue());
        }
        updateColor();
        updatingFromSlider = false;
    }

    private void updateColorFromTextField(JTextField textField) {
        updatingFromTextField = true;
        try {
            int value = Integer.parseInt(textField.getText());
            if (value < MIN_VALUE || value > MAX_VALUE) {
                throw new NumberFormatException("Value must be between 0 and 255");
            }
            if (textField == redValue) {
                ownColor = new Color(value, ownColor.getGreen(), ownColor.getBlue());
            } else if (textField == greenValue) {
                ownColor = new Color(ownColor.getRed(), value, ownColor.getBlue());
            } else if (textField == blueValue) {
                ownColor = new Color(ownColor.getRed(), ownColor.getGreen(), value);
            }

            if (!updatingFromSlider) {
                RComponent.setValue(ownColor.getRed());
                GComponent.setValue(ownColor.getGreen());
                BComponent.setValue(ownColor.getBlue());
            }
            updateColor();
        } catch (NumberFormatException e) {
            if (textField == redValue) {
                textField.setText(String.valueOf(ownColor.getRed()));
            } else if (textField == greenValue) {
                textField.setText(String.valueOf(ownColor.getGreen()));
            } else if (textField == blueValue) {
                textField.setText(String.valueOf(ownColor.getBlue()));
            }
            JOptionPane.showMessageDialog(this, "Please enter a value between 0 and 255", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        updatingFromTextField = false;
    }

    @Override
    public void update(Color color) {
        ownColor = color;
        colorArea.removeSwatch(this);
        RComponent.setValue(color.getRed());
        GComponent.setValue(color.getGreen());
        BComponent.setValue(color.getBlue());
        redValue.setText(String.valueOf(color.getRed()));
        greenValue.setText(String.valueOf(color.getGreen()));
        blueValue.setText(String.valueOf(color.getBlue()));
        colorArea.registerSwatch(this);
    }

    public void updateColor() {
        colorArea.removeSwatch(this);
        colorArea.setColor(ownColor);
        colorArea.repaint();
        if (!updatingFromTextField) {
            redValue.setText(String.valueOf(ownColor.getRed()));
            greenValue.setText(String.valueOf(ownColor.getGreen()));
            blueValue.setText(String.valueOf(ownColor.getBlue()));
        }
        colorArea.registerSwatch(this);
    }

    public JButton createButton() {
        JButton changeColor = new JButton("Change Color");
        changeColor.addActionListener(e -> {
            Color newColor = CustomColorChooser.showDialog(null, "Select a color", ownColor);
            if (newColor != null) {
                ownColor = newColor;
                updateColor();
            }
        });
        return changeColor;
    }
}
