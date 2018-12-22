package org.yourorghere;

public class Ball {

    //ускорение свободного падения
    private static final double G = 9.8d;
    //радиус
    public double R;
    //вектора:
    //положения
    public final Vector location;
    //скорости
    public final Vector velocity;
    //псевдоскоростей
    public final Vector pvelocity;
    //инваертированная масса, для вычислений удобнее хранить её именно в таком виде
    public final double iMass;

    public Ball(double R, double density) {
        //получаем радиус
        this.R = R;
        //выделяем память под вектора
        location = new Vector();
        velocity = new Vector();
        pvelocity = new Vector();
        //масса через оббъём шара
        iMass = 1.0d / (4.0d / 3.0d * Math.PI * Math.pow(R, 3) * density);
    }

    //интегратор эйлера
    public void integrate() {
        //скорость по оси Y уменьшается на G (гравитация действует вниз)
        velocity.y -= G;
        //добавляем к положению псевдоскорость
        location.add(pvelocity);
        //обнуляем псевдоскорость
        pvelocity.set(Vector.NULL);
        //добавляем скорость * квант времени
        location.add(velocity, 1.0d / 100.0d);
    }
}
