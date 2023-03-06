package com.savelii.lab4;
import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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




    @GET
    @Path("/getLogin")
    @Produces("application/json")
    public Response loginGetClick(@Context HttpServletRequest request)
    {

        int rows = Integer.parseInt(request.getParameter("rows"));
        int start = Integer.parseInt(request.getParameter("start"));

        List<MyObject> objects = new ArrayList<>();

        System.out.println(rows);
        // Создаем объекты и вложенные объекты с заданным свойством title
        if(rows == start)
        {
            for (int i = 0; i <= 9; i++) {
                MyObject object = new MyObject(i);
                objects.add(object);

            }
        }else {

            for (int i = start; i <= rows; i++) {
            MyObject object = new MyObject(i);
            objects.add(object);

        }}


        return Response.ok(objects).build();

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
//    @Produces("text/plain")
    public String check(@DefaultValue("") @FormParam("key") String key)
    {
        AuthLogic authLogic = new AuthLogic();
        if ((boolean) authLogic.isValid(key)[0])
            return key;
        return "";
    }


}