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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author alexe
 */
@Entity
@Table(name = "cliente")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findByIdCliente", query = "SELECT c FROM Cliente c WHERE c.idCliente = :idCliente"),
    @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cliente.findByTel", query = "SELECT c FROM Cliente c WHERE c.tel = :tel")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cliente")
    private Integer idCliente;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "tel")
    private String tel;
    //relation many to many between Cliente and Empresa, using new table, named cliente_empresa, which has 2 fk (idcliente, idempresa)
    @JoinTable(name = "cliente_empresa", joinColumns = {
        @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")}, inverseJoinColumns = {
        @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")})
    @ManyToMany
    private Collection<Empresa> empresaCollection;

    public Cliente() {
    }

    public Cliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Cliente(Integer idCliente, String nombre, String tel) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.tel = tel;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Collection<Empresa> getEmpresaCollection() {
        return empresaCollection;
    }

    public void setEmpresaCollection(Collection<Empresa> empresaCollection) {
        this.empresaCollection = empresaCollection;
    }

    public void addEmpresa(Empresa empresa) {
        empresaCollection.add(empresa);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //login to print correctly
        if (empresaCollection == null || empresaCollection.size() == 0) {
            return "idCliente=" + idCliente + ", nombre=" + nombre + ", tel=" + tel;
        }
        return "idCliente=" + idCliente + ", nombre=" + nombre + ", tel=" + tel + ", ID de empresas conectadas: " + printCollection();
    }

    public String printCollection() {
        String print = "";
        for (Empresa em : empresaCollection) {
            print += em.getIdEmpresa() + "; ";
        }
        try {
            print = print.substring(0, print.length() - 2);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds");
        }
        return print;
    }
}
