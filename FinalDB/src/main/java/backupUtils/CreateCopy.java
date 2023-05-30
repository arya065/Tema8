/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backupUtils;

import java.util.*;
import entities.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.persistence.*;
import jpaControllers.*;

/**
 *
 * @author alexe
 */
public class CreateCopy {

    private ClienteJpaController clContr;
    private EmpresaJpaController emContr;
    private FabricanteJpaController fbContr;
    private ProductoJpaController prContr;
    private List<Cliente> clList;
    private List<Empresa> emList;
    private List<Fabricante> fbList;
    private List<Producto> prList;

    public CreateCopy(ClienteJpaController clContr, EmpresaJpaController emContr, FabricanteJpaController fbContr, ProductoJpaController prContr) {
        this.clContr = clContr;
        this.emContr = emContr;
        this.fbContr = fbContr;
        this.prContr = prContr;
    }

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
}
