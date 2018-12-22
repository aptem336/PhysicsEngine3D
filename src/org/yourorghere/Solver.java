package org.yourorghere;

import java.awt.Color;
import java.util.ArrayList;
import static org.yourorghere.BC.gl;
import static org.yourorghere.BC.glut;

public class Solver {

    //коэффициент упругости
    public static double E = 0.125d;
    //коэффициент трения
    public static double F = 0.125d;
    //массив шаров
    public static final ArrayList<Ball> BALLS = new ArrayList<>();
    //триггер движения
    public static boolean move = true;
    //массив контактов с треуголниками
    private static final ArrayList<ContactJoint> JOINTS = new ArrayList<>();
    //количество итераций на разрешение импульсов, проникновений
    private static final int IIC = 20, PIC = 20;
    //переключатель сброса
    public static boolean reset = true;

    public static void step() {
        //если переключатель сброса включен
        if (reset) {
            //чистим массив сфер
            BALLS.clear();
            //переключаем
            reset = false;
        }
        JOINTS.clear();
        //процедура генерации контактов
        Collision.generateJoints(JOINTS);
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
        //отрисовываем каждую сферу
        BALLS.forEach((ball) -> {
            buildSphere(ball.location.x, ball.location.y, ball.location.z, ball.R, Color.gray);
        });
        //если движение разрешено
        if (move) {
            //интегрируем позицию
            BALLS.forEach((ball) -> {
                ball.integrate();
            });
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
