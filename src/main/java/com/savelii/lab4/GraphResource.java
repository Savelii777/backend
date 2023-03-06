package com.savelii.lab4;

import org.jboss.ejb3.annotation.SecurityDomain;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/graph")
@SecurityDomain("other")
public class GraphResource {

    HashMap<Long, ArrayList<Point>> data = new HashMap<>();

    public GraphResource()
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query q = em.createQuery("select c from Point c");

        List<Point> playerList = q.getResultList();
        for(Point p : playerList)
        {
            Long id = p.getOwner_id();
            if (this.data.containsKey(id))
            {
                this.data.get(id).add(p);
            }
            else
            {
                ArrayList<Point> add = new ArrayList<>();
                add.add(new Point(p.getX(), p.getY(), p.getR(), p.isInside(), p.getOwner_id()));
                this.data.put(id, add);
            }
        }

        tx.commit();
        em.close();
        emf.close();
    }

    @GET
    @Path("points/get")
    public Response getData(@Context HttpServletRequest request)
    {
        String key = request.getParameter("key");

        AuthLogic authLogic = new AuthLogic();

        Object[] auth = authLogic.isValid(key);

        boolean isValid = (boolean) auth[0];
        if (!isValid)
            return Response.status(500).build();

        Long id = (Long) auth[1];

        Object result  = data.get(id);

        System.out.println(result);
        if(result == null){
            ArrayList<Point> empty = new ArrayList<>();
            return Response.ok(empty).build();
        }
        return Response.ok(result).build();

    }


    @POST
    @Path("submit")
    public Response submit(Object data)
    {
        String key = "";
        double x = 0;
        double y = 0;
        double r = 0;
        String input = data.toString();
        Pattern pattern = Pattern.compile("key=(\\w+),\\s*shot\\s*=\\s*\\{r=(-?\\d+(?:\\.\\d+)?),\\s*x=(-?\\d+(?:\\.\\d+)?),\\s*y=(-?\\d+(?:\\.\\d+)?)\\}");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
           key = matcher.group(1); // "YGxAKR1D87iUUPpDgD9z"
            r = Double.parseDouble(matcher.group(2)); // 1
            x = Double.parseDouble(matcher.group(3)); // 1
            y = Double.parseDouble(matcher.group(4)); // 1
            System.out.println(x);
            System.out.println(y);
            System.out.println(r);
        }


        AuthLogic authLogic = new AuthLogic();

        Object[] auth = authLogic.isValid(key);

        boolean isValid = (boolean) auth[0];
        if (!isValid)
            return Response.status(500).build();


        Long owner_id = (Long) auth[1];

        System.out.println(key);
        System.out.println(x);
        System.out.println(y);
        System.out.println(r);
        System.out.println(owner_id);

        Point point = new Point(x, y, r, checkIfInside(x, y, r), owner_id);

        addData(point);
        return Response.ok(point).build();

    }


    @POST
    @Path("clear")
    public Response clear(String key)
    {
        AuthLogic authLogic = new AuthLogic();
        Object[] auth = authLogic.isValid(key);
        Long auth_id = (Long) auth[1];
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.createQuery("delete from Point p where p.owner_id = :auth_id")
                .setParameter("auth_id", auth_id)
                .executeUpdate();

        tx.commit();
        em.close();
        data.clear();
        emf.close();
        return Response.ok().build();

    }

    public void reset()
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.createQuery("delete from Point").executeUpdate();

        tx.commit();
        em.close();
        data.clear();
        emf.close();
    }

    public void addData(Point data)
    {


        Long id = data.getOwner_id();
        if (this.data.containsKey(id))
        {
            this.data.get(id).add(data);
        }
        else
        {
            ArrayList<Point> add = new ArrayList<>();
            add.add(data);
            this.data.put(id, add);
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Point data1 = new Point(data.getX(), data.getY(), data.getR(), data.isInside(), data.getOwner_id());

        em.persist(data1);
        tx.commit();
        em.close();
        emf.close();
    }


    private boolean checkIfInside(double x, double y, double r)
    {
        if (x > 0)
        {
            if (y > 0)
                return (r*r/6)>=(Math.abs(x)*Math.abs(y));
            else
                return x <= r && Math.abs(y) <= r;
        }
        else
        {
            if (y > 0)
                return y*y <= r*r/4. - x*x;
            else
                return false;
        }
    }
}