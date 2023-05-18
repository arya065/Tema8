/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Alquiler;
import entidades.Cliente;
import entidades.Vehiculo;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author carlos
 */
public class AlquilerJpaController implements Serializable {

    public AlquilerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alquiler alquiler) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = alquiler.getCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getId());
                alquiler.setCliente(idCliente);
            }
            Vehiculo idVehiculo = alquiler.getVehiculo();
            if (idVehiculo != null) {
                idVehiculo = em.getReference(idVehiculo.getClass(), idVehiculo.getId());
                alquiler.setVehiculo(idVehiculo);
            }
            em.persist(alquiler);
            if (idCliente != null) {
                idCliente.getAlquilerList().add(alquiler);
                idCliente = em.merge(idCliente);
            }
            if (idVehiculo != null) {
                idVehiculo.getAlquilerList().add(alquiler);
                idVehiculo = em.merge(idVehiculo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alquiler alquiler) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alquiler persistentAlquiler = em.find(Alquiler.class, alquiler.getId());
            Cliente idClienteOld = persistentAlquiler.getCliente();
            Cliente idClienteNew = alquiler.getCliente();
            Vehiculo idVehiculoOld = persistentAlquiler.getVehiculo();
            Vehiculo idVehiculoNew = alquiler.getVehiculo();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getId());
                alquiler.setCliente(idClienteNew);
            }
            if (idVehiculoNew != null) {
                idVehiculoNew = em.getReference(idVehiculoNew.getClass(), idVehiculoNew.getId());
                alquiler.setVehiculo(idVehiculoNew);
            }
            alquiler = em.merge(alquiler);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getAlquilerList().remove(alquiler);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getAlquilerList().add(alquiler);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idVehiculoOld != null && !idVehiculoOld.equals(idVehiculoNew)) {
                idVehiculoOld.getAlquilerList().remove(alquiler);
                idVehiculoOld = em.merge(idVehiculoOld);
            }
            if (idVehiculoNew != null && !idVehiculoNew.equals(idVehiculoOld)) {
                idVehiculoNew.getAlquilerList().add(alquiler);
                idVehiculoNew = em.merge(idVehiculoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alquiler.getId();
                if (findAlquiler(id) == null) {
                    throw new NonexistentEntityException("The alquiler with id " + id + " no longer exists.");
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
            Alquiler alquiler;
            try {
                alquiler = em.getReference(Alquiler.class, id);
                alquiler.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alquiler with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = alquiler.getCliente();
            if (idCliente != null) {
                idCliente.getAlquilerList().remove(alquiler);
                idCliente = em.merge(idCliente);
            }
            Vehiculo idVehiculo = alquiler.getVehiculo();
            if (idVehiculo != null) {
                idVehiculo.getAlquilerList().remove(alquiler);
                idVehiculo = em.merge(idVehiculo);
            }
            em.remove(alquiler);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alquiler> findAlquilerEntities() {
        return findAlquilerEntities(true, -1, -1);
    }

    public List<Alquiler> findAlquilerEntities(int maxResults, int firstResult) {
        return findAlquilerEntities(false, maxResults, firstResult);
    }

    private List<Alquiler> findAlquilerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alquiler.class));
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

    public Alquiler findAlquiler(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alquiler.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlquilerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alquiler> rt = cq.from(Alquiler.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
