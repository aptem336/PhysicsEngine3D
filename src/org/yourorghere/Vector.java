package org.yourorghere;

public class Vector {

    public final static Vector NULL = new Vector();

    public double x, y, z;

    public Vector() {
        x = y = z = 0;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public void add(Vector vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public void add(Vector vector, double value) {
        x += vector.x * value;
        y += vector.y * value;
        z += vector.z * value;
    }

    public double len() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void normalize() {
        double len = len();
        x /= len;
        y /= len;
        z /= len;
    }

    public static double getDP(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    //веторное произведение даЄт вектор перпенидикул€рный исходным
    public static Vector getCrossProduct(Vector v1, Vector v2) {
        //просто формула векторного произведени€
        return new Vector(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
    }

    public static Vector getSum(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public static Vector getDiff(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static Vector getProduct(Vector v, double value) {
        return new Vector(v.x * value, v.y * value, v.z * value);
    }

    //нормаль плоскости перпендикул€рна двум еЄ Єбрам
    public static Vector getPlaneNormal(Vector a, Vector b, Vector c) {
        return getCrossProduct(getDiff(a, b), getDiff(a, c));
    }

    public Vector getInvert() {
        return new Vector(-x, -y, -z);
    }

    public Vector getNormalized() {
        double len = len();
        return new Vector(x / len, y / len, z / len);
    }
}
