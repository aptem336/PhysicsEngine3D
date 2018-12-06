package org.yourorghere;

import java.awt.Color;
import javax.media.opengl.GL;
import static org.yourorghere.FF.gl;

public class Build {

    public static void buildTriangle(Vector[] vertex, Vector planeNormal, Color color) {
        gl.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        //устанавливаем нормаль для освещения
        gl.glNormal3d(planeNormal.x, planeNormal.y, planeNormal.z);
        gl.glBegin(GL.GL_TRIANGLES);
        //задаём точки
        gl.glVertex3d(vertex[0].x, vertex[0].y, vertex[0].z);
        gl.glVertex3d(vertex[1].x, vertex[1].y, vertex[1].z);
        gl.glVertex3d(vertex[2].x, vertex[2].y, vertex[2].z);
        gl.glEnd();
    }

    public static void buildLine(Vector[] vertex, Color color) {
        gl.glPushMatrix();
        gl.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        gl.glBegin(GL.GL_LINES);
        //задаём точки
        gl.glVertex3d(vertex[0].x, vertex[0].y, vertex[0].z);
        gl.glVertex3d(vertex[1].x, vertex[1].y, vertex[1].z);
        gl.glEnd();
        gl.glPopMatrix();
    }
}
