/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Fabricante;
import entities.Empresa;
import entities.Producto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaController.exceptions.IllegalOrphanException;
import jpaController.exceptions.NonexistentEntityException;

/**
 *
 * @author alexe
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) {
        if (producto.getEmpresaCollection() == null) {
            producto.setEmpresaCollection(new ArrayList<Empresa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fabricante idFabricante = producto.getIdFabricante();
            if (idFabricante != null) {
                idFabricante = em.getReference(idFabricante.getClass(), idFabricante.getIdFabricante());
                producto.setIdFabricante(idFabricante);
            }
            Collection<Empresa> attachedEmpresaCollection = new ArrayList<Empresa>();
            for (Empresa empresaCollectionEmpresaToAttach : producto.getEmpresaCollection()) {
                empresaCollectionEmpresaToAttach = em.getReference(empresaCollectionEmpresaToAttach.getClass(), empresaCollectionEmpresaToAttach.getIdEmpresa());
                attachedEmpresaCollection.add(empresaCollectionEmpresaToAttach);
            }
            producto.setEmpresaCollection(attachedEmpresaCollection);
            em.persist(producto);
            if (idFabricante != null) {
                idFabricante.getProductoCollection().add(producto);
                idFabricante = em.merge(idFabricante);
            }
            for (Empresa empresaCollectionEmpresa : producto.getEmpresaCollection()) {
                Producto oldIdProductoOfEmpresaCollectionEmpresa = empresaCollectionEmpresa.getIdProducto();
                empresaCollectionEmpresa.setIdProducto(producto);
                empresaCollectionEmpresa = em.merge(empresaCollectionEmpresa);
                if (oldIdProductoOfEmpresaCollectionEmpresa != null) {
                    oldIdProductoOfEmpresaCollectionEmpresa.getEmpresaCollection().remove(empresaCollectionEmpresa);
                    oldIdProductoOfEmpresaCollectionEmpresa = em.merge(oldIdProductoOfEmpresaCollectionEmpresa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            Fabricante idFabricanteOld = persistentProducto.getIdFabricante();
            Fabricante idFabricanteNew = producto.getIdFabricante();
            Collection<Empresa> empresaCollectionOld = persistentProducto.getEmpresaCollection();
            Collection<Empresa> empresaCollectionNew = producto.getEmpresaCollection();
            List<String> illegalOrphanMessages = null;
            for (Empresa empresaCollectionOldEmpresa : empresaCollectionOld) {
                if (!empresaCollectionNew.contains(empresaCollectionOldEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empresa " + empresaCollectionOldEmpresa + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idFabricanteNew != null) {
                idFabricanteNew = em.getReference(idFabricanteNew.getClass(), idFabricanteNew.getIdFabricante());
                producto.setIdFabricante(idFabricanteNew);
            }
            Collection<Empresa> attachedEmpresaCollectionNew = new ArrayList<Empresa>();
            for (Empresa empresaCollectionNewEmpresaToAttach : empresaCollectionNew) {
                empresaCollectionNewEmpresaToAttach = em.getReference(empresaCollectionNewEmpresaToAttach.getClass(), empresaCollectionNewEmpresaToAttach.getIdEmpresa());
                attachedEmpresaCollectionNew.add(empresaCollectionNewEmpresaToAttach);
            }
            empresaCollectionNew = attachedEmpresaCollectionNew;
            producto.setEmpresaCollection(empresaCollectionNew);
            producto = em.merge(producto);
            if (idFabricanteOld != null && !idFabricanteOld.equals(idFabricanteNew)) {
                idFabricanteOld.getProductoCollection().remove(producto);
                idFabricanteOld = em.merge(idFabricanteOld);
            }
            if (idFabricanteNew != null && !idFabricanteNew.equals(idFabricanteOld)) {
                idFabricanteNew.getProductoCollection().add(producto);
                idFabricanteNew = em.merge(idFabricanteNew);
            }
            for (Empresa empresaCollectionNewEmpresa : empresaCollectionNew) {
                if (!empresaCollectionOld.contains(empresaCollectionNewEmpresa)) {
                    Producto oldIdProductoOfEmpresaCollectionNewEmpresa = empresaCollectionNewEmpresa.getIdProducto();
                    empresaCollectionNewEmpresa.setIdProducto(producto);
                    empresaCollectionNewEmpresa = em.merge(empresaCollectionNewEmpresa);
                    if (oldIdProductoOfEmpresaCollectionNewEmpresa != null && !oldIdProductoOfEmpresaCollectionNewEmpresa.equals(producto)) {
                        oldIdProductoOfEmpresaCollectionNewEmpresa.getEmpresaCollection().remove(empresaCollectionNewEmpresa);
                        oldIdProductoOfEmpresaCollectionNewEmpresa = em.merge(oldIdProductoOfEmpresaCollectionNewEmpresa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Empresa> empresaCollectionOrphanCheck = producto.getEmpresaCollection();
            for (Empresa empresaCollectionOrphanCheckEmpresa : empresaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Empresa " + empresaCollectionOrphanCheckEmpresa + " in its empresaCollection field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Fabricante idFabricante = producto.getIdFabricante();
            if (idFabricante != null) {
                idFabricante.getProductoCollection().remove(producto);
                idFabricante = em.merge(idFabricante);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
