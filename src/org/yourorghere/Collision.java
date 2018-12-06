package org.yourorghere;

import java.util.ArrayList;

public class Collision {

    //процедура генерации контактов
    public static void generateJoints(Ball ball, ArrayList<ContactJoint> JOINTS) {
        //запускаем проверку для каждого треугольника
        for (Triangle triangle : Terrain.triangles) {
            //проверяем выход за ребра треугольника
            if (checkEdges(ball, triangle)) {
                //проверяем пересечение плоскости треуголника
                double deep = checkPlane(ball, triangle);
                //если вышел за плоскость (глубина > 0) но не прошёл насквозь до полвины
                if (deep > 0 && deep < Solver.BALL.R) {
                    //создаём новый контакт
                    JOINTS.add(new ContactJoint(triangle.planeNormal, deep));
                }
            }
        }
    }

    private static double checkPlane(Ball ball, Triangle quad) {
        //возвращаем глубину проникновения
        return quad.planeDot - Vector.getDP(ball.location, quad.planeNormal) + ball.R;
    }

    private static boolean checkEdges(Ball ball, Triangle quad) {
        for (int i = 0; i < 3; i++) {
            //если проекции на какую-либо нормаль не пересечкаются
            if (quad.edgesDots[i] - Vector.getDP(ball.location, quad.edgesNormal[i]) + ball.R < 0) {
                //возврщаем false
                return false;
            }
        }
        return true;
    }
}
