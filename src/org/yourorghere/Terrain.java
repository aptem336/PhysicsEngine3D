package org.yourorghere;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class Terrain {

    //размер карты, шаг получения точек
    private static int mapSize, stepSize;
    //байтовый массив высот
    private static byte[] heightMap;
    //массив получаемых треуголников
    public static Triangle[] triangles;

    public static void load(String fileName, int mapSize, int stepSize) {
        Terrain.mapSize = mapSize;
        Terrain.stepSize = stepSize;
        //читаем в массив
        heightMap = read(fileName);
        //обнуляем массив треугольников
        triangles = new Triangle[0];
        //генерируем нвоые треугольники
        for (int x = 0; x < mapSize; x += stepSize) {
            for (int y = 0; y < mapSize; y += stepSize) {
                //выделяем память для двух новых
                triangles = Arrays.copyOf(triangles, triangles.length + 2);
                //получаем 4 необходимые точки
                Vector[] vertex = getVertexs(x, y);
                //генерируем два треугольника
                // 0 --- 1
                // |  /  |
                // 3 --- 2
                triangles[triangles.length - 2] = new Triangle(new Vector[]{vertex[0], vertex[1], vertex[3]});
                triangles[triangles.length - 1] = new Triangle(new Vector[]{vertex[1], vertex[2], vertex[3]});
            }
        }
    }

    private static Vector[] getVertexs(int x, int y) {
        //генерируем 4 точки по квадрату 
        return new Vector[]{
            new Vector(x - mapSize / 2, getHeight(x, y) - 256, y),
            new Vector(x - mapSize / 2, getHeight(x, y + stepSize) - 256, y + stepSize),
            new Vector(x - mapSize / 2 + stepSize, getHeight(x + stepSize, y + stepSize) - 256, y + stepSize),
            new Vector(x - mapSize / 2 + stepSize, getHeight(x + stepSize, y) - 256, y)};

    }

    private static byte[] read(String fileName) {
        //выделяем память под высоты
        byte[] map = new byte[mapSize * mapSize];
        //создаём поток
        FileInputStream input;
        try {
            //инициализируем
            input = new FileInputStream(fileName);
            //читаем в массив mS*mS байт
            input.read(map, 0, mapSize * mapSize);
            input.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return map;
    }

    private static double getHeight(int x, int z) {
        //усекаем x и z чтобы они не могли выти за пределы массива высот
        int mapX = x % mapSize;
        int mapZ = z % mapSize;
        //получаем высоту с необходимым индексом
        return (heightMap[mapX + (mapZ * mapSize)] & 0xFF) * 2d - 512d;
    }

}
