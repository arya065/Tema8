
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author carlos
 */
@Entity
@Table(name = "tarjeta_bancaria")
@NamedQueries({
    @NamedQuery(name = "TarjetaBancaria.findAll", query = "SELECT t FROM TarjetaBancaria t"),
    @NamedQuery(name = "TarjetaBancaria.findById", query = "SELECT t FROM TarjetaBancaria t WHERE t.id = :id"),
    @NamedQuery(name = "TarjetaBancaria.findByNumero", query = "SELECT t FROM TarjetaBancaria t WHERE t.numero = :numero"),
    @NamedQuery(name = "TarjetaBancaria.findByFechaCaducidad", query = "SELECT t FROM TarjetaBancaria t WHERE t.fechaCaducidad = :fechaCaducidad"),
    @NamedQuery(name = "TarjetaBancaria.findByCsv", query = "SELECT t FROM TarjetaBancaria t WHERE t.csv = :csv")})
public class TarjetaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numero")
    private String numero;

    // La anotación @Temporal sirve para indicar a JPA el tipo de dato
    // JDBC java.sql (DATE, TIME, TIMESTAMP) al que pasar el atributo
    // "fechaCaducidad",que es de tipo java.util.Date
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;

    @Column(name = "csv")
    private Integer csv;

    // Asociación bidireccional entre Tarjeta y Cliente
    // Una tarjeta pertenece a un cliente (@OneToOne)
    // Con este anotación y este atributo se puede recuperar el cliente
    // al que pertenece esta tarjeta.
    @OneToOne(mappedBy = "tarjeta")
    private Cliente cliente;

    public TarjetaBancaria() {
    }

    public TarjetaBancaria(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    // Método añadido, no generado por el IDE al crear la entidad JPA
    public LocalDate getFechaCaducidadLocalDate() {
        return new Date(this.fechaCaducidad.getTime()).
                toInstant().
                atZone(ZoneId.systemDefault()).
                toLocalDate();
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }
    
    // Método añadido, no generado por el IDE al crear la entidad JPA
    public void setFechaCaducidadLocalDate(LocalDate fechaCaducidad){
        this.fechaCaducidad = Date.from(fechaCaducidad.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public Integer getCsv() {
        return csv;
    }

    public void setCsv(Integer csv) {
        this.csv = csv;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        if (!(object instanceof TarjetaBancaria)) {
            return false;
        }
        TarjetaBancaria other = (TarjetaBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // Si toString de tarjetaBancaria llama a toString de Cliente
        // se genera una excepción StackOverFlow al desbordarse la pila de llamadas
        // ya que las llamadas se encadenan sin fin

        // Para evitar llamadas concatenadas entre objetos relacionados voy
        // a usar el atributo nombre del cliente, no el toString completo.
        String nombre = (this.cliente != null) ? this.cliente.getNombre() : "";

        StringBuilder builder = new StringBuilder();
        builder.append("TarjetaBancaria [id=");
        builder.append(id);
        builder.append(", csv=");
        builder.append(csv);
        builder.append(", fechaCaducidad=");
        builder.append(getFechaCaducidadLocalDate());
        builder.append(", numero=");
        builder.append(numero);
        builder.append(", cliente=");
        builder.append(nombre);
        builder.append("]");
        return builder.toString();
    }

}
