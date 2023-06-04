package backupUtils;

import com.mycompany.finaldb.CreatingEntities;
import com.mysql.cj.xdevapi.Statement;
import java.util.*;
import entities.*;
import java.io.*;
import java.time.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jpaControllers.*;
import jpaControllers.exceptions.NonexistentEntityException;
import org.apache.commons.io.*;

public class BackUp {

    private ClienteJpaController clContr;
    private EmpresaJpaController emContr;
    private FabricanteJpaController fbContr;
    private ProductoJpaController prContr;
    private List<Cliente> clList;
    private ArrayList<String> clFkList;
    private List<Empresa> emList;
    private ArrayList<String> emclFkList;
    private ArrayList<String> emprFkList;
    private List<Fabricante> fbList;
    private ArrayList<String> fbFkList;
    private List<Producto> prList;
    private ArrayList<String> prFkList;
    private File timeFile;

    public BackUp(ClienteJpaController clContr, EmpresaJpaController emContr, FabricanteJpaController fbContr, ProductoJpaController prContr) {
        this.clContr = clContr;
        this.emContr = emContr;
        this.fbContr = fbContr;
        this.prContr = prContr;
        this.clFkList = new ArrayList<>();
        this.emclFkList = new ArrayList<>();
        this.emprFkList = new ArrayList<>();
        this.fbFkList = new ArrayList<>();
        this.prFkList = new ArrayList<>();
    }

//make backup
    public void makeFullBackUp() throws IOException {
        deletePreviousFiles();

        //create dateTime now
        LocalDateTime dateTime = LocalDateTime.now();
        String time = dateTime.getMinute() + "-" + dateTime.getHour() + ";"
                + dateTime.getDayOfMonth() + "-" + dateTime.getMonthValue() + "-" + dateTime.getYear();

        //creating directory
        File directory = new File("BackUp");
        directory.mkdir();

        //creating timeFile
        timeFile = new File("BackUp/lastBackUpTime.txt");
        writeStringToFile(timeFile, time);

        //creating entities files
        File clFile = new File("BackUp/Cliente" + time + ".txt");
        File emFile = new File("BackUp/Empresa" + time + ".txt");
        File fbFile = new File("BackUp/Fabricante" + time + ".txt");
        File prFile = new File("BackUp/Producto" + time + ".txt");

        //make list with all entities
        emList = emContr.findEmpresaEntities();
        clList = clContr.findClienteEntities();
        fbList = fbContr.findFabricanteEntities();
        prList = prContr.findProductoEntities();

        //writing entities to files
        writeStringToFile(clFile, makeStringEntites(clList));
        writeStringToFile(emFile, makeStringEntites(emList));
        writeStringToFile(fbFile, makeStringEntites(fbList));
        writeStringToFile(prFile, makeStringEntites(prList));

    }

    private String makeStringEntites(List list) {
        String volver = "";
        for (Object obj : list) {
            volver += obj.toString() + "\n";
        }
        return volver;
    }

