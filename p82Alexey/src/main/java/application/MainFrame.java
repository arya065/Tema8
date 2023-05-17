package application;

import javax.swing.*;


public class MainFrame extends JFrame{
    UserEntry intEntry;
    UserDB intBD;

    public MainFrame() {
    }
    
    private void initComponents(){
        this.add(intBD);
        this.add(intEntry);
    }
}
