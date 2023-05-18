
package programa;

import controladores.AlquilerJpaController;
import controladores.ClienteJpaController;
import controladores.TarjetaBancariaJpaController;
import controladores.VehiculoJpaController;
import controladores.exceptions.NonexistentEntityException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author carlos
 */
public class Consultas {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("alquiler");
    private static final VehiculoJpaController vc = new VehiculoJpaController(emf);
    private static final AlquilerJpaController ac = new AlquilerJpaController(emf);
    private static final TarjetaBancariaJpaController tc = new TarjetaBancariaJpaController(emf);
    private static final ClienteJpaController cc = new ClienteJpaController(emf);

    public static void main(String[] args) throws NonexistentEntityException, Exception {

        // Consultas de las tablas completas
        mostrarVehiculos();
        mostrarClientes();
        mostrarTarjetas();
        mostrarAlquileres();
        
        // Número de registros de la tablas
        System.out.println("Número de tuplas tabla Cliente: " + cc.getClienteCount());

        // Obtiene cuatro vehículos, saltándose los tres primeros
        // usando el método findVehiculoEntities(maxResult, firstResult)
        System.out.println("************************");
        vc.findVehiculoEntities(4, 3).forEach(System.out::println);
        System.out.println("************************");

        // Obtención de entidades por ID. Si no existe el ID, devuelve null
        var vehiculo = vc.findVehiculo(1);
        System.out.println(vehiculo);
        System.out.println("************************");
        var cliente = cc.findCliente(1);
        System.out.println(cliente);
        System.out.println("************************");
        var tarjeta = tc.findTarjetaBancaria(11);
        System.out.println(tarjeta);
        System.out.println("************************");
        var alquiler = ac.findAlquiler(1);
        System.out.println(alquiler);
        System.out.println("************************");
        
        // Búsqueda de una entidad usando Named Query
        // Si no existe ninguna entidad que cumple, lanza excepción NoResultException
        System.out.println(" ----- Búsqueda por named query ---------- ");
        vehiculo = vc.findByBastidor("4534543Z01");
        System.out.println(vehiculo);
        
        tarjeta = tc.findByNumero("555444");
        System.out.println(tarjeta);

    }

    public static void mostrarVehiculos() {
        System.out.println("--------- Listado de Vehículos -------------");
        vc.findVehiculoEntities().forEach(System.out::println);
        System.out.println("--------------------------------------------");
        
    }

    public static void mostrarClientes() {
        System.out.println("--------- Listado de Clientes -------------");
        cc.findClienteEntities().forEach(System.out::println);
        System.out.println("--------------------------------------------");
    }

    public static void mostrarAlquileres() {
        System.out.println("--------- Listado de Alquileres -------------");
        ac.findAlquilerEntities().forEach(System.out::println);
        System.out.println("--------------------------------------------");
    }

    public static void mostrarTarjetas() {
        System.out.println("--------- Listado de Tarjetas -------------");
        tc.findTarjetaBancariaEntities().forEach(System.out::println);
        System.out.println("--------------------------------------------");
    }

}
