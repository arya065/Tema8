package BDUtils;

import java.sql.*;

public class Connection {

    private static final String LOCALHOST = "jdbc:mysql://192.168.56.101/";
    private static final String BDNAME = "bdalmacen";
    private static final String USER = "arya";
    private static final String PASSWORD = "arya";

    private static java.sql.Connection instance = null;

    // Patrón Singleton
    // Constructor privado no accesible desde otras clases
    private Connection() {
    }

    // Método de clase para acceder a la instancia del objeto Connection
    public static java.sql.Connection getInstance() {
        // Si el objeto Connection no está creado, se crea
        if (instance == null) {
            try {

                // Se crea el objeto Connection	
                instance = DriverManager.getConnection(LOCALHOST + BDNAME, USER, PASSWORD);

                System.out.println("Connection realized succesfully");

            } catch (SQLException e) {

                // Error en la conexión
                System.out.println("Connection failed: " + e.getMessage());
            }
        }
        return instance;
    }
}
