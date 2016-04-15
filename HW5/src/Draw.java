import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Jack on 3/8/2016.
 */
public class Draw implements GLEventListener, ActionListener, ChangeListener, KeyListener {
    // TODO: update to point at folder with .obj files
    public static final String PATH = "src/";


    private JSlider ambientSlider, diffuseSlider,
            specularSlider, shininessSlider;//, divisionsSlider;
    private JRadioButton light0Button, light1Button, smoothButton;
    //private JLabel divisionsLabel;

    int subdivisions = 10;
    boolean light0On = true;
    boolean light1On = true;
    boolean smooth = true;

    public float shuttleRotation;


    // gl and glu are used to interface with OpenGL
    GL2 gl;
    GLU glu;

    private float lightAmbient[] = {.3f, .3f, .3f, 1f};
    private float lightDiffuse[] = {.7f, .7f, .7f, .7f};
    private float lightSpecular[] = {1f, 1f, 1f, 1f};
    private float light0Position[] = {0f, 7f, 5f, 1f};
    private float light1Position[] = {-5f, 10f, 10f, 1f};

    private float floorBlack[] = {0f, 0f, 0f};
    private float floorWhite[] = {1f, 1f, 1f};
    private float sphere1Ambient[] = {0, 0.3f, 0};
    private float sphere1Diffuse[] = {0, 0.9f, 0};

    private float backAmbient[] = {0f, 0.3f, 0.3f};
    private float backDiffuse[] = {0f, 0.9f, 0.9f};

    private float phi, rotation, shuttleX,shuttleY;

    private float materialSpecular[] = {0.9f, 0.9f, 0.9f};
    private int materialShininess = 32;

    private GLCanvas canvas;

    private Obj obj1, obj2;

    private float theta;
    private float time;

    public Draw() {
        this.theta = 0;
        this.phi = (float) Math.PI/2;
        this.shuttleY = 1f;
        this.shuttleX=3f;

        try {
            Read reader = new Read();
            File file1 = new File(PATH+"skyscraper.obj");
            File file2 = new File(PATH+"shuttle.obj");
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
        this.canvas.addKeyListener(this);

        JFrame sliderFrame = new JFrame("Modify Attributes");
        sliderFrame.setLayout(new GridLayout(6,1));
        sliderFrame.setSize(300, 600);
        sliderFrame.setAlwaysOnTop(true);
        sliderFrame.setLocation(1000, 50);

        JPanel row0 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        light0Button = new JRadioButton("Light 0",true);
        light0Button.addActionListener(this);
        row0.add(light0Button);
        light1Button = new JRadioButton("Light 1",true);
        light1Button.addActionListener(this);
        row0.add(light1Button);
        smoothButton = new JRadioButton("Smooth",true);
        smoothButton.addActionListener(this);
        row0.add(smoothButton);
        sliderFrame.add(row0);

        JPanel row1 = new JPanel(new BorderLayout());
        ambientSlider = newSlider(row1, 0, 100, 25, "Ambient");
        ambientSlider.setValue(50);
        sliderFrame.add(row1);

        JPanel row2 = new JPanel(new BorderLayout());
        diffuseSlider = newSlider(row2, 0, 100, 25, "Diffuse");
        diffuseSlider.setValue(60);
        sliderFrame.add(row2);

        JPanel row3 = new JPanel(new BorderLayout());
        specularSlider = newSlider(row3, 0, 100, 25, "Specular");
        specularSlider.setValue(50);
        sliderFrame.add(row3);

        JPanel row4 = new JPanel(new BorderLayout());
        shininessSlider = newSlider(row4, 0, 100, 25, "Shininess");
        shininessSlider.setValue(30);
        sliderFrame.add(row4);

        frame.setVisible(true);
        sliderFrame.setVisible(true);

        FPSAnimator animator = new FPSAnimator(canvas, 60);

        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glClearColor(.9f, .9f, .9f, 1f);
        glu.gluLookAt(0, 0, 0, 0, 0, -1, 0, 1, 0);

        gl.glShadeModel(GL2.GL_SMOOTH);
        float dir [] = {-1, 0, 0};
        float pos[] = {2, 0, 0};
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0,  GL2.GL_POSITION, light0Position, 0);
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glLightfv(GL2.GL_LIGHT1,  GL2.GL_POSITION, light1Position, 0);

        gl.glEnable(GL2.GL_NORMALIZE);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        render();
    }

