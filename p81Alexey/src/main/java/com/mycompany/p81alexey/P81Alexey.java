package com.mycompany.p81alexey;

import BDClases.*;
import java.io.*;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class P81Alexey {

    public static void main(String[] args) {
        ArrayList<FacturaVO> list = makeListFromCSV();
        list.forEach(s -> System.out.println(s.toString()));
    }

    public static ArrayList<FacturaVO> makeListFromCSV() {
        ArrayList<FacturaVO> volver = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(new File("facturas.csv")))) {
            String tmp;
            while ((tmp = br.readLine()) != null) {
                StringBuffer str = new StringBuffer(tmp);
                str.delete(0, 8);
                str.deleteCharAt(str.length() - 1);
                volver.add(makeObject(str));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e2) {
            System.out.println("IOExeption");
        }
        return volver;
    }

    public static FacturaVO makeObject(StringBuffer str) {
        String fields[] = str.toString().split(";|=");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new FacturaVO(Integer.parseInt(fields[1]), fields[1],
                LocalDate.parse(fields[3], formatter), fields[5], Double.parseDouble(fields[7]));
    }

    public static void saveObjectsInTable(ArrayList<FacturaVO> list) {
        FacturaDAO daoFactura = new FacturaDAO();
        try {
            daoFactura.insertPersona(list);
            allMethods(daoFactura);
        } catch (SQLException e) {
            System.out.println("SQLExeption");
        }
    }

    public static void allMethods(FacturaDAO daofactura) throws SQLException {
        System.out.println("_______________________________getAll");
        List<FacturaVO> list = daofactura.getAll();
        list.forEach(s -> System.out.println(s.toString()));
        System.out.println("________________________________");
        System.out.println(daofactura.findByPk(2));
        System.out.println("________________________________");
        daofactura.insertPersona(new FacturaVO(10, "10", LocalDate.now(), "test", 1.1));
        list = daofactura.getAll();
        list.forEach(s -> System.out.println(s.toString()));
        System.out.println("________________________________");
        daofactura.deletePersona(new FacturaVO(10, "10", LocalDate.now(), "test", 1.1));
        list = daofactura.getAll();
        list.forEach(s -> System.out.println(s.toString()));
        System.out.println("________________________________");
        daofactura.updatePersona(1, new FacturaVO(10, "10", LocalDate.now(), "test", 1.1));
        list = daofactura.getAll();
        list.forEach(s -> System.out.println(s.toString()));
        System.out.println("________________________________");
        daofactura.deletePersona();
        list = daofactura.getAll();
        list.forEach(s -> System.out.println(s.toString()));
    }
}
