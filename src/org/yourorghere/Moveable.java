package org.yourorghere;

public abstract class Moveable {

    //вектора:
    //положения
    public final Vector location;
    //скорости
    public final Vector velocity;
    //псевдоскоростей
    public final Vector pvelocity;
    //инваертированная масса, для вычислений удобнее хранить её именно в таком виде
    public double iMass;

    public Moveable() {
        //выделяем память под вектора
        location = new Vector();
        velocity = new Vector();
        pvelocity = new Vector();
    }

}
