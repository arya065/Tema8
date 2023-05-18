
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author carlos
 */
@Entity
@Table(name = "cliente")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findById", query = "SELECT c FROM Cliente c WHERE c.id = :id"),
    @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cliente.findByApellidos", query = "SELECT c FROM Cliente c WHERE c.apellidos = :apellidos"),
    @NamedQuery(name = "Cliente.findByNif", query = "SELECT c FROM Cliente c WHERE c.nif = :nif")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Basic(optional = false)
    @Column(name = "nif")
    private String nif;

    // Asociación bidireccional entre Cliente y TarjetaBancaria
    // Un cliente tiene una tarjeta (@OneToOne) y una tarjeta sólo es de un cliente
    // La anotación JoinColumn indica a JPA el atributo de la tabla
    // Cliente que debe usar para realizar el JOIN con la tabla de TarjetaBancaria
    // Cliente es la entidad propietaria de la relación al tener la clave ajena de
    // Tarjeta
    @JoinColumn(name = "id_tarjeta", referencedColumnName = "id")
    @OneToOne
    private TarjetaBancaria tarjeta;

    // Asociación bidireccional entre Cliente y Alquiler
    // Un cliente puede realizar muchos alquileres
    // Con esta anotación y este atributo se pueden recuperar los alquileres
    // de este cliente
    // mappedBy indica el atributo asociado en la clase Alquiler
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    private List<Alquiler> alquilerList;

    public Cliente() {
    }

    public Cliente(Integer id) {
        this.id = id;
    }

    public Cliente(Integer id, String nif) {
        this.id = id;
        this.nif = nif;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public TarjetaBancaria getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(TarjetaBancaria idTarjeta) {
        this.tarjeta = idTarjeta;
    }

    public List<Alquiler> getAlquilerList() {
        return alquilerList;
    }

    public void setAlquilerList(List<Alquiler> alquilerList) {
        this.alquilerList = alquilerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Cliente [id=");
        builder.append(id);
        builder.append(", apellidos=");
        builder.append(apellidos);
        builder.append(", nif=");
        builder.append(nif);
        builder.append(", nombre=");
        builder.append(nombre);
        builder.append(", alquileresCliente=");
        builder.append(toStringAlquileres());
        builder.append(", tarjetaBancaria=");
        // Si toString de tarjetaBancaria llama a toString de Cliente
        // se genera una excepción StackOverFlow al desbordarse la pila de llamadas
        // ya que las llamadas se concatenan sin fin. Se usa el atributo Id
        // del objeto tarjeta, si no es null
        
        if (tarjeta!=null){
            builder.append(tarjeta.getId());
        }
        
        builder.append("]");
        return builder.toString();
    }

    private String toStringAlquileres() {
        StringBuilder tmp = new StringBuilder();
        for (Alquiler alquiler : alquilerList) {
            tmp.append(alquiler.getId()).append(" ");
        }
        return tmp.toString();
    }

}
