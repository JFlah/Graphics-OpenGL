//
// Lighting demo
//

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.gl2.*;
import com.jogamp.opengl.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;

class Lighting implements GLEventListener, ActionListener, ChangeListener {

    int ww=1000,wh=600;
    private JSlider ambientSlider, diffuseSlider,
            specularSlider, shininessSlider, divisionsSlider;
    private JRadioButton light0Button, light1Button;
    private JLabel divisionsLabel;

    int subdivisions = 10;
    boolean wire = false;
    boolean light0On = true;
    boolean light1On = true;

    // gl and glu are used to interface with OpenGL
    GL2 gl;
    GLU glu;
    GLUT glut;

    private float lightAmbient[] = {.3f, .3f, .3f, 1f};
    private float lightDiffuse[] = {.7f, .7f, .7f, .7f};
    private float lightSpecular[] = {1f, 1f, 1f, 1f};
    private float light0Position[] = {20f, 0f, -5f, 1f};
    private float light1Position[] = {0f, 10f, 10f, 1f};
    private float lightOff[] = {0,0,0,1};

    private float backAmbient[] = {0f, 0.3f, 0.3f};
    private float backDiffuse[] = {0f, 0.9f, 0.9f};
    private float torusAmbient[] = {0.3f, 0, 0};
    private float torusDiffuse[] = {0.9f, 0, 0};
    private float sphere1Ambient[] = {0, 0.3f, 0};
    private float sphere1Diffuse[] = {0, 0.9f, 0};
    private float sphere2Ambient[] = {0, 0, 0.3f};
    private float sphere2Diffuse[] = {0, 0, 0.9f};

    private float materialSpecular[] = {0.9f, 0.9f, 0.9f};
    private int materialShininess = 32;

    // GLEventListener methods:  init, reshape, display, dispose
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();
        gl.glClearColor(.8f, .8f, 1, 1); // set clear color to pale yellow

        gl.glShadeModel(GL2.GL_SMOOTH);
        float dir [] = {-1, 0, 0};
        float pos[] = {2, 0, 0};
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0,  GL2.GL_POSITION, light0Position, 0);
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glLightfv(GL2.GL_LIGHT1,  GL2.GL_POSITION, light1Position, 0);
    }

    public void reshape(GLAutoDrawable drawable,int x,int y, int width, int height){
        ww = width; wh = height;
        float aspect = ww/(float)wh;
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0,10*aspect,0,10,-10f,20f);
    }

    public void display(GLAutoDrawable drawable){
        render();
    }

    public void dispose(GLAutoDrawable drawable){
    }

    private void render(){
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

        for(int light=GL2.GL_LIGHT0; light<=GL2.GL_LIGHT1; light++){
            gl.glLightfv(light,  GL2.GL_AMBIENT, lightAmbient, 0);
            gl.glLightfv(light,  GL2.GL_DIFFUSE, lightDiffuse, 0);
            gl.glLightfv(light,  GL2.GL_SPECULAR, lightSpecular, 0);
        }

        // set up the view transformation
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(0,3,8,0,0,0,0,1,0);

        // draw the background
        gl.glPushMatrix();
        gl.glTranslatef(0,0,-5);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, backAmbient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, backDiffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, materialShininess);
        gl.glNormal3f(0,0,1);
        gl.glRecti(0,-5,25,10);
        gl.glPopMatrix();

        // draw a sphere
        gl.glPushMatrix();
        gl.glTranslatef(5,7,0);
        gl.glRotatef(90,1,0,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, sphere1Ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, sphere1Diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, materialShininess);
        if(wire){
            glut.glutWireSphere(2.5,subdivisions,subdivisions);
        }
        else {
            glut.glutSolidSphere(2.5,subdivisions,subdivisions);
        }
        gl.glPopMatrix();

        // draw another sphere
        gl.glPushMatrix();
        gl.glTranslatef(12,7,0);
        gl.glRotatef(90,1,0,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, sphere2Ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, sphere2Diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, materialShininess);
        if(wire){
            glut.glutWireSphere(2.8,subdivisions,subdivisions);
        }
        else {
            glut.glutSolidSphere(2.8,subdivisions,subdivisions);
        }
        gl.glPopMatrix();

        // draw a torus
        gl.glPushMatrix();
        gl.glTranslatef(10,2,0);
        gl.glRotatef(90,1,0,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, torusAmbient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, torusDiffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, materialShininess);
        if(wire){
            glut.glutWireTorus(1,2,subdivisions,subdivisions);
        }
        else {
            glut.glutSolidTorus(1,2,subdivisions,subdivisions);
        }
        gl.glPopMatrix();
    }

    static public void main(String[] args){
        new Lighting();
    }

    Lighting(){
        JFrame frame = new JFrame("Lighting Demo");
        GLProfile glp=GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        frame.add(canvas);
        frame.setSize(ww,wh);
        frame.setLocation(50,50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        JPanel row5 = new JPanel(new BorderLayout());
        divisionsSlider = newSlider(row5, 10, 100, 20, "Divisions");
        divisionsLabel = new JLabel(""+subdivisions);
        divisionsSlider.setValue(subdivisions);
        row5.add(divisionsLabel, BorderLayout.EAST);
        sliderFrame.add(row5);

        frame.setVisible(true);
        sliderFrame.setVisible(true);

        canvas.addGLEventListener(this);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == light0Button)
            light0On = !light0On;
        else if (event.getSource() == light1Button) {
            light1On = !light1On;
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
        else if (e.getSource() == divisionsSlider) {
            subdivisions = divisionsSlider.getValue();
            divisionsLabel.setText(String.format( "%5.0f     ", new Float(subdivisions)));
        }
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
}
