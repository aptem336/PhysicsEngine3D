package org.yourorghere;

public class Ball extends Moveable {

    //ускорение свободного падения
    private static final double G = 9.8d;
    //радиус
    public double R;

    public Ball(double R, double density) {
        super();
        //получаем радиус
        this.R = R;
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
