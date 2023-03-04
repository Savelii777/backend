package com.savelii.lab4;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/auth")
public class LoginResource
{

    @POST
    @Path("/login")
    public Response login(String login, String pass)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query q = em.createQuery("select c from Auth c where c.login like ?1 and c.password like ?2")
                .setParameter(1, login)
                .setParameter(2, Auth.encrypt(pass));

        List<Auth> playerList = q.getResultList();
        em.close();
        emf.close();

        if (playerList.size() == 1){
            System.out.println(playerList.get(0).getApi_key());
            Token token = new Token(playerList.get(0).getApi_key(),"success");
            return Response.ok(token).build();
        }
        else
        {
            System.out.println("no auth");
            return Response.ok("").build();
        }
    }

    @POST
    @Path("/register")
    public Response register(String login, String pass)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Auth auth = new Auth(login, pass);
        Token token = new Token(auth.getApi_key(),"success");


        em.persist(auth);
        tx.commit();
        em.close();
        emf.close();

        return Response.ok(token).build();
    }

    @POST
    @Path("/check")
    @Produces("text/plain")
    public String check(@DefaultValue("") @FormParam("key") String key)
    {
        AuthLogic authLogic = new AuthLogic();
        if ((boolean) authLogic.isValid(key)[0])
            return key;
        return "";
    }


}