    public void render() {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        // turn the lights on or off
        if(light0On)
            gl.glEnable(GL2.GL_LIGHT0);
        else
            gl.glDisable(GL2.GL_LIGHT0);
        if(light1On)
            gl.glEnable(GL2.GL_LIGHT1);
        else
            gl.glDisable(GL2.GL_LIGHT1);
        if (smooth) gl.glShadeModel(GL2.GL_SMOOTH);
        else gl.glShadeModel(GL2.GL_FLAT);

        for(int light=GL2.GL_LIGHT0; light<=GL2.GL_LIGHT1; light++){
            gl.glLightfv(light,  GL2.GL_AMBIENT, lightAmbient, 0);
            gl.glLightfv(light,  GL2.GL_DIFFUSE, lightDiffuse, 0);
            gl.glLightfv(light,  GL2.GL_SPECULAR, lightSpecular, 0);
        }

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        float xDir = (float) (Math.cos(theta) * Math.cos(phi));
        float yDir = (float) Math.sin(theta);
        float zDir = -1 * (float) (Math.cos(theta) * Math.sin(phi));
        glu.gluLookAt(0, 2, 0, xDir, yDir + 2, zDir, 0, 1, 0); // view transformation

        gl.glPushMatrix();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glBegin(GL2.GL_QUADS);

        gl.glNormal3f(0,1,0);

        // drawing checkered floor
        for (int i = -40; i < 40; i++) {
            for (int k = -40; k < 40; k++) {

                if ((i + k) % 2 == 0) {
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, floorWhite, 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, floorWhite, 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, floorWhite, 0);
                }
                else {
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, floorBlack, 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, floorBlack, 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, floorBlack, 0);
                }
                gl.glVertex3f(i, -1, k);
                gl.glVertex3f(i + 1, -1, k);
                gl.glVertex3f(i + 1, -1, k + 1);
                gl.glVertex3f(i, -1, k + 1);
            }
        }
        gl.glEnd();
        gl.glPopMatrix();

        drawRocket(obj2);
        drawSkyscraper(obj1);
        time+=0.01;

    }

    private void drawRocket(Obj obj){
        gl.glPushMatrix();
        float scale = 5/(obj.xMax-obj.xMin); // for vertex size disparities

        if (shuttleRotation < 90){
            gl.glTranslatef(shuttleX, shuttleY, -15); // (0,.5,-15)
            shuttleRotation+=0.5;
            shuttleX-=0.01666666;
        } else if (time<6.5f) { // takeoff
            float t = time - 1.7999986f;
            if (t > 1)
                theta = (t-1)*1.3f/(float)Math.PI;
            t = t*t*t;
            gl.glTranslatef(shuttleX, t+shuttleY, -15);
        }
        gl.glRotatef(-90, 1, 0, 0); // rotate by theta around y axis
        gl.glRotatef(shuttleRotation,0,1,0);
        gl.glScalef(scale, scale, scale);

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, sphere1Ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, sphere1Diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, materialShininess);

        drawObj(obj);
        gl.glPopMatrix();
    }

    private void drawSkyscraper(Obj obj){
        gl.glPushMatrix();
        float scale = 50/(obj.xMax-obj.xMin); // for vertex size disparities

        gl.glTranslatef(-2, -1, -15);
        gl.glScalef(scale/15, scale, scale/10);

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, sphere1Ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, sphere1Diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, materialShininess);

        drawObj(obj);
        gl.glPopMatrix();
    }

    private void drawObj(Obj obj) {
        gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
        gl.glPolygonOffset(1.0f, 1.0f);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        // draw the object here
        obj.draw(gl);

        gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);

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

    // This assumes the parent is a panel using BorderLayout.
    private JSlider newSlider(JPanel parent, int min, int max, int step, String label) {
        JSlider slider = new JSlider(min, max);
        slider.setMajorTickSpacing(step);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
        JLabel name = new JLabel(label);
        parent.add(name, BorderLayout.WEST);
        parent.add(slider, BorderLayout.CENTER);
        return slider;
    }

    public static void main(String[] args) {
        Draw draw = new Draw();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == light0Button)
            light0On = !light0On;
        else if (event.getSource() == light1Button) {
            light1On = !light1On;
        } else if (event.getSource() == smoothButton){
            smooth = !smooth;
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == ambientSlider) {
            lightAmbient[0]=lightAmbient[1]=lightAmbient[2]=
                    ambientSlider.getValue()/100.0f;
        }
        if (e.getSource() == diffuseSlider) {
            lightDiffuse[0]=lightDiffuse[1]=lightDiffuse[2]=
                    diffuseSlider.getValue()/100.0f;
        }
        if (e.getSource() == specularSlider) {
            lightSpecular[0]=lightSpecular[1]=lightSpecular[2]=
                    specularSlider.getValue()/100.0f;
        }
        if (e.getSource() == shininessSlider) {
            materialShininess=shininessSlider.getValue();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        switch (key) {
            case 'a':
                this.phi += 0.1;
                break;
            case 'd':
                this.phi -= 0.1;
                break;
            case 'w':
                this.theta += 0.1;
                break;
            case 's':
                this.theta -= 0.1;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
