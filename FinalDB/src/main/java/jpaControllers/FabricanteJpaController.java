/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaControllers;

import entities.Fabricante;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Producto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author alexe
 */
public class FabricanteJpaController implements Serializable {

    public FabricanteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fabricante fabricante) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto = fabricante.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getIdProducto());
                fabricante.setProducto(producto);
            }
            em.persist(fabricante);
            if (producto != null) {
                Fabricante oldIdFabricanteOfProducto = producto.getIdFabricante();
                if (oldIdFabricanteOfProducto != null) {
                    oldIdFabricanteOfProducto.setProducto(null);
                    oldIdFabricanteOfProducto = em.merge(oldIdFabricanteOfProducto);
                }
                producto.setIdFabricante(fabricante);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fabricante fabricante) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fabricante persistentFabricante = em.find(Fabricante.class, fabricante.getIdFabricante());
            Producto productoOld = persistentFabricante.getProducto();
            Producto productoNew = fabricante.getProducto();
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getIdProducto());
                fabricante.setProducto(productoNew);
            }
            fabricante = em.merge(fabricante);
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.setIdFabricante(null);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                Fabricante oldIdFabricanteOfProducto = productoNew.getIdFabricante();
                if (oldIdFabricanteOfProducto != null) {
                    oldIdFabricanteOfProducto.setProducto(null);
                    oldIdFabricanteOfProducto = em.merge(oldIdFabricanteOfProducto);
                }
                productoNew.setIdFabricante(fabricante);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = fabricante.getIdFabricante();
                if (findFabricante(id) == null) {
                    throw new NonexistentEntityException("The fabricante with id " + id + " no longer exists.");
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
            Fabricante fabricante;
            try {
                fabricante = em.getReference(Fabricante.class, id);
                fabricante.getIdFabricante();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fabricante with id " + id + " no longer exists.", enfe);
            }
            Producto producto = fabricante.getProducto();
            if (producto != null) {
                producto.setIdFabricante(null);
                producto = em.merge(producto);
            }
            em.remove(fabricante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fabricante> findFabricanteEntities() {
        return findFabricanteEntities(true, -1, -1);
    }

    public List<Fabricante> findFabricanteEntities(int maxResults, int firstResult) {
        return findFabricanteEntities(false, maxResults, firstResult);
    }

    private List<Fabricante> findFabricanteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fabricante.class));
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

    public Fabricante findFabricante(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fabricante.class, id);
        } finally {
            em.close();
        }
    }

    public int getFabricanteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fabricante> rt = cq.from(Fabricante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
