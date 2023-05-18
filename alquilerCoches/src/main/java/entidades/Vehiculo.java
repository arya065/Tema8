
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author carlos
 */
@Entity
@Table(name = "vehiculo")
@NamedQueries({
    @NamedQuery(name = "Vehiculo.findAll", query = "SELECT v FROM Vehiculo v"),
    @NamedQuery(name = "Vehiculo.findById", query = "SELECT v FROM Vehiculo v WHERE v.id = :id"),
    @NamedQuery(name = "Vehiculo.findByBastidor", query = "SELECT v FROM Vehiculo v WHERE v.bastidor = :bastidor"),
    @NamedQuery(name = "Vehiculo.findByMatricula", query = "SELECT v FROM Vehiculo v WHERE v.matricula = :matricula"),
    @NamedQuery(name = "Vehiculo.findByMarca", query = "SELECT v FROM Vehiculo v WHERE v.marca = :marca"),
    @NamedQuery(name = "Vehiculo.findByModelo", query = "SELECT v FROM Vehiculo v WHERE v.modelo = :modelo"),
    @NamedQuery(name = "Vehiculo.findByDisponible", query = "SELECT v FROM Vehiculo v WHERE v.disponible = :disponible"),
    @NamedQuery(name = "Vehiculo.findByPrecio", query = "SELECT v FROM Vehiculo v WHERE v.precio = :precio")})
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "bastidor")
    private String bastidor;

    @Basic(optional = false)
    @Column(name = "matricula")
    private String matricula;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Basic(optional = false)
    @Column(name = "disponible")
    private boolean disponible;

    @Basic(optional = false)
    @Column(name = "precio")
    private double precio;

    // Relación bidireccional uno a muchos a Alquiler
    // Un vehículo puede estar alquilado muchas veces
    // Este atributo representa la lista de alquileres en los que
    // se encuentra este vehículo
    // mappedBy indica el atributo asociado en la clase Alquiler 
    @OneToMany(mappedBy = "vehiculo")
    private List<Alquiler> alquilerList;

    public Vehiculo() {
    }

    public Vehiculo(Integer id) {
        this.id = id;
    }

    public Vehiculo(Integer id, String bastidor, String matricula, boolean disponible, double precio) {
        this.id = id;
        this.bastidor = bastidor;
        this.matricula = matricula;
        this.disponible = disponible;
        this.precio = precio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBastidor() {
        return bastidor;
    }

    public void setBastidor(String bastidor) {
        this.bastidor = bastidor;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
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
        if (!(object instanceof Vehiculo)) {
            return false;
        }
        Vehiculo other = (Vehiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Vehiculo [id=");
        builder.append(id);
        builder.append(", bastidor=");
        builder.append(bastidor);
        builder.append(", disponible=");
        builder.append(disponible);
        builder.append(", marca=");
        builder.append(marca);
        builder.append(", matricula=");
        builder.append(matricula);
        builder.append(", modelo=");
        builder.append(modelo);
        builder.append(", precio=");
        builder.append(precio);
        builder.append(", alquileresVehiculo=");
        builder.append(toStringAlquileres());
        builder.append("]");
        return builder.toString();
    }

    private String toStringAlquileres() {
        StringBuilder tmp = new StringBuilder();
        for (Alquiler alquiler : alquilerList) {
            tmp.append(alquiler.getId()).append(", ");
        }
 
        return tmp.length()==0?tmp.toString():tmp.toString()+"\b\b";
    }

}
