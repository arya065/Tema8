package application;

import javax.swing.*;
import java.awt.*;

public class UserInterfaceEntry extends JPanel {

    private JFrame frame;
    private JTextField localhost;
    private JTextField dbName;
    private JTextField userName;
    private JPasswordField password;

    public UserInterfaceEntry() {
        //create panel
        this.setLayout(new GridBagLayout());
        //create frame
        frame = new JFrame("My first DB App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        
        //create fields with localhost,dbname, username, password
        localhost = new JTextField("Insert localhost");
        dbName = new JTextField("Insert name of database");
        userName = new JTextField("Insert username");
        password = new JPasswordField("Insert password");
        
        //add fields
        frame.add(localhost);
        frame.add(dbName);
        frame.add(userName);
        frame.add(password);
        
        // this. options
//        frame.add(this);
        
        
        
    }

}
