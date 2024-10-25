import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ColorPanel extends JPanel {
    private Color color;
    private ArrayList observers;

    public ColorPanel(Color color) {
        this.color = color;
        setPreferredSize(new Dimension(400, 100));
        observers = new ArrayList<>();
    }

    public void colorChanged() {
        notifySwatch();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void setColor(Color color) {
        this.color = color;
        colorChanged();
    }

    public void registerSwatch(Swatch s) {
        observers.add(s);
    }

    public void removeSwatch(Swatch s) {
        int i = observers.indexOf(s);
        if (i >= 0) {
            observers.remove(i);
        }
    }

    public void notifySwatch() {
        for (int i = 0 ; i <  observers.size(); i++) {
            Swatch s = (Swatch) observers.get(i);
            s.update(color);
        }
    }
}

