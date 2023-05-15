package application;

import javax.swing.*;
import java.awt.*;

public class UserInterfaceBD extends JPanel {

    private JFrame frame;
    private JPanel panel;

    public UserInterfaceBD() {
        this.frame = new JFrame("test");
        this.panel = new UserEntry();
    }

    private void initComponents() {
        frame = new JFrame("My first DB App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLayout(new GridBagLayout());

    }
}
