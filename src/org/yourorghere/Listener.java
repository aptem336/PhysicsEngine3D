package org.yourorghere;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.media.opengl.GLAutoDrawable;
import static org.yourorghere.FF.angleH;
import static org.yourorghere.FF.angleV;

public class Listener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    public static Vector diff = new Vector();
    public static double force = 1000d;

    private void reset() {
        Solver.BALL.velocity.set(Vector.NULL);
        Solver.BALL.location.set(Vector.NULL);
        Solver.move = false;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            reset();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        force -= e.getPreciseWheelRotation() * 50;
        force = Math.min(2000d, force);
        force = Math.max(0d, force);
        diff.z = force;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Solver.BALL.toss(diff);
        Solver.move = true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            angleV -= 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            angleV += 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            angleH += 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            angleH -= 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            Solver.E = Math.min(Solver.E + 0.125d, 1d);
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            Solver.E = Math.max(Solver.E - 0.125d, 0d);
        }
        if (e.getKeyCode() == KeyEvent.VK_T) {
            Solver.F = Math.min(Solver.F + 0.125d, 1d);
        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
            Solver.F = Math.max(Solver.F - 0.125d, 0d);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Solver.move = !Solver.move;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        reset();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void init(GLAutoDrawable drawable) {
    }

    public void display(GLAutoDrawable drawable) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //разница между текущим положением и центром экрана = импульс броска
        diff = Vector.getDiff(new Vector(e.getX(), e.getY(), force), new Vector(400d, 400d, 0d));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
