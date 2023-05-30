package backupUtils;

import com.mycompany.finaldb.CreatingEntities;
import java.util.*;
import entities.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.persistence.*;
import jpaControllers.*;

public class UseBackUp {

    private ClienteJpaController clContr;
    private EmpresaJpaController emContr;
    private FabricanteJpaController fbContr;
    private ProductoJpaController prContr;
    private List<Cliente> clList;
    private List<Empresa> emList;
    private List<Fabricante> fbList;
    private List<Producto> prList;

    public UseBackUp(ClienteJpaController clContr, EmpresaJpaController emContr, FabricanteJpaController fbContr, ProductoJpaController prContr) {
        this.clContr = clContr;
        this.emContr = emContr;
        this.fbContr = fbContr;
        this.prContr = prContr;
        
        //сделать метод получение списка объктов из файла
        //далее можно использовать метод makeTablesBackUp в main
//        this.clList = clList;
//        this.emList = emList;
//        this.fbList = fbList;
//        this.prList = prList;
    }

    public void makeTablesBackUp() {
        CreatingEntities creator = new CreatingEntities();

        //create Fabricante
        for (Fabricante fabricante : fbList) {
            creator.createFabricante(fabricante.getIdFabricante(), fabricante.getNombre());
        }
        //create Producto
        for (Producto producto : prList) {
            creator.createProducto(producto.getIdProducto(), producto.getPrecio(), producto.getNombre());
        }
        //create Cliente
        for (Cliente cliente : clList) {
            creator.createCliente(cliente.getIdCliente(), cliente.getNombre(), cliente.getTel());
        }
        //create Empresa
        for (Empresa empresa : emList) {
            creator.createEmpresa(empresa.getIdEmpresa(), empresa.getNombre());
        }

        //add relations between entities
        for (Fabricante fabricante : fbList) {
            creator.addProductoFabricante(fabricante, fabricante.getProducto());
        }
        for (Producto producto : prList) {
            producto.setEmpresaCollection(producto.getEmpresaCollection());
        }
        for (Empresa empresa : emList) {
            empresa.setClienteCollection(empresa.getClienteCollection());
        }
    }

    public void makeListOfFile() {
        File clFile = new File("BackUp/Cliente.txt");
        File emFile = new File("BackUp/Empresa.txt");
        File fbFile = new File("BackUp/Fabricante.txt");
        File prFile = new File("BackUp/Producto.txt");

        readFile(clFile);
        readFile(emFile);
        readFile(fbFile);
        readFile(prFile);

    }

    public void readFile(File file) {
        String[] volver;
        try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
            String str;
            while ((str = br.readLine()) != null) {
                volver = str.split(", ");
                
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
