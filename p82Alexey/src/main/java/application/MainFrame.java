package application;

import BDUtils.Conn;
import java.awt.Dimension;
import java.sql.Connection;
import javax.swing.*;

public class MainFrame extends JFrame {

    UserEntry intEntry;
    UserDB intBD;
    Connection con;

    public MainFrame() {
        intEntry = new UserEntry();
        
        initComponents();

    }

    private void initComponents() {
        this.add(intEntry);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setVisible(true);

        do {
            if (intEntry.closed()) {
                setintBDVisible();
            }
        } while (!intEntry.closed());

    }

    public void setintBDVisible() {
//        this.con = intEntry.getConnection();
        this.remove(intEntry);
        intBD = new UserDB(intEntry.getConnection());
        this.add(intBD);
        this.repaint();
        this.revalidate();
        this.setSize(1200, 422);
    }

}
