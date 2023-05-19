package com.mycompany.p82alexey;

import BDUtils.Conn;
import BDUtils.FacturaDAO;
import Controladores.FacturaJpaController;
import Entities.Factura;
import application.MainFrame;
import java.sql.Connection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class P82Alexey {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        MainFrame tmp = new MainFrame();
        
        //создание объекта в java
        Factura test = new Factura(1);
        // создание фабрики 
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_p82Alexey_jar_1.0-SNAPSHOTPU");
        // создание контроллера 
        FacturaJpaController controller = new FacturaJpaController(emf);
        // отправка объекта test в нашу БД
        controller.create(test);
        
        
    }
}
/*
В пакете "application" он включает классы, необходимые для выполнения программы
с графическим интерфейсом, которая позволяет выполнять типичные задачи CRUD
с таблицей счетов базы данных, используя методы драйвера JPA.
Прокомментируйте код.
*/