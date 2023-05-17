package application;

import javax.swing.*;
import java.awt.*;

public class UserInterfaceEntry extends JPanel {

    private JPanel panel;
    private JTextField localhost;
    private JLabel labelhost;
    private JTextField dbName;
    private JLabel labeldb;
    private JTextField userName;
    private JLabel labeluser;
    private JPasswordField password;
    private JLabel labelpass;

    public UserInterfaceEntry() {
        //create panel
        this.setLayout(new GridBagLayout());

        //create fields with localhost,dbname, username, password
        localhost = new JTextField("");
        dbName = new JTextField("");
        userName = new JTextField("");
        password = new JPasswordField("");

        // create labels
        labelhost = new JLabel("");
    }

}
