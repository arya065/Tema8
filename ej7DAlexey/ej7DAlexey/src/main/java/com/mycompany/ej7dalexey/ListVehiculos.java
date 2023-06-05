package com.mycompany.ej7dalexey;

import java.util.*;
import com.mycompany.ej7balexey.Vehiculo;
import java.io.*;

public class ListVehiculos {

    private ArrayList<Vehiculo> listCars;
    private ArrayList<Vehiculo> listCarsCopy;
    private ArrayList<String> fullList;
    private String turismos;
    private String furgonetas;
    private String deportivos;

    public ListVehiculos() throws IOException {
        listCars = new ArrayList<>();
        fullList = new ArrayList<>();
        listCarsCopy = new ArrayList<>();
        // hacer fullList
        makefullList("turismos.txt");
        makefullList("furgonetas.txt");
        makefullList("deportivos.txt");
        getLine(listCars);// hacer listCars
        createCopias();
        printNamecsvFiles();
        fullList = new ArrayList<>();
        readAndSaveCopias();

        System.out.println("");

    }

    public void makefullList(String nameDoc) throws FileNotFoundException {
        FileReader fr = new FileReader(nameDoc);
        Scanner scan = new Scanner(fr);
        while (scan.hasNext()) {
            fullList.add(scan.nextLine());
        }
    }

    public String makeString(String nameDoc) throws FileNotFoundException {
        FileReader fr = new FileReader(nameDoc);
        Scanner scan = new Scanner(fr);
        String volver = "";
        while (scan.hasNext()) {
            volver += scan.nextLine();
        }
        return volver;
    }

    public void getLine(ArrayList<Vehiculo> list) {
        for (String line : fullList) {
            makeListVehiculos(line, list);
        }
    }

    public void makeListVehiculos(String line, ArrayList<Vehiculo> list) {
        String marca = "";
        int volumen;
        String nombreProductor = "";
        int velocidadMaxima;
        String tipo = "";
        String tmp = "";

        int indexStart = line.indexOf("marca=");
        int indexEnd = line.indexOf(":volumen");
        for (int i = indexStart + 6; i < indexEnd; i++) {
            marca += line.charAt(i);
        }

        indexStart = line.indexOf("nombreProductor=");
        indexEnd = line.indexOf(":velocidadMaxima");
        for (int i = indexStart + 16; i < indexEnd; i++) {
            nombreProductor += line.charAt(i);
        }

        indexStart = line.indexOf("volumen=");
        indexEnd = line.indexOf(":nombreProductor");
        for (int i = indexStart + 8; i < indexEnd; i++) {
            tmp += line.charAt(i);

        }
        volumen = Integer.parseInt(tmp);

        tmp = "";
        indexStart = line.indexOf("velocidadMaxima=");
        indexEnd = line.indexOf(":tipo");
        for (int i = indexStart + 16; i < indexEnd; i++) {
            tmp += line.charAt(i);
        }
        velocidadMaxima = Integer.parseInt(tmp);

        indexStart = line.indexOf("tipo=");
        for (int i = indexStart + 5; i < line.length(); i++) {
            tipo += line.charAt(i);
        }

        list.add(new Vehiculo(marca, volumen, nombreProductor, velocidadMaxima, tipo));

    }

    public void createCopias() throws FileNotFoundException, IOException {
        File file = new File("copias");
        file.mkdir();
        turismos = makeString("turismos.txt");
        furgonetas = makeString("furgonetas.txt");
        deportivos = makeString("deportivos.txt");
        generatecsvFile(turismos, "copias/turismos.csv");
        generatecsvFile(furgonetas, "copias/furgonetas.csv");
        generatecsvFile(deportivos, "copias/deportivos.csv");
    }

    public void generatecsvFile(String text, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(text);
        writer.flush();
        writer.close();

    }

    public void printNamecsvFiles() {
        File copias = new File("copias");
        File[] listFiles = copias.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            System.out.println(listFiles[i].getName());
        }
    }

    public void readAndSaveCopias() throws FileNotFoundException {
        makefullList("copias/turismos.csv");
        makefullList("copias/furgonetas.csv");
        makefullList("copias/deportivos.csv");

        getLine(listCarsCopy);

        listCarsCopy.forEach(s -> System.out.println(s.toString()));
        Comparator<Vehiculo> comparator = new Comparator<Vehiculo>() {
            @Override
            public int compare(Vehiculo o1, Vehiculo o2) {
                return o1.getMarca().compareToIgnoreCase(o2.getMarca());
            }
        };
        Collections.sort(listCarsCopy, comparator);
        deleteFile("copias/turismos.csv");
        deleteFile("copias/furgonetas.csv");
        deleteFile("copias/deportivos.csv");

        printNamecsvFiles();
        
    }
    public void deleteFile(String wayFile){
        File file = new File(wayFile);
        file.delete();
    }
}
