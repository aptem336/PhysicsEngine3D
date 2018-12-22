package org.yourorghere;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class FF implements GLEventListener {

    public static void main(String[] args) {
        //создаЄм окно
        Frame frame = new Frame("Body collision simulation");
        //получаем размеры экрана
        int size = Toolkit.getDefaultToolkit().getScreenSize().height - 30;
        frame.setSize(size, size);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        //создаЄм канву - место рисовани€
        GLCanvas canvas = new GLCanvas();
        //создаЄм и добавл€ем слушателей
        Listener listener = new Listener();
        canvas.addKeyListener(listener);
        canvas.addMouseListener(listener);
        canvas.addMouseMotionListener(listener);
        canvas.addMouseWheelListener(listener);
        canvas.addGLEventListener(new FF());
        canvas.setBounds(0, 0, frame.getWidth(), frame.getHeight() - 30);
        //создаЄм управл€ющий методами отрисовки аниматор
        Animator animator = new Animator(canvas);

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

    public static GL gl;
    private static GLU glu;
    public static GLUT glut;

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();
        //включаем свет
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        //настраиваем материал
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glEnable(GL.GL_NORMALIZE);
        //настравиваем цвет
        gl.glAlphaFunc(GL.GL_GREATER, 0);
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_ALPHA_TEST);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        //включаем тест глубины
        gl.glEnable(GL.GL_DEPTH_TEST);
        //очищаем буфер тЄмно серым цветом
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
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

    //рассто€ние от камеры до объекта
    public static double len = 2500d;
    //вертикальный и горизонатльный углы в сфере по которой перемещаетс€ камера
    public static double angleV = 45d, angleH = 270d;
    //положение камеры в этой сфере
    private static double xView, zView, yView;

    private static void calcCam() {
        //вычисл€ем компоненты через вертикальный и горизонтальный углы
        xView = len * Math.sin(Math.toRadians(angleV)) * Math.cos(Math.toRadians(angleH));
        zView = 1024d + len * Math.sin(Math.toRadians(angleV)) * Math.sin(Math.toRadians(angleH));
        yView = -512d + len * Math.cos(Math.toRadians(angleV));
    }

    //переменные дл€ подсчЄта фпс
    private static long time;
    private static long lc_time;
    private static int frames = 60;
    public static int fps;

    @Override
    public void display(GLAutoDrawable drawable) {
        //очищаем буферы цвета и глубины
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        //устанавливаем положение источника освещени€
        gl.glLightiv(GL.GL_LIGHT0, GL.GL_POSITION, new int[]{1, 1, 1, 0}, 0);
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
        //вызываем метод вывода надписей
        drawText(drawable);
        gl.glFlush();
    }

    private static void drawText(GLAutoDrawable drawable) {
        gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glWindowPos2i(10, drawable.getHeight() - 45);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, fps + "   fps");

        gl.glWindowPos2i(10, drawable.getHeight() - 65);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, Solver.E + "   elasticity");

        gl.glWindowPos2i(10, drawable.getHeight() - 85);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, Solver.F + "   friction");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 45);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Esc - reset");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 65);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "W, S, A, D - rotate camera");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 85);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "R/T - +/- elasticity");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 105);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "F/G - +/- friction");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 125);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Space - pause");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 145);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Mouse wheel - add ball");
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
