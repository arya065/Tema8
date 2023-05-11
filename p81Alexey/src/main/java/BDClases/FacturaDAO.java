/*
 * Clase que implementa la interface IPersona
 */
package BDClases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J. Carlos F. Vico <jcarlosvico@maralboran.es>
 */
public class FacturaDAO implements InterfaceFactura {

    private Connection con = null;
    private static final String SERVIDOR = "jdbc:mysql://192.168.56.101/";
    private static final String NOMBRE_BASE_DATOS = "p81Alexey";
    private static final String USER = "arya";
    private static final String PASS = "arya";

    private static Connection instancia = null;

    // Método de clase para acceder a la instancia del objeto Connection
    public static Connection getInstance() {
        // Si el objeto PersonaDAO no está creado, se crea
        if (instancia == null) {
            try {

                // Se crea el objeto PersonaDAO	
                instancia = DriverManager.getConnection(SERVIDOR + NOMBRE_BASE_DATOS, USER, PASS);

                System.out.println("Conexión realizada con éxito.");

            } catch (SQLException e) {

                // Error en la conexión
                System.out.println("Conexión fallida: " + e.getMessage());
            }
        }
        return instancia;
    }

    public FacturaDAO() {
        con = FacturaDAO.getInstance();
    }

    @Override
    public List<FacturaVO> getAll() throws SQLException {
        List<FacturaVO> lista = new ArrayList<>();

        // Preparamos la consulta de datos mediante un objeto Statement
        // ya que no necesitamos parametrizar la sentencia SQL
        try ( Statement st = con.createStatement()) {
            // Ejecutamos la sentencia y obtenemos las filas en el objeto ResultSet
            ResultSet res = st.executeQuery("select * from factura");
            // Ahora construimos la lista, recorriendo el ResultSet y mapeando los datos
            while (res.next()) {
                FacturaVO p = new FacturaVO();
                // Recogemos los datos de la persona, guardamos en un objeto
                p.setPk(res.getInt("pk"));
                p.setCodigoUnico(res.getString("codigoUnico"));
                p.setFechaEmision(res.getDate("fecha_Emision").toLocalDate());
                p.setDescripcion(res.getString("descripcion"));
                p.setTotalImporteFactura(res.getDouble("totalImporte"));

                //Añadimos el objeto a la lista
                lista.add(p);
            }
        }

        return lista;
    }

    @Override
    public FacturaVO findByPk(int pk) throws SQLException {

        ResultSet res = null;
        FacturaVO factura = new FacturaVO();

        String sql = "select * from persona where pk=?";

        try ( PreparedStatement prest = con.prepareStatement(sql)) {
            // Preparamos la sentencia parametrizada
            prest.setInt(1, pk);

            // Ejecutamos la sentencia y obtenemos las filas en el objeto ResultSet
            res = prest.executeQuery();

            // Nos posicionamos en el primer registro del Resultset. Sólo debe haber una fila
            // si existe esa pk
            if (res.next()) {
                // Recogemos los datos de la persona, guardamos en un objeto
                factura.setPk(res.getInt("pk"));
                factura.setCodigoUnico(res.getString("codigoUnico"));
                factura.setFechaEmision(res.getDate("fecha_Emision").toLocalDate());
                factura.setDescripcion(res.getString("descripcion"));
                factura.setTotalImporteFactura(res.getDouble("totalImporte"));
                return factura;
            }

            return null;
        }
    }

    @Override
    public int insertPersona(FacturaVO factura) throws SQLException {

        int numFilas = 0;
        String sql = "insert into factura values (?,?,?)";

        if (findByPk(factura.getPk()) != null) {
            // Existe un registro con esa pk
            // No se hace la inserción
            return numFilas;
        } else {
            // Instanciamos el objeto PreparedStatement para inserción
            // de datos. Sentencia parametrizada
            try ( PreparedStatement prest = con.prepareStatement(sql)) {

                // Establecemos los parámetros de la sentencia
                prest.setInt(1, factura.getPk());
                prest.setString(2, factura.getCodigoUnico());
                prest.setDate(3, Date.valueOf(factura.getFechaEmision()));
                prest.setString(4, factura.getDescripcion());
                prest.setDouble(5, factura.getTotalImporteFactura());

                numFilas = prest.executeUpdate();
            }
            return numFilas;
        }

    }

    @Override
    public int insertPersona(List<FacturaVO> lista) throws SQLException {
        int numFilas = 0;

        for (FacturaVO tmp : lista) {
            numFilas += insertPersona(tmp);
        }

        return numFilas;
    }

    @Override
    public int deletePersona() throws SQLException {

        String sql = "delete from factura";

        int nfilas = 0;

        // Preparamos el borrado de datos  mediante un Statement
        // No hay parámetros en la sentencia SQL
        try ( Statement st = con.createStatement()) {
            // Ejecución de la sentencia
            nfilas = st.executeUpdate(sql);
        }

        // El borrado se realizó con éxito, devolvemos filas afectadas
        return nfilas;

    }

    @Override
    public int deletePersona(FacturaVO persona) throws SQLException {
        int numFilas = 0;

        String sql = "delete from factura where pk = ?";

        // Sentencia parametrizada
        try ( PreparedStatement prest = con.prepareStatement(sql)) {

            // Establecemos los parámetros de la sentencia
            prest.setInt(1, persona.getPk());
            // Ejecutamos la sentencia
            numFilas = prest.executeUpdate();
        }
        return numFilas;
    }

    @Override
    public int updatePersona(int pk, FacturaVO nuevosDatos) throws SQLException {

        int numFilas = 0;
        String sql = "update factura set codigoUnico = ?, fechaEmision = ?, descripcion = ?, totalImporteFactura = ?,  where pk=?";

        if (findByPk(pk) == null) {
            // La persona a actualizar no existe
            return numFilas;
        } else {
            // Instanciamos el objeto PreparedStatement para inserción
            // de datos. Sentencia parametrizada
            try ( PreparedStatement prest = con.prepareStatement(sql)) {

                // Establecemos los parámetros de la sentencia
                prest.setInt(1, nuevosDatos.getPk());
                prest.setString(2, nuevosDatos.getCodigoUnico());
                prest.setDate(3, Date.valueOf(nuevosDatos.getFechaEmision()));
                prest.setString(4, nuevosDatos.getDescripcion());
                prest.setDouble(5, nuevosDatos.getTotalImporteFactura());

                numFilas = prest.executeUpdate();
            }
            return numFilas;
        }
    }
}
