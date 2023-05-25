package com.mycompany.finaldb;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import jpaController.*;

public class FinalDB {

    public static void main(String[] args) {

        initComponents();
    }

    public static void initComponents() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_FinalDB_jar_1.0-SNAPSHOTPU");
        ClienteJpaController clContr = new ClienteJpaController(factory);
        EmpresaJpaController emContr = new EmpresaJpaController(factory);
        FabricanteJpaController fbContr = new FabricanteJpaController(factory);
        ProductoJpaController prContr = new ProductoJpaController(factory);
        
        MainFraime frame = new MainFraime(clContr, emContr, fbContr, prContr);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(830, 540);
        frame.setVisible(true);
        frame.setResizable(false);
        
    }
}
