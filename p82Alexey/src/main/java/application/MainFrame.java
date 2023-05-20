package application;

import Controladores.FacturaJpaController;
import javax.persistence.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    //variables para utilizar
    UserDB intBD;
    EntityManagerFactory factory;
    FacturaJpaController controller;

    //constructor para ejecutar la programma
    public MainFrame() {
        this.factory = Persistence.createEntityManagerFactory("com.mycompany_p82Alexey_jar_1.0-SNAPSHOTPU");
        controller = new FacturaJpaController(factory);
        this.intBD = new UserDB(controller);
        initComponents();

    }

    //anadir Panel UserDB y poner las opciones
    private void initComponents() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1100, 500);
        this.setVisible(true);
        this.add(intBD);
        this.setResizable(false);

    }

}
