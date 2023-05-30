package com.mycompany.finaldb;

import backupUtils.CreateCopy;
import backupUtils.UseBackUp;
import entities.*;
import java.io.IOException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import jpaControllers.*;
import javax.swing.*;

public class FinalDB {

    public static void main(String[] args) throws IOException {

        initComponents();
    }

    public static void initComponents() throws IOException {
        //create controllers
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_FinalDB_jar_1.0-SNAPSHOTPU");
        ClienteJpaController clContr = new ClienteJpaController(factory);
        EmpresaJpaController emContr = new EmpresaJpaController(factory);
        FabricanteJpaController fbContr = new FabricanteJpaController(factory);
        ProductoJpaController prContr = new ProductoJpaController(factory);

        int confirm = askBackUp();
        //класс useBackUp и его метод 
        if (confirm == 0) {
            UseBackUp backup = new UseBackUp(clContr, emContr, fbContr, prContr);
            
        } else if (confirm == 1) {
            //options of frame
            MainFraime frame = new MainFraime(clContr, emContr, fbContr, prContr);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(830, 540);
            frame.setVisible(true);
            frame.setResizable(false);

            //creating first entities
            createEntitiesDB(clContr, emContr, fbContr, prContr);
        }

        CreateCopy copy = new CreateCopy(clContr, emContr, fbContr, prContr);
        copy.makeFullBackUp();
    }

    public static void createEntitiesDB(ClienteJpaController clContr, EmpresaJpaController emContr, FabricanteJpaController fbContr, ProductoJpaController prContr) {
        CreatingEntities creator = new CreatingEntities();

        //create fabricantes
        creator.createFabricante(1, "fabricante1");
        creator.createFabricante(2, "fabricante2");
        creator.createFabricante(3, "fabricante3");

        //create productos
        creator.createProducto(1, 1.0, "producto1");
        creator.createProducto(2, 2.0, "producto2");
        creator.createProducto(3, 3.0, "producto3");

        //create clientes
        creator.createCliente(1, "cliente1", "111111111");
        creator.createCliente(2, "cliente2", "222222222");
        creator.createCliente(3, "cliente3", "333333333");

        //create empresas
        creator.createEmpresa(1, "empresa1");
        creator.createEmpresa(2, "empresa2");
        creator.createEmpresa(3, "empresa3");

        //make relations between entities
        creator.addProductoFabricante(fbContr.findFabricante(1), prContr.findProducto(1));
        creator.addProductoFabricante(fbContr.findFabricante(2), prContr.findProducto(2));
        creator.addProductoFabricante(fbContr.findFabricante(3), prContr.findProducto(3));

        creator.addEmpresaProducto(emContr.findEmpresa(1), prContr.findProducto(1));
        creator.addEmpresaProducto(emContr.findEmpresa(2), prContr.findProducto(2));
        creator.addEmpresaProducto(emContr.findEmpresa(3), prContr.findProducto(3));

        creator.addClienteEmpresa(clContr.findCliente(1), emContr.findEmpresa(1));
        creator.addClienteEmpresa(clContr.findCliente(2), emContr.findEmpresa(2));
        creator.addClienteEmpresa(clContr.findCliente(3), emContr.findEmpresa(3));

    }

    public static int askBackUp() {
        return JOptionPane.showConfirmDialog(null, "Utilizar ultimo backup?");
    }
}
