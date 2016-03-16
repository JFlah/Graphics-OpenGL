import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Jack on 3/8/2016.
 */
public class Draw implements GLEventListener {
    // TODO: update to point at folder with .obj files
    public static final String PATH = "src/";

    private GLCanvas canvas;
    private GL2 gl;
    private GLU glu;

    private Obj obj1, obj2;

    private float theta;

    public static final float colorVals[][] = new float[][] {
            {1f,0f,0f}, {0f, 1f, 0f}, {0f, 0f, 1f},
            {0f, 1f, 1f}, {1f, 0f, 1f}, {1f, 1f,0f},
    };

    public Draw() {
        try {
            Read reader = new Read();
            File file1 = new File(PATH+"lamp.obj");
            File file2 = new File(PATH+"teapot.obj");
            obj1 = reader.readFile(file1);
            obj2 = reader.readFile(file2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        caps.setDoubleBuffered(false);
        this.canvas = new GLCanvas(caps);
        JFrame frame = new JFrame("Draw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocation(50, 50);
        frame.setLayout(new BorderLayout());
        frame.add(canvas);
        frame.setVisible(true);
        this.canvas.addGLEventListener(this);
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glShadeModel(GL2.GL_FLAT);
        gl.glClearColor(.9f, .9f, .9f, 1f);
        glu.gluLookAt(0, 0, 0, 0, 0, -1, 0, 1, 0);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        render();
    }

    public void render() {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glBegin(GL2.GL_QUADS);

        // drawing checkered floor
        for (int i = -40; i < 40; i++) {
            for (int k = -40; k < 40; k++) {
                if ((i + k) % 2 == 0) gl.glColor3f(1, 1, 1);
                else gl.glColor3f(0, 0, 0);
                gl.glVertex3f(i, -1, k);
                gl.glVertex3f(i + 1, -1, k);
                gl.glVertex3f(i + 1, -1, k + 1);
                gl.glVertex3f(i, -1, k + 1);
            }
        }
        gl.glEnd();

        for (int i = 0; i < 4; i++) { // x
            for (int j = 0; j < 3; j++) { // y
                for (int k = -10; k < 10; k++) { // z
                    Obj obj = Math.abs(k+j)%2==0 ? obj1 : obj2; // randomize teapot or lamp
                    gl.glPushMatrix();
                    gl.glTranslatef(4*i - 5f, 4*j, 4*k);
                    float scale = 1/(obj.xMax-obj.xMin); // for vertex size disparities
                    gl.glScalef(scale, scale, scale);
                    gl.glRotatef(j==0 ? 0 : theta, 0, 1, 0); // rotate by theta around y axis
                    gl.glColor3fv(colorVals[i], 0);
                    drawObj(obj);
                    gl.glPopMatrix();
                }
            }
        }
        theta+=5;
    }

    private void drawObj(Obj obj) {
        gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
        gl.glPolygonOffset(1.0f, 1.0f);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        // draw the object here
        obj.draw(gl);

        gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);

        gl.glColor3f(0, 0, 0);  // use black for the wire frame
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        // draw the object again
        obj.draw(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        float aspect = w / (float) h;
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(30, aspect, 1, 100);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public static void main(String[] args) {
        Draw draw = new Draw();
    }
}
