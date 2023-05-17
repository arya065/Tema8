package BDUtils;

import java.sql.*;

public class Conn {

    private static String LOCALHOST;
    private static String BDNAME;
    private static String USER;
    private static String PASSWORD;
    private static String result;

    private static java.sql.Connection instance = null;

    private Conn() {

    }

    public static java.sql.Connection getInstance(String localhost, String bdname, String user, String password) {
        LOCALHOST = "jdbc:mysql://" + localhost + "/";
        BDNAME = bdname;
        USER = user;
        PASSWORD = password;
        if (instance == null) {
            try {

                // Se crea el objeto Connection	
                instance = DriverManager.getConnection(LOCALHOST + BDNAME, USER, PASSWORD);

                connectionRealized();

            } catch (SQLException e) {

                // Error en la conexión
                connectionFailed(e.getMessage());

            }
        }
        return instance;
    }

    public static void connectionRealized() {
        result = "Connection realized succesfully";
    }

    public static void connectionFailed(String tmp) {
        result = "Connection failed:" + tmp;
    }

    public static boolean getResult() {
        if (result.equalsIgnoreCase("Connection realized succesfully")) {
            return true;
        }
        return false;
    }

}
