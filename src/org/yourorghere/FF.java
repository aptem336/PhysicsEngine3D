package org.yourorghere;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class FF implements GLEventListener {

    public static GL gl;
    private static GLU glu;
    public static GLUT glut;

    private static long time;
    private static long lc_time;
    private static int frames = 60;
    private static int fps;

    private static Listener listener;
    private static GLCanvas canvas;
    public static Animator animator;

    public static double len = 2500d;
    public static double angleV = 45d, angleH = 270d;
    private static double xView, zView, yView;

    public static void main(String[] args) {
        //создаЄм окно
        Frame frame = new Frame("Free fall simulation");
        frame.setType(Window.Type.UTILITY);
        //получаем размеры экрана
        int size = Toolkit.getDefaultToolkit().getScreenSize().height - 30;
        frame.setSize(size, size);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        //создаЄм канву - место рисовани€
        canvas = new GLCanvas();
        //создаЄм и добавл€ем слушателей
        listener = new Listener();
        canvas.addKeyListener(listener);
        canvas.addMouseListener(listener);
        canvas.addMouseMotionListener(listener);
        canvas.addMouseWheelListener(listener);
        canvas.addGLEventListener(new FF());
        canvas.setBounds(0, 0, frame.getWidth(), frame.getHeight() - 30);
        //создаЄм управл€ющий методами отрисовки аниматор
        animator = new Animator(canvas);

        frame.add(canvas);
        //добавл€ем слушател€ окна
        frame.addWindowListener(new WindowAdapter() {
            @Override
            //переопредел€ем операцию закрыти€ окна
            public void windowClosing(WindowEvent e) {
                new Thread(() -> {
                    //останавливаем аниматор
                    animator.stop();
                    //выходим со статусом 0 (без ошибок)
                    System.exit(0);
                }).start();
            }
        });
        //запускаем аниматор
        animator.start();
        //загружаем местность
        Terrain.load("hm.raw", 2048, 64);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();

        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glLightiv(GL.GL_LIGHT0, GL.GL_POSITION, new int[]{1, 1, 1, 1}, 0);

        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glEnable(GL.GL_NORMALIZE);

        gl.glAlphaFunc(GL.GL_GREATER, 0);
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_ALPHA_TEST);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_DEPTH_TEST);

        //очищаем буфер тЄмно серым цветом
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 100000.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private static void calcCam() {
        //вычисл€ем компоненты через вертикальный и горизонтальный углы
        xView = len * Math.sin(Math.toRadians(angleV)) * Math.cos(Math.toRadians(angleH));
        zView = 1024d + len * Math.sin(Math.toRadians(angleV)) * Math.sin(Math.toRadians(angleH));
        yView = -512d + len * Math.cos(Math.toRadians(angleV));
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslated(0d, 0d, 0d);

        //вычисл€ем положение камеры
        calcCam();
        glu.gluLookAt(xView, yView, zView, 0d, -512d, 1024d, 0d, 0.5d, 0d);
        //запускаем управл€ющий разрешением коллизий метод
        Solver.step();
        //отрисовываем все треугольники
        for (Triangle triangle : Terrain.triangles) {
            Build.buildTriangle(triangle.vertex, triangle.planeNormal, Triangle.COLOR);
        }
        //блок дл€ подсчЄта fps
        //текущее врем€
        time = System.currentTimeMillis();
        //если прошло больше секунды
        if (time - lc_time >= 1000) {
            //значение fps равно насчитанным кадрам
            fps = frames;
            //сохран€ем врем€ дл€ вычислени€ разницы
            lc_time = time;
            //обнул€ем количество фреймов
            frames = 0;
        }
        //увеличиваем количество кадров
        frames++;

        gl.glFlush();
        //вызываем метод вывода надписей
        drawText(drawable);
    }

    private static void drawText(GLAutoDrawable drawable) {
        gl.glColor4f(1f, 0f, 0f, 1f);
        gl.glWindowPos2i(10, drawable.getHeight() - 40);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, fps + "   fps");

        gl.glColor4f(1f, 0f, 0f, 1f);
        gl.glWindowPos2i(10, drawable.getHeight() - 60);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, Listener.force + "   toss force");

        gl.glColor4f(1f, 0f, 0f, 1f);
        gl.glWindowPos2i(10, drawable.getHeight() - 80);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, Solver.E + "   elasticity");

        gl.glColor4f(1f, 0f, 0f, 1f);
        gl.glWindowPos2i(10, drawable.getHeight() - 100);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, Solver.F + "   friction");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 40);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Esc - reset");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 60);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "W, S, A, D - rotate camera");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 80);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "MouseWheel - +/- toss force");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 100);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "R/F - +/- elasticity");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 120);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "T/G - +/- friction");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 140);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Space - pause");
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
