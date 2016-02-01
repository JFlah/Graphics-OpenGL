//
// Draw a Square.
//
// Draws a red square on a white background.
//

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class HW1 implements GLEventListener {

    int ww=500,wh=500;
    int vx=ww/2, vy=wh/2;   // center vertex
    int r = 150;        // radius of circle being made
    double red=0, green=0.1, blue=0; // triangle color

    double theta = 0;   // initial theta angle
    int triangles = 8; // how many triangles it is made of
    double incr = 0.15; // increment of color throughout circle
    double thetaIncr = 0.01; // for rotation manipulation
    boolean rotating = true;

    Random rand = new Random(); // for moving

    JCheckBox reverseBox;
    JCheckBox bounceBox;

    // gl and glu are used to interface with OpenGL
    GL2 gl;
    GLU glu;

    // GLEventListener methods:  init, reshape, display, dispose
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glClearColor(1, 1, 1, 1); // set clear color to white
    }

    public void reshape(GLAutoDrawable drawable,int x,int y, int width, int height){

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0,width,height,0);
    }

    public void display(GLAutoDrawable drawable){
        render();
    }

    public void dispose(GLAutoDrawable drawable){
    }

    public void render() {
        green = 0.1;    // initial green value

        // check for moving around
        if (bounceBox.isSelected()) {
            int dx = rand.nextInt((6+6)+1) - 6;
            int dy = rand.nextInt((6+6)+1) - 6;

            //System.out.println(vx);
            //System.out.println(vy);

            if (vx  > ww-r) {
                if (dx > 0) {
                    dx *= -1;
                }
            }
            if (vx < r) {
                if (dx < 0) {
                    dx *= -1;
                }
            }
            if (vy  > wh-r-(r/2)) {
                if (dy > 0) {
                    dy *= -1;
                }
            }
            if (vy < r) {
                if (dy < 0) {
                    dy *= -1;
                }
            }

            //System.out.println("vx" + vx);
            //System.out.println("vy" + vy);
            vx += dx;
            vy += dy;
        }

        // check for reversal
        if (!reverseBox.isSelected()) {
            theta += thetaIncr; // incrementing for part 2 rotation
        } else {
            theta -= thetaIncr;
        }

        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // clear the canvas

        // draw the triangle
        gl.glBegin(GL2.GL_TRIANGLES);

        for (int i = 0; i <= triangles-1; i++) {
            // change incrementation to keep up with an increase in
            // the number of triangles circle is made of
            // ONLY WORKS TO 61 TRIANGLES, then starts repeating
            // this can be adjusted if need be, just add more ifs
            if (triangles <= 13) {
                incr = 0.15;
            }
            if (triangles > 13) {
                incr = 0.1;
            }
            if (triangles > 19) {
                incr = 0.05;
            }
            if (triangles > 37) {
                incr = 0.03;
            }
            // determine shade of green
            if (i < triangles/2) {  // if not more than halfway thru circle, lighten
                green += incr;
            } else {                // darken once past halfway through
                green -= incr;
            }

            // do actual drawing
            gl.glColor3d(red, green, blue);
            gl.glVertex2f(vx, vy);
            gl.glVertex2d(vx + r * Math.cos(theta), vy + r * Math.sin(theta));
            theta += 2 * Math.PI / triangles;
            gl.glVertex2d(vx + r * Math.cos(theta), vy + r * Math.sin(theta));
        }

        gl.glEnd();
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyChar()) {
            case '+':   // make it bigger
                r+=10;
                break;
            case '-':   // make it smaller
                r-=10;
                break;
            case 'T':   // increase num of triangles
                triangles++;
                break;
            case 't':   // decrease num of triangles
                if (triangles < 1) {
                    break;
                }
                triangles--;
                break;
            case 'r':   // make it red
                red = 1;
                green = 0;
                blue = 0;
                break;
            case 'b':   // make it blue
                red = 0;
                green = 0;
                blue = 0.8;
                break;
            case 'g':    // make it green again
                red = 0;
                green = 0.1;
                blue = 0;
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (rotating) {
                thetaIncr = 0;
            } else {
                thetaIncr = 0.01;
            }
            rotating = !rotating;
        }
    }

    public static void main(String[] args){
        new HW1();
    }

    HW1(){
        JFrame frame = new JFrame("Draw Some Triangles");
        GLProfile glp=GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        frame.add(canvas);
        frame.setSize(ww,wh);
        frame.setLocation(50,50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topRow = new JPanel( new FlowLayout(FlowLayout.LEADING));
        JPanel north = new JPanel( new BorderLayout());
        JPanel northWest = new JPanel( new BorderLayout());

        reverseBox = new JCheckBox("Reverse");
        reverseBox.setSelected(false);
        bounceBox = new JCheckBox("Jump Around");
        bounceBox.setSelected(false);

        topRow.add(reverseBox);
        topRow.add(bounceBox);
        northWest.add(topRow, BorderLayout.NORTH);
        north.add(northWest, BorderLayout.WEST);

        JPanel center = new JPanel(new GridLayout(1,1));
        center.add(canvas);

        frame.add(north,  BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);

        frame.setVisible(true);

        canvas.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                HW1.this.keyPressed(e);
            }
        });

        canvas.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                HW1.this.mouseClicked(e);
            }
        });

        canvas.addGLEventListener(this);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

}