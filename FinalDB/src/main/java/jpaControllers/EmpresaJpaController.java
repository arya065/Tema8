/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaControllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Producto;
import entities.Cliente;
import entities.Empresa;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author alexe
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(List<Empresa> list) {
        for (Empresa em : list) {
            create(em);
        }
    }

    public void create(Empresa empresa) {
        if (empresa.getClienteCollection() == null) {
            empresa.setClienteCollection(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto idProducto = empresa.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                empresa.setIdProducto(idProducto);
            }
            Collection<Cliente> attachedClienteCollection = new ArrayList<Cliente>();
            for (Cliente clienteCollectionClienteToAttach : empresa.getClienteCollection()) {
                clienteCollectionClienteToAttach = em.getReference(clienteCollectionClienteToAttach.getClass(), clienteCollectionClienteToAttach.getIdCliente());
                attachedClienteCollection.add(clienteCollectionClienteToAttach);
            }
            empresa.setClienteCollection(attachedClienteCollection);
            em.persist(empresa);
            if (idProducto != null) {
                idProducto.getEmpresaCollection().add(empresa);
                idProducto = em.merge(idProducto);
            }
            for (Cliente clienteCollectionCliente : empresa.getClienteCollection()) {
                clienteCollectionCliente.getEmpresaCollection().add(empresa);
                clienteCollectionCliente = em.merge(clienteCollectionCliente);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getIdEmpresa());
            Producto idProductoOld = persistentEmpresa.getIdProducto();
            Producto idProductoNew = empresa.getIdProducto();
            Collection<Cliente> clienteCollectionOld = persistentEmpresa.getClienteCollection();
            Collection<Cliente> clienteCollectionNew = empresa.getClienteCollection();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                empresa.setIdProducto(idProductoNew);
            }
            Collection<Cliente> attachedClienteCollectionNew = new ArrayList<Cliente>();
            for (Cliente clienteCollectionNewClienteToAttach : clienteCollectionNew) {
                clienteCollectionNewClienteToAttach = em.getReference(clienteCollectionNewClienteToAttach.getClass(), clienteCollectionNewClienteToAttach.getIdCliente());
                attachedClienteCollectionNew.add(clienteCollectionNewClienteToAttach);
            }
            clienteCollectionNew = attachedClienteCollectionNew;
            empresa.setClienteCollection(clienteCollectionNew);
            empresa = em.merge(empresa);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getEmpresaCollection().remove(empresa);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getEmpresaCollection().add(empresa);
                idProductoNew = em.merge(idProductoNew);
            }
            for (Cliente clienteCollectionOldCliente : clienteCollectionOld) {
                if (!clienteCollectionNew.contains(clienteCollectionOldCliente)) {
                    clienteCollectionOldCliente.getEmpresaCollection().remove(empresa);
                    clienteCollectionOldCliente = em.merge(clienteCollectionOldCliente);
                }
            }
            for (Cliente clienteCollectionNewCliente : clienteCollectionNew) {
                if (!clienteCollectionOld.contains(clienteCollectionNewCliente)) {
                    clienteCollectionNewCliente.getEmpresaCollection().add(empresa);
                    clienteCollectionNewCliente = em.merge(clienteCollectionNewCliente);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empresa.getIdEmpresa();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getIdEmpresa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            Producto idProducto = empresa.getIdProducto();
            if (idProducto != null) {
                idProducto.getEmpresaCollection().remove(empresa);
                idProducto = em.merge(idProducto);
            }
            Collection<Cliente> clienteCollection = empresa.getClienteCollection();
            for (Cliente clienteCollectionCliente : clienteCollection) {
                clienteCollectionCliente.getEmpresaCollection().remove(empresa);
                clienteCollectionCliente = em.merge(clienteCollectionCliente);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Empresa getLastEmpresa() {
        List<Empresa> tmp = findEmpresaEntities();
        return tmp.get(tmp.size() - 1);
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Empresa findEmpresa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
