package org.yourorghere;

import java.util.ArrayList;

public class Collision {

    //процедура генерации контактов
    public static void generateJoints(ArrayList<ContactJoint> JOINTS) {
        Solver.BALLS.forEach((ball) -> {
            //запускаем проверку для каждого треугольника
            for (Triangle triangle : Terrain.triangles) {
                //проверяем выход за ребра треугольника
                if (checkEdges(ball, triangle)) {
                    //проверяем пересечение плоскости треуголника
                    double deep = checkPlane(ball, triangle);
                    //если вышел за плоскость (глубина > 0) но не прошёл насквозь до полвины
                    if (deep > 0 && deep < ball.R) {
                        //создаём новый контакт
                        JOINTS.add(new ContactJoint(ball, triangle.planeNormal, deep));
                    }
                }
            }
        });
        //проходим по массиву шаров, каждый с каждым
        for (int i = 0; i < Solver.BALLS.size() - 1; i++) {
            for (int j = i + 1; j < Solver.BALLS.size(); j++) {
                //разность положений - из неё можно получить расстояние между ними, а также она будет использована в качестве нормали отскока
                Vector diff = Vector.getDiff(Solver.BALLS.get(i).location, Solver.BALLS.get(j).location);
                //сумма радиусов
                double R = Solver.BALLS.get(i).R + Solver.BALLS.get(j).R;
                //если длина меньше суммы радиусов - шары сталкиваются
                //аналогично, если квадратная длина меньше квадрата суммы радиусов - шары сталкиваются
                if (diff.squreLen() <= R * R) {
                    //добавляем контакт, нормированная разность положений - нормаль отсока, разница между суммой радиусов и её длиной - глубина проникновения в друг друга
                    JOINTS.add(new ContactJoint(Solver.BALLS.get(i), Solver.BALLS.get(j), diff.getNormalized(), R - diff.len()));
                }
            }
        }
    }

    private static double checkPlane(Ball ball, Triangle triangle) {
        //возвращаем глубину проникновения
        return triangle.planeDot - Vector.getDP(ball.location, triangle.planeNormal) + ball.R;
    }

    private static boolean checkEdges(Ball ball, Triangle triangle) {
        for (int i = 0; i < triangle.edgesDots.length; i++) {
            //если проекции на какую-либо нормаль не пересечкаются
            if (triangle.edgesDots[i] - Vector.getDP(ball.location, triangle.edgesNormal[i]) + ball.R < 0) {
                //возврщаем false
                return false;
            }
        }
        return true;
    }
}
