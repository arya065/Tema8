
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

// @Entity, Indica que la clase es una entidad que se va a mapear con una tabla 
// Los campos de la tabla se mapearán con atributos de la tabla
@Entity
// @Table, sirve para indicar características del esquema de la tabla
// En este caso, el nombre de la tabla con la que se va a mapear
@Table(name = "vehiculo")
// @NamedQuery, sirve para indicar consultas identificables por un nombre
@NamedQueries({
    @NamedQuery(name = "Vehiculo.findAll", query = "SELECT v FROM Vehiculo v"),
    @NamedQuery(name = "Vehiculo.findMatricula", query = "SELECT v FROM Vehiculo v WHERE v.matricula = :matricula"),})

// Clase JavaBean, implementa la interfaz Serializable, lo que implica incluir
// el atributo serialVersionUID que viene a indicar la versión de la clase
// Serializable lo que indica es que el objeto se convierte en bytes para ser
// guardado y posteriormente recuperado
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Id, indica el atributo que va a mapear con la clave primaria de la tabla
    @Id
    // @GeneratedValue, indica que este valor se genera automáticamente cuando la entidad
    // se guarde en la tabla. En este caso IDENTITY se basa en una columna con incremento automático 
    // y permite que la base de datos genere un nuevo valor con cada operación de inserción
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false) // Columna de la tabla que no puede ser null
    @Column(name = "id") // Nombre de la columna de la tabla con la que relacionar el atributo
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
    // En este caso este campo es tinyint en la BD pero lo asignamos a boolean
    private boolean disponible; 
    @Basic(optional = false)
    @Column(name = "precio")
    private double precio;

    // Constructores
    
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
    
    // Getters y setters

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

    // Equals y hashcode en función del atributo "id"
    // En la BD se supone que dos objetos son iguales si el "id" es igual
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

    // toString por defecto suele incluir solamente el "id" pero si 
    // queremos ver más atributos se puede modificar
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vehiculo{");
        sb.append("id=").append(id);
        sb.append(", bastidor=").append(bastidor);
        sb.append(", matricula=").append(matricula);
        sb.append(", marca=").append(marca);
        sb.append(", modelo=").append(modelo);
        sb.append(", disponible=").append(disponible);
        sb.append(", precio=").append(precio);
        sb.append('}');
        return sb.toString();
    }

}
