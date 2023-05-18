
package programa;

import controladores.AlquilerJpaController;
import controladores.ClienteJpaController;
import controladores.TarjetaBancariaJpaController;
import controladores.VehiculoJpaController;
import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.Alquiler;
import entidades.Cliente;
import entidades.TarjetaBancaria;
import entidades.Vehiculo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author carlos
 */
public class CreacionModificacionBorradoEntidades {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("alquiler");
    private static final VehiculoJpaController vc = new VehiculoJpaController(emf);
    private static final AlquilerJpaController ac = new AlquilerJpaController(emf);
    private static final TarjetaBancariaJpaController tc = new TarjetaBancariaJpaController(emf);
    private static final ClienteJpaController cc = new ClienteJpaController(emf);

    public static void crearTarjeta() {
        // Creación de una tarjeta, sin Cliente
        TarjetaBancaria t = new TarjetaBancaria();
        t.setCsv(22);
        t.setFechaCaducidadLocalDate(LocalDate.now());
        t.setNumero("122155");
        tc.create(t);  
    }

    public static void crearCliente() {
        // Se crea un cliente 
        // En este caso el cliente no tiene alquileres asociados
        var c = new Cliente();
        c.setNombre("Lucía");
        c.setApellidos("Rubite");
        c.setNif("88888888S");
        cc.create(c);
    }
    
    public static void crearClienteConTarjeta(TarjetaBancaria t) {
        // Se crea un cliente con la tarjeta asociada
        // En este caso el cliente no tiene alquileres asociados
        var c = new Cliente();
        c.setNombre("Ernesto");
        c.setApellidos("Mate");
        c.setNif("99999999S");
        c.setTarjeta(t);
        cc.create(c);
    }

    public static void crearVehiculo() {
        // Creación de un vehículo sin alquileres
        // Si se intentan crear entidades con campos NOT_NULL a NULL --> Excepción
        var v1 = new Vehiculo();
        v1.setId(2); // Aunque se pueda poner valor, se va a ignorar en la BD
        v1.setBastidor("222777654");
        v1.setMarca("Mercedes Benz");
        // vc.create(v1); --> Excepción. Faltan campos que no pueden ser null
        v1.setMatricula("3456TTT");
        v1.setPrecio(23.9);
        vc.create(v1); // Ya no hay excepción
       
    }
    
    public static void crearVehiculoConAlquiler(Alquiler a){
        //Creación de un vehículo con un alquiler que existe en la BD
        // Si hay algún alquiler a null en la lista lanza excepción
        var v1 = new Vehiculo();
        v1.setBastidor("666333222");
        v1.setMarca("Chatarra Go");
        v1.setMatricula("9999ZZZ");
        v1.setPrecio(31.9);
        
        List<Alquiler> listaAlquileres = new ArrayList<>();
        listaAlquileres.add(a);
        v1.setAlquilerList(listaAlquileres);
        vc.create(v1);
        
    }
    
    public static void crearAlquiler(Cliente c, Vehiculo v){
        // Creación de un alquiler, durante 3 días desde hoy
        Alquiler al = new Alquiler();
        al.setCliente(c);
        al.setVehiculo(v);
        al.setFechaInicioLocalDate(LocalDate.now());
        al.setNumeroDias(3);
        ac.create(al);
    }
    
    public static void modificarVehiculo(Vehiculo v) throws NonexistentEntityException, Exception{
        // Para editar un vehículo, primero se busca, se hacen los cambios
        // y finalmente se llama al controlador
        var coche = vc.findVehiculo(3);
        System.out.println(coche);

        if (coche != null) {
            coche.setMarca("Sin marca");
            coche.setModelo("Sin modelo");
        }
        vc.edit(coche);
    }
    
    public static void borrarCliente(Integer id) throws IllegalOrphanException, NonexistentEntityException{
        // Se borra el cliente por ID, si no existe lanza excepción NonexistentEntityException
        // Si tiene alquileres lanza excepción IllegalOrphanException
        // Si tiene una tarjeta asociada, la tarjeta queda sin Cliente
        cc.destroy(id);
    }

    public static void main(String[] args) throws NonexistentEntityException, Exception {

        // CONSULTAR LAS TABLAS PARA COMPROBAR REGISTROS
        
        // Creación de una tarjeta
        crearTarjeta();
        System.out.println("CREACIÓN DE UNA TARJETA BANCARIA --------------");
        Consultas.mostrarTarjetas();
        
        // Creación de un cliente, sin tarjeta
        System.out.println("CREACIÓN DE UN CLIENTE NUEVO --------------");
        crearCliente();
        Consultas.mostrarClientes();
        
        // Creación de un cliente, asignandole la tarejeta anterior
        // Para ello busco la tarjeta y se la paso al método
        System.out.println("CREACIÓN DE UN CLIENTE CON UNA TARJETA EXISTENTE EN LA BD --------------");
        TarjetaBancaria t = tc.findByNumero("122155");
        crearClienteConTarjeta(t);
        Consultas.mostrarClientes();

        System.out.println("CREACIÓN DE UN VEHICULO CON UN ALQUILER EXISTENTE EN LA BD --------------");
        // Creación de un vehículo, al que se le asigna un alquiler existente
        // Busco un alquiler y se lo asigno a este vehículo, por lo que 
        // al vehículo que tenga el alquiler 3 se le quita. De esto se 
        // encarga el controlador JPA.
        Alquiler alq = ac.findAlquiler(3); // Existe
        crearVehiculoConAlquiler(alq);
        Consultas.mostrarVehiculos();
        
        System.out.println("CREACIÓN DE UN ALQUILER, A PARTIR DE UN CLIENTE Y UN VEHÍCULO EXISTENTES --------------");
        Cliente c = cc.findCliente(1);
        Vehiculo v = vc.findVehiculo(2);
        crearAlquiler(c, v);
        Consultas.mostrarAlquileres();
        
        System.out.println("MODIFICACIÓN DE UN VEHÍCULO --------------");
        // Primero se busca el vehículo a modificar en la BD
        v = vc.findByMatricula("1234AAB");
        modificarVehiculo(v);
        Consultas.mostrarVehiculos();
        
        System.out.println("BORRADO DE UN CLIENTE --------------");
        //borrarCliente(10000); // No existe --> NonexistentEntityException
        //borrarCliente(1); // Existe pero tiene alquileres activos --> IllegalOrphanException
        Cliente paraBorrar = cc.findByNif("99999999S"); // Ernesto Mate
        borrarCliente(paraBorrar.getId());
        Consultas.mostrarTarjetas();

    }

}
