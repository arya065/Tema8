
package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author carlos
 */
@Entity
@Table(name = "alquiler")
@NamedQueries({
    @NamedQuery(name = "Alquiler.findAll", query = "SELECT a FROM Alquiler a"),
    @NamedQuery(name = "Alquiler.findById", query = "SELECT a FROM Alquiler a WHERE a.id = :id"),
    @NamedQuery(name = "Alquiler.findByFechaInicio", query = "SELECT a FROM Alquiler a WHERE a.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Alquiler.findByNumeroDias", query = "SELECT a FROM Alquiler a WHERE a.numeroDias = :numeroDias")})
public class Alquiler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "numero_dias")
    private Integer numeroDias;

    // Relación bidireccional muchos a uno a Cliente
    // Muchos alquileres pueden ser del mismo cliente
    // Este atributo representa el cliente involucrado en este alquiler
    // La tabla Alquiler es la propietaria de la relación al tener la clave ajena
    // Esto se indica con @JoinColumn y el atributo de la tabla con el que obtener
    // los datos de la tabla cliente
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente cliente;

    // Relación bidireccional muchos a uno a Vehiculo
    // Muchos alquileres pueden alquilar al mismo vehiculo
    // Este atributo representa el vehiculo involucrado en este alquiler
    // La tabla Alquiler es la propietaria de la relación al tener la clave ajena
    // Esto se indica con @JoinColumn y el atributo de la tabla con el que obtener
    // los datos de la tabla vehiculo
    @JoinColumn(name = "id_vehiculo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Vehiculo vehiculo;

    public Alquiler() {
    }

    public Alquiler(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaInicio() {

        return fechaInicio;
    }

    // Método añadido, no generado por el IDE al crear la entidad JPA
    public LocalDate getFechaIniciodLocalDate() {
        return new Date(this.fechaInicio.getTime()).
                toInstant().
                atZone(ZoneId.systemDefault()).
                toLocalDate();
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    // Método añadido, no generado por el IDE al crear la entidad JPA
    public void setFechaInicioLocalDate(LocalDate fecha) {
        this.fechaInicio = Date.from(fecha.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public Integer getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(Integer numeroDias) {
        this.numeroDias = numeroDias;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente idCliente) {
        this.cliente = idCliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo idVehiculo) {
        this.vehiculo = idVehiculo;
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
        if (!(object instanceof Alquiler)) {
            return false;
        }
        Alquiler other = (Alquiler) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Alquiler [id=");
        builder.append(id);
        builder.append(", fechaInicio=");
        builder.append(getFechaIniciodLocalDate());
        builder.append(", numeroDias=");
        builder.append(numeroDias);
        builder.append(", cliente=");
        if (cliente != null) {
            builder.append(cliente.getId()); // Evito llamadas concatenadas
        }

        builder.append(", vehiculo=");
        if (vehiculo!=null){
            builder.append(vehiculo.getId()); // Evito llamadas concatenadas
        }
        
        builder.append("]");
        return builder.toString();
    }

}
