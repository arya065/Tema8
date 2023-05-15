/*
 * Interface que usa el patrón DAO sobre la tabla Persona
 */

package BDUtils;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author J. Carlos F. Vico <jcarlosvico@maralboran.es>
 */

public interface InterfaceFactura {
    
    // Método para obtener todos los registros de la tabla
    List<FacturaVO> getAll() throws SQLException;
    
    // Méodo para obtener un registro a partir de la PK
    FacturaVO findByPk(int pk) throws SQLException;
    
    // Método para insertar un registro
    int insertFactura (FacturaVO persona) throws SQLException;
    
    // Método para insertar varios registros
    int insertFactura (List<FacturaVO> lista) throws SQLException;
    
    // Método para borrar una persona
    int deleteFactura (FacturaVO p) throws SQLException;
    
    // Método para borrar toda la tabla
    int deleteFactura() throws SQLException;
    
    // Método para modificar una persona. Se modifica a la persona que tenga esa 'pk'
    // con los nuevos datos que traiga la persona 'nuevosDatos'
    int updateFactura (int pk, FacturaVO nuevosDatos) throws SQLException;
    
}
