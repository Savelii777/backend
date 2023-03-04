package com.savelii.lab4;

import javax.persistence.*;

@Entity
@Table(name = "Point")
public class Point
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double x;
    private double y;
    private double r;
    private boolean inside;
    private Long owner_id;

    public Point(double x, double y, double r, boolean inside, Long owner_id)
    {
        this.x = x;
        this.y = y;
        this.r = r;
        this.inside = inside;
        this.owner_id = owner_id;
    }

    public Point(Point data)
    {
        this.x = data.getX();
        this.y = data.y;
        this.r = data.getR();
        this.inside = data.isInside();
        this.owner_id = data.getOwner_id();
    }

    public Point() {};

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public double getR() {
        return r;
    }

    public boolean isInside() {
        return inside;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setY_for_slider(double y_for_slider) {
        this.y = y_for_slider / 100.;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setInside(boolean inside) {
        this.inside = inside;
    }

    @Override
    public String toString() {
        return String.format("[%f,%f,%f,%b]", x, y, r, inside);
    }

    public Long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Long owner_id) {
        this.owner_id = owner_id;
    }
}
