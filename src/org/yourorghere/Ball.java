package org.yourorghere;

public class Ball {

    //ускорение свободного падения
    private static final double G = 9.8d;
    //квант времени
    private static final double DT = 1d / 300d;
    //радиус
    public double R;
    //вектора
    //положения
    public final Vector location;
    //скорости
    public final Vector velocity;
    //псевдоскоростей
    public final Vector pvelocity;

    public Ball(double R) {
        //получаем радиус
        this.R = R;
        //выделяем память под ветора
        location = new Vector();
        velocity = new Vector();
        pvelocity = new Vector();
    }
    
    //интегратор эйлера
    public void integrateLocation() {
        //скорость по оси Y уменьшается на G (гравитация действует вниз)
        velocity.y -= G;
        //добавляем к положению псевдоскорость
        location.add(pvelocity);
        //обнуляем псевдоскорость
        pvelocity.set(Vector.NULL);
        //добавляем скорость * квант времени
        location.add(velocity, DT);
    }

    //бросок
    public void toss(Vector toss) {
        //добавляем к скорости импульс броска
        this.velocity.add(toss);
    }
}