    private void writeStringToFile(File file, String text) {
        try ( FileWriter fr = new FileWriter(file)) {
            fr.write(text);
            fr.flush();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void deletePreviousFiles() {
        try {
            FileUtils.deleteDirectory(new File("BackUp"));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

//use backup
    public void readBackUp() {
        deleteAllEntities();
        timeFile = new File("BackUp/lastBackUpTime.txt");
        String time = "";
        try ( BufferedReader br = new BufferedReader(new FileReader(timeFile))) {
            time = br.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        File clFile = new File("BackUp/Cliente" + time + ".txt");
        File emFile = new File("BackUp/Empresa" + time + ".txt");
        File fbFile = new File("BackUp/Fabricante" + time + ".txt");
        File prFile = new File("BackUp/Producto" + time + ".txt");

        clList = readFile(clFile);
        emList = readFile(emFile);
        fbList = readFile(fbFile);
        prList = readFile(prFile);

        makeTablesBackUp();
    }

    private void makeTablesBackUp() {
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
        for (int i = 0; i < fbList.size(); i++) {
            String fks = fbFkList.get(i);
            for (int j = 0; j < fks.length(); j++) {
                creator.addProductoFabricante(fbList.get(i), prContr.findProducto(Character.getNumericValue(fks.charAt(j))));
            }
        }
        for (int i = 1; i <= prList.size(); i++) {
            String fks = prFkList.get(i - 1);
            for (int j = 0; j < fks.length(); j++) {
                creator.addEmpresaProducto(emContr.findEmpresa(Character.getNumericValue(fks.charAt(j))), prContr.findProducto(i));
            }
        }
        for (int i = 1; i <= emList.size(); i++) {
            String fks = emclFkList.get(i - 1);
            for (int j = 0; j < fks.length(); j++) {
                creator.addClienteEmpresa(clContr.findCliente(Character.getNumericValue(fks.charAt(j))), emContr.findEmpresa(i));
            }
        }
    }

    private List readFile(File file) {
        List<Object> list = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
            String str;

            //set fileName for easier confirm of option
            String fileName = "";
            if (file.getName().charAt(0) == 'C') {
                fileName = "Cliente";
            } else if (file.getName().charAt(0) == 'E') {
                fileName = "Empresa";
            } else if (file.getName().charAt(0) == 'F') {
                fileName = "Fabricante";
            } else if (file.getName().charAt(0) == 'P') {
                fileName = "Producto";
            }

            //cycle of adding to list
            while ((str = br.readLine()) != null) {
                String[] fields = str.split(", ");

                if (fileName.equalsIgnoreCase("Cliente")) {
                    Cliente cliente = stringToCliente(fields);
                    list.add(cliente);
                } else if (fileName.equalsIgnoreCase("Empresa")) {
                    Empresa empresa = stringToEmpresa(fields);
                    list.add(empresa);
                } else if (fileName.equalsIgnoreCase("Fabricante")) {
                    Fabricante fabricante = stringToFabricante(fields);
                    list.add(fabricante);
                } else if (fileName.equalsIgnoreCase("Producto")) {
                    Producto producto = stringToProducto(fields);
                    list.add(producto);
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return list;
    }

    private Empresa stringToEmpresa(String[] fields) {
        StringBuffer id = new StringBuffer(fields[0]);
        StringBuffer name = new StringBuffer(fields[1]);
        try {
            StringBuffer connectedProducto = new StringBuffer(fields[2]);
            createListFk(emprFkList, connectedProducto.substring(11));
        } catch (ArrayIndexOutOfBoundsException e) {
            createListFk(clFkList, "");
        }
        try {
            StringBuffer connectedCliente = new StringBuffer(fields[3]);
            createListFk(emclFkList, connectedCliente.substring(28));
        } catch (ArrayIndexOutOfBoundsException e) {
            createListFk(clFkList, "");
        }

        return new Empresa(Integer.parseInt(id.substring(10)), name.substring(7));
    }

    private Cliente stringToCliente(String[] fields) {
        StringBuffer id = new StringBuffer(fields[0]);
        StringBuffer name = new StringBuffer(fields[1]);
        StringBuffer tel = new StringBuffer(fields[2]);
        try {
            StringBuffer connected = new StringBuffer(fields[3]);
            createListFk(clFkList, connected.substring(27));
        } catch (ArrayIndexOutOfBoundsException e) {
            createListFk(clFkList, "");
        }
        return new Cliente(Integer.parseInt(id.substring(10)), name.substring(7), tel.substring(4));
    }

    private Fabricante stringToFabricante(String[] fields) {
        StringBuffer id = new StringBuffer(fields[0]);
        StringBuffer name = new StringBuffer(fields[1]);
        try {
            StringBuffer connected = new StringBuffer(fields[2]);
            createListFk(fbFkList, connected.substring(9));
        } catch (ArrayIndexOutOfBoundsException e) {
            createListFk(clFkList, "");
        }
        return new Fabricante(Integer.parseInt(id.substring(13)), name.substring(7));
    }

    private Producto stringToProducto(String[] fields) {
        StringBuffer id = new StringBuffer(fields[0]);
        StringBuffer price = new StringBuffer(fields[1]);
        StringBuffer name = new StringBuffer(fields[2]);
        try {
            StringBuffer connected = new StringBuffer(fields[3]);
            createListFk(prFkList, connected.substring(13));
        } catch (ArrayIndexOutOfBoundsException e) {
            createListFk(clFkList, "");
        }
        return new Producto(Integer.parseInt(id.substring(11)), Double.parseDouble(price.substring(7)), name.substring(7));
    }

//creating list of foreing keys for access to entities
    public void createListFk(ArrayList<String> list, String fks) {
        String[] tmp = fks.split("; ");
        String cleared = "";
        for (int i = 0; i < tmp.length; i++) {
            cleared += tmp[i];
        }
        list.add(cleared);
    }

    public void deleteAllEntities() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_FinalDB_jar_1.0-SNAPSHOTPU");
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            //delete data from DB
            Query deleteQuery = em.createQuery("DELETE FROM Empresa");
            deleteQuery.executeUpdate();
            Query deleteQuery2 = em.createQuery("DELETE FROM Cliente");
            deleteQuery2.executeUpdate();
            Query deleteQuery4 = em.createQuery("DELETE FROM Producto");
            deleteQuery4.executeUpdate();
            Query deleteQuery3 = em.createQuery("DELETE FROM Fabricante");
            deleteQuery3.executeUpdate();

            //set auto_increment to 1
            em.createNativeQuery("ALTER TABLE Empresa AUTO_INCREMENT = 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE Cliente AUTO_INCREMENT = 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE Fabricante AUTO_INCREMENT = 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE Producto AUTO_INCREMENT = 1").executeUpdate();

            transaction.commit();

            System.out.println();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
