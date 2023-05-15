package BDUtils;

import java.sql.*;

public class Conn {

    private static final String LOCALHOST = "jdbc:mysql://127.0.0.1/";
    private static final String BDNAME = "p81Alexey";
    private static final String USER = "root";
    private static final String PASSWORD = "qwer";

    private static java.sql.Connection instance = null;

    // Patrón Singleton
    // Constructor privado no accesible desde otras clases
    private Conn() {
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
