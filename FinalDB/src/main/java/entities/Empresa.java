/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author alexe
 */
@Entity
@Table(name = "empresa")
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findByIdEmpresa", query = "SELECT e FROM Empresa e WHERE e.idEmpresa = :idEmpresa"),
    @NamedQuery(name = "Empresa.findByNombre", query = "SELECT e FROM Empresa e WHERE e.nombre = :nombre")})
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_empresa")
    private Integer idEmpresa;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    //relation many to many with table Cliente
    @ManyToMany(mappedBy = "empresaCollection")
    private Collection<Cliente> clienteCollection;
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    //relation one to many with Producto, Empresa can use many Products
    @ManyToOne
    private Producto idProducto;

    public Empresa() {
    }

    public Empresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Empresa(Integer idEmpresa, String nombre) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<Cliente> getClienteCollection() {
        return clienteCollection;
    }

    public void setClienteCollection(Collection<Cliente> clienteCollection) {
        this.clienteCollection = clienteCollection;
    }

    public void addCliente(Cliente cliente) {
        clienteCollection.add(cliente);
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresa != null ? idEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.idEmpresa == null && other.idEmpresa != null) || (this.idEmpresa != null && !this.idEmpresa.equals(other.idEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //logic to print correctly
        if (idProducto == null && clienteCollection.size() != 0) {
            return "idEmpresa=" + idEmpresa + ", nombre=" + nombre + ", ID de clientes connectados: " + printCollection();
        }
        if (clienteCollection.size() == 0 && idProducto != null) {
            return "idEmpresa=" + idEmpresa + ", nombre=" + nombre + ", idProducto=" + idProducto.getIdProducto();
        }
        if (clienteCollection.size() == 0 && idProducto == null) {
            return "idEmpresa=" + idEmpresa + ", nombre=" + nombre;
        }
        return "idEmpresa=" + idEmpresa + ", nombre=" + nombre + ", idProducto=" + idProducto.getIdProducto() + ", ID de clientes connectados: " + printCollection();
    }

    public String printCollection() {
        String print = "";
        for (Cliente cl : clienteCollection) {
            print += cl.getIdCliente() + "; ";
        }
        try {
            print = print.substring(0, print.length() - 2);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds");
        }
        return print;
    }

}
