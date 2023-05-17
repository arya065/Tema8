package BDUtils;

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

    private Connection con;

    public FacturaDAO(String localhost, String bdname, String user, String password) {
        con = Conn.getInstance(localhost, bdname, user, password);
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
                p.setFechaEmision(res.getDate("fechaEmision").toLocalDate());
                p.setDescripcion(res.getString("descripcion"));
                p.setTotalImporteFactura(res.getDouble("totalImporteFactura"));

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

        String sql = "select * from factura where pk=?";

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
                factura.setFechaEmision(res.getDate("fechaEmision").toLocalDate());
                factura.setDescripcion(res.getString("descripcion"));
                factura.setTotalImporteFactura(res.getDouble("totalImporteFactura"));
                return factura;
            }

            return null;
        }
    }

    @Override
    public int insertFactura(FacturaVO factura) throws SQLException {

        int numFilas = 0;
        String sql = "insert into factura values (?,?,?,?,?)";

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
    public int insertFactura(List<FacturaVO> lista) throws SQLException {
        int numFilas = 0;

        for (FacturaVO tmp : lista) {
            numFilas += FacturaDAO.this.insertFactura(tmp);
        }

        return numFilas;
    }

    @Override
    public int deleteFactura() throws SQLException {

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
    public int deleteFactura(FacturaVO persona) throws SQLException {
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
    public int updateFactura(int pk, FacturaVO nuevosDatos) throws SQLException {

        int numFilas = 0;
        String sql = "update factura set  codigoUnico = ?, fechaEmision = ?, descripcion = ?,"
                + " totalImporteFactura = ? where pk=?";

        if (findByPk(pk) == null) {
            // La persona a actualizar no existe
            return numFilas;
        } else {
            // Instanciamos el objeto PreparedStatement para inserción
            // de datos. Sentencia parametrizada
            try ( PreparedStatement prest = con.prepareStatement(sql)) {

                // Establecemos los parámetros de la sentencia
                prest.setString(1, nuevosDatos.getCodigoUnico());
                prest.setDate(2, Date.valueOf(nuevosDatos.getFechaEmision()));
                prest.setString(3, nuevosDatos.getDescripcion());
                prest.setDouble(4, nuevosDatos.getTotalImporteFactura());
                prest.setInt(5, pk);

                numFilas = prest.executeUpdate();
            }
            return numFilas;
        }
    }
}
