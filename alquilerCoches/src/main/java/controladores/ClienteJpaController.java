/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.Alquiler;
import entidades.Cliente;
import entidades.TarjetaBancaria;
import java.io.Serializable;
import java.util.ArrayList;
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
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getAlquilerList() == null) {
            cliente.setAlquilerList(new ArrayList<Alquiler>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TarjetaBancaria idTarjeta = cliente.getTarjeta();
            if (idTarjeta != null) {
                idTarjeta = em.getReference(idTarjeta.getClass(), idTarjeta.getId());
                cliente.setTarjeta(idTarjeta);
            }
            List<Alquiler> attachedAlquilerList = new ArrayList<Alquiler>();
            for (Alquiler alquilerListAlquilerToAttach : cliente.getAlquilerList()) {
                alquilerListAlquilerToAttach = em.getReference(alquilerListAlquilerToAttach.getClass(), alquilerListAlquilerToAttach.getId());
                attachedAlquilerList.add(alquilerListAlquilerToAttach);
            }
            cliente.setAlquilerList(attachedAlquilerList);
            em.persist(cliente);
            if (idTarjeta != null) {
                Cliente oldClienteOfIdTarjeta = idTarjeta.getCliente();
                if (oldClienteOfIdTarjeta != null) {
                    oldClienteOfIdTarjeta.setTarjeta(null);
                    oldClienteOfIdTarjeta = em.merge(oldClienteOfIdTarjeta);
                }
                idTarjeta.setCliente(cliente);
                idTarjeta = em.merge(idTarjeta);
            }
            for (Alquiler alquilerListAlquiler : cliente.getAlquilerList()) {
                Cliente oldIdClienteOfAlquilerListAlquiler = alquilerListAlquiler.getCliente();
                alquilerListAlquiler.setCliente(cliente);
                alquilerListAlquiler = em.merge(alquilerListAlquiler);
                if (oldIdClienteOfAlquilerListAlquiler != null) {
                    oldIdClienteOfAlquilerListAlquiler.getAlquilerList().remove(alquilerListAlquiler);
                    oldIdClienteOfAlquilerListAlquiler = em.merge(oldIdClienteOfAlquilerListAlquiler);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getId());
            TarjetaBancaria idTarjetaOld = persistentCliente.getTarjeta();
            TarjetaBancaria idTarjetaNew = cliente.getTarjeta();
            List<Alquiler> alquilerListOld = persistentCliente.getAlquilerList();
            List<Alquiler> alquilerListNew = cliente.getAlquilerList();
            List<String> illegalOrphanMessages = null;
            for (Alquiler alquilerListOldAlquiler : alquilerListOld) {
                if (!alquilerListNew.contains(alquilerListOldAlquiler)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alquiler " + alquilerListOldAlquiler + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTarjetaNew != null) {
                idTarjetaNew = em.getReference(idTarjetaNew.getClass(), idTarjetaNew.getId());
                cliente.setTarjeta(idTarjetaNew);
            }
            List<Alquiler> attachedAlquilerListNew = new ArrayList<Alquiler>();
            for (Alquiler alquilerListNewAlquilerToAttach : alquilerListNew) {
                alquilerListNewAlquilerToAttach = em.getReference(alquilerListNewAlquilerToAttach.getClass(), alquilerListNewAlquilerToAttach.getId());
                attachedAlquilerListNew.add(alquilerListNewAlquilerToAttach);
            }
            alquilerListNew = attachedAlquilerListNew;
            cliente.setAlquilerList(alquilerListNew);
            cliente = em.merge(cliente);
            if (idTarjetaOld != null && !idTarjetaOld.equals(idTarjetaNew)) {
                idTarjetaOld.setCliente(null);
                idTarjetaOld = em.merge(idTarjetaOld);
            }
            if (idTarjetaNew != null && !idTarjetaNew.equals(idTarjetaOld)) {
                Cliente oldClienteOfIdTarjeta = idTarjetaNew.getCliente();
                if (oldClienteOfIdTarjeta != null) {
                    oldClienteOfIdTarjeta.setTarjeta(null);
                    oldClienteOfIdTarjeta = em.merge(oldClienteOfIdTarjeta);
                }
                idTarjetaNew.setCliente(cliente);
                idTarjetaNew = em.merge(idTarjetaNew);
            }
            for (Alquiler alquilerListNewAlquiler : alquilerListNew) {
                if (!alquilerListOld.contains(alquilerListNewAlquiler)) {
                    Cliente oldIdClienteOfAlquilerListNewAlquiler = alquilerListNewAlquiler.getCliente();
                    alquilerListNewAlquiler.setCliente(cliente);
                    alquilerListNewAlquiler = em.merge(alquilerListNewAlquiler);
                    if (oldIdClienteOfAlquilerListNewAlquiler != null && !oldIdClienteOfAlquilerListNewAlquiler.equals(cliente)) {
                        oldIdClienteOfAlquilerListNewAlquiler.getAlquilerList().remove(alquilerListNewAlquiler);
                        oldIdClienteOfAlquilerListNewAlquiler = em.merge(oldIdClienteOfAlquilerListNewAlquiler);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Alquiler> alquilerListOrphanCheck = cliente.getAlquilerList();
            for (Alquiler alquilerListOrphanCheckAlquiler : alquilerListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Alquiler " + alquilerListOrphanCheckAlquiler + " in its alquilerList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TarjetaBancaria idTarjeta = cliente.getTarjeta();
            if (idTarjeta != null) {
                idTarjeta.setCliente(null);
                idTarjeta = em.merge(idTarjeta);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    // Método añadido, usando una named query de la entidad vehículo
    public Cliente findByNif(String nif){
        EntityManager em = getEntityManager();
        // Se crea la query usando el nombre de la named query
        Query q = em.createNamedQuery("Cliente.findByNif");
        // Se establece el parámetro de la consulta
        q.setParameter("nif", nif);
        return (Cliente)q.getSingleResult();
    }
    
}
