package org.yourorghere;

import java.awt.Color;

public class Triangle {

    public static final Color COLOR = new Color(0xFFFFFF);
    //вершины
    public final Vector[] vertex;
    //нормаль плоскости
    public final Vector planeNormal;
    //скалярное произведение самого себя на эту нормаль
    public final double planeDot;
    //нормали рёбер
    public final Vector[] edgesNormal;
    //скалярные произведения рёбер на собственные нормали
    public final double[] edgesDots;

    public Triangle(Vector[] vertex) {
        this.vertex = vertex;
        edgesNormal = new Vector[3];
        edgesDots = new double[3];
        //полчаем нормаль плоскости через три точки
        planeNormal = Vector.getPlaneNormal(vertex[0], vertex[1], vertex[2]);
        //нормируем (длина = 1)
        planeNormal.normalize();
        //получаем скалярное произведение
        planeDot = Vector.getDP(vertex[0], planeNormal);
        for (int i = 0; i < 3; i++) {
            //нормаль ребра перпендикурярна нормаль плоскости и данному ребру 
            edgesNormal[i] = Vector.getCrossProduct(Vector.getDiff(vertex[(i + 1) % 3], vertex[i]), planeNormal);
            //нормируем 
            edgesNormal[i].normalize();
            //получаем скалярные произведения
            edgesDots[i] = Vector.getDP(vertex[i], edgesNormal[i]);
        }
    }
}
