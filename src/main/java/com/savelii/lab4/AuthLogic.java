package com.savelii.lab4;

import javax.persistence.*;
import java.util.List;

public class AuthLogic {
    public Object[] isValid(String key)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query q = em.createQuery("select c from Auth c where c.api_key like ?1")
                .setParameter(1, key);

        List<Auth> playerList = q.getResultList();

        em.close();
        emf.close();

        return new Object[]{playerList.size() == 1, playerList.size() == 1 ? playerList.get(0).getId() : null};
    }
}
