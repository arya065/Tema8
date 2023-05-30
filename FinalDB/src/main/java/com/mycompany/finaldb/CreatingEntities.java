package com.mycompany.finaldb;

import entities.*;
import jpaControllers.*;
import javax.persistence.*;

public class CreatingEntities {

    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_FinalDB_jar_1.0-SNAPSHOTPU");
    private final ClienteJpaController clContr = new ClienteJpaController(factory);
    private final EmpresaJpaController emContr = new EmpresaJpaController(factory);
    private final FabricanteJpaController fbContr = new FabricanteJpaController(factory);
    private final ProductoJpaController prContr = new ProductoJpaController(factory);

    public void createFabricante(int id, String nombre) {
        Fabricante fabricante = new Fabricante(id, nombre);
        fbContr.create(fabricante);
    }

    public void createProducto(int id, double price, String name) {
        Producto producto = new Producto(id, price, name);
        prContr.create(producto);
    }

    public void createEmpresa(int id, String name) {
        Empresa empresa = new Empresa(id, name);
        emContr.create(empresa);
    }

    public void createCliente(int id, String name, String tel) {
        Cliente cliente = new Cliente(id, name, tel);
        clContr.create(cliente);
    }

    public void addProductoFabricante(Fabricante fabricante, Producto producto) {
        producto.setIdFabricante(fabricante);
        try {
            fbContr.edit(fabricante);
            prContr.edit(producto);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void addEmpresaProducto(Empresa empresa, Producto producto) {
        producto.addEmpresa(empresa);
        empresa.setIdProducto(producto);
        try {
            prContr.edit(producto);
            emContr.edit(empresa);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void addClienteEmpresa(Cliente cliente, Empresa empresa) {
        empresa.addCliente(cliente);
        cliente.addEmpresa(empresa);
        try {
            emContr.edit(empresa);
            clContr.edit(cliente);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
