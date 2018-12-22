package org.yourorghere;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.media.opengl.GLAutoDrawable;
import static org.yourorghere.BC.angleH;
import static org.yourorghere.BC.angleV;

public class Listener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Solver.reset = true;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Ball ball = new Ball(50.0d, 1.0d);
        ball.location.set(new Vector(Math.random() * 1548.0d - 774.0d, 0.0d, Math.random() * 1548.0d + 250.0d));
        Solver.BALLS.add(ball);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
            Solver.E = Math.max(Solver.E - 0.125d, 0d);
        }
        if (e.getKeyCode() == KeyEvent.VK_T) {
            Solver.E = Math.min(Solver.E + 0.125d, 1d);
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            Solver.F = Math.max(Solver.F - 0.125d, 0d);
        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
            Solver.F = Math.min(Solver.F + 0.125d, 1d);
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
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
