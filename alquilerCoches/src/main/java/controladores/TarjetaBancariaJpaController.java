/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Cliente;
import entidades.TarjetaBancaria;
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
public class TarjetaBancariaJpaController implements Serializable {

    public TarjetaBancariaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TarjetaBancaria tarjetaBancaria) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente = tarjetaBancaria.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getId());
                tarjetaBancaria.setCliente(cliente);
            }
            em.persist(tarjetaBancaria);
            if (cliente != null) {
                TarjetaBancaria oldIdTarjetaOfCliente = cliente.getTarjeta();
                if (oldIdTarjetaOfCliente != null) {
                    oldIdTarjetaOfCliente.setCliente(null);
                    oldIdTarjetaOfCliente = em.merge(oldIdTarjetaOfCliente);
                }
                cliente.setTarjeta(tarjetaBancaria);
                cliente = em.merge(cliente);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TarjetaBancaria tarjetaBancaria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TarjetaBancaria persistentTarjetaBancaria = em.find(TarjetaBancaria.class, tarjetaBancaria.getId());
            Cliente clienteOld = persistentTarjetaBancaria.getCliente();
            Cliente clienteNew = tarjetaBancaria.getCliente();
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getId());
                tarjetaBancaria.setCliente(clienteNew);
            }
            tarjetaBancaria = em.merge(tarjetaBancaria);
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.setTarjeta(null);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                TarjetaBancaria oldIdTarjetaOfCliente = clienteNew.getTarjeta();
                if (oldIdTarjetaOfCliente != null) {
                    oldIdTarjetaOfCliente.setCliente(null);
                    oldIdTarjetaOfCliente = em.merge(oldIdTarjetaOfCliente);
                }
                clienteNew.setTarjeta(tarjetaBancaria);
                clienteNew = em.merge(clienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarjetaBancaria.getId();
                if (findTarjetaBancaria(id) == null) {
                    throw new NonexistentEntityException("The tarjetaBancaria with id " + id + " no longer exists.");
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
            TarjetaBancaria tarjetaBancaria;
            try {
                tarjetaBancaria = em.getReference(TarjetaBancaria.class, id);
                tarjetaBancaria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjetaBancaria with id " + id + " no longer exists.", enfe);
            }
            Cliente cliente = tarjetaBancaria.getCliente();
            if (cliente != null) {
                cliente.setTarjeta(null);
                cliente = em.merge(cliente);
            }
            em.remove(tarjetaBancaria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TarjetaBancaria> findTarjetaBancariaEntities() {
        return findTarjetaBancariaEntities(true, -1, -1);
    }

    public List<TarjetaBancaria> findTarjetaBancariaEntities(int maxResults, int firstResult) {
        return findTarjetaBancariaEntities(false, maxResults, firstResult);
    }

    private List<TarjetaBancaria> findTarjetaBancariaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TarjetaBancaria.class));
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

    public TarjetaBancaria findTarjetaBancaria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TarjetaBancaria.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetaBancariaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TarjetaBancaria> rt = cq.from(TarjetaBancaria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    // Método añadido, usando una named query de la entidad vehículo
    public TarjetaBancaria findByNumero(String numero){
        EntityManager em = getEntityManager();
        // Se crea la query usando el nombre de la named query
        Query q = em.createNamedQuery("TarjetaBancaria.findByNumero");
        // Se establece el parámetro de la consulta
        q.setParameter("numero", numero);
        return (TarjetaBancaria)q.getSingleResult();
    }
}
