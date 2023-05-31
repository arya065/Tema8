package backupUtils;

import com.mycompany.finaldb.CreatingEntities;
import java.util.*;
import entities.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.persistence.*;
import jpaControllers.*;

public class BackUp {

    private ClienteJpaController clContr;
    private EmpresaJpaController emContr;
    private FabricanteJpaController fbContr;
    private ProductoJpaController prContr;
    private List<Cliente> clList;
    private List<Empresa> emList;
    private List<Fabricante> fbList;
    private List<Producto> prList;

    public BackUp(ClienteJpaController clContr, EmpresaJpaController emContr, FabricanteJpaController fbContr, ProductoJpaController prContr) {
        this.clContr = clContr;
        this.emContr = emContr;
        this.fbContr = fbContr;
        this.prContr = prContr;
    }

    //make backup
    public void makeFullBackUp() throws IOException {
        File clFile = new File("BackUp/Cliente.txt");
        File emFile = new File("BackUp/Empresa.txt");
        File fbFile = new File("BackUp/Fabricante.txt");
        File prFile = new File("BackUp/Producto.txt");

        emList = emContr.findEmpresaEntities();
        clList = clContr.findClienteEntities();
        fbList = fbContr.findFabricanteEntities();
        prList = prContr.findProductoEntities();

        writeStringToFile(clFile, makeStringEntites(clList));
        writeStringToFile(emFile, makeStringEntites(emList));
        writeStringToFile(fbFile, makeStringEntites(fbList));
        writeStringToFile(prFile, makeStringEntites(prList));

    }

    public String makeStringEntites(List list) {
        String volver = "";
        for (Object obj : list) {
            volver += obj.toString() + "\n";
        }
        return volver;
    }

    public void writeStringToFile(File file, String text) {
        try ( FileWriter fr = new FileWriter(file)) {
            fr.write(text);
            fr.flush();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    //use backup
    public void makeTablesBackUp() {
        CreatingEntities creator = new CreatingEntities();
        //сделать метод получение списка объктов из файла
        //далее можно использовать метод makeTablesBackUp в main
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

    public List readFile(File file) {
        List<Object> list = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
            String str;
            while ((str = br.readLine()) != null) {
                String[] fields = str.split(", ");
                if (file.getName().charAt(7) == 'C') {
                    Cliente cliente = stringToCliente(fields);
                    list.add(cliente);
                } else if (file.getName().charAt(7) == 'E') {
                    Empresa empresa = stringToEmpresa(fields);
                    list.add(empresa);
                } else if (file.getName().charAt(7) == 'F') {
                    Fabricante fabricante = stringToFabricante(fields);
                    list.add(fabricante);
                } else if (file.getName().charAt(7) == 'P') {
                    Producto producto = stringToProducto(fields);
                    list.add(producto);
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public Empresa stringToEmpresa(String[] fields) {
        return new Empresa(Integer.parseInt(fields[0]), fields[1]);
    }

    public Cliente stringToCliente(String[] fields) {
        return new Cliente(Integer.parseInt(fields[0]), fields[1], fields[2]);
    }

    public Fabricante stringToFabricante(String[] fields) {
        return new Fabricante(Integer.parseInt(fields[0]), fields[1]);
    }

    public Producto stringToProducto(String[] fields) {
        return new Producto(Integer.parseInt(fields[0]), Double.parseDouble(fields[1]), fields[2]);
    }
}
