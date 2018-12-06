package org.yourorghere;

import java.awt.Color;
import java.util.ArrayList;
import static org.yourorghere.FF.gl;
import static org.yourorghere.FF.glut;

public class Solver {

    //коэффициент упругости
    public static double E = 0.125d;
    //коэффициент трения
    public static double F = 0.125d;
    //управляемый объект
    public static final Ball BALL = new Ball(25d);
    //триггер движения
    public static boolean move = false;
    //массив контактов с треуголниками
    private static final ArrayList<ContactJoint> JOINTS = new ArrayList<>();
    //количество итераций на разрешение импульсов, проникновений
    private static final int IIC = 20, PIC = 20;

    public static void step() {
        JOINTS.clear();
        //процедура генерации контактов
        Collision.generateJoints(BALL, JOINTS);
        //IIC раз 
        for (int i = 0; i < IIC; i++) {
            //решаем импульсы
            JOINTS.forEach((joint) -> {
                joint.solveImpulse();
            });
        }
        //PPC раз
        for (int i = 0; i < PIC; i++) {
            //решаем проникновения
            JOINTS.forEach((joint) -> {
                joint.solvePenetration();
            });
        }
        //отрисовываем сферу
        buildSphere(BALL.location.x, BALL.location.y, BALL.location.z, BALL.R, Color.red);
        //если движение разрешено
        if (move) {
            //интегрируем позицию
            BALL.integrateLocation();
            //иначе    
        } else {
            //отрисовываем линию броска
            Build.buildLine(new Vector[]{Vector.NULL, Vector.getProduct(Listener.diff, 0.25d)}, Color.red);
        }
    }

    private static final int SLICES = 20, STACKS = 20;

    private static void buildSphere(double x, double y, double z, double size, Color color) {
        gl.glPushMatrix();
        //переносим сферу в точку (x, y, z)
        gl.glTranslated(x, y, z);
        //устанавливаем цвет в float
        gl.glColor4d(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d, color.getAlpha() / 255d);
        glut.glutSolidSphere(size, SLICES, STACKS);
        gl.glPopMatrix();
    }
}
