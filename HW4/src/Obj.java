import com.jogamp.opengl.GL2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 3/8/2016.
 */
public class Obj {
    public List<int[]> faces;
    public List<float[]> vertices;
    public float xMin, xMax;
    public float yMin, yMax;
    public float zMin, zMax;

    public Obj() {
        this.faces = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }

    public void newFace(int[] theFaces) {
        this.faces.add(theFaces);
    }

    public void newVector(float[] theVectors) {
        float x = theVectors[0];
        float y = theVectors[1];
        float z = theVectors[2];
        if (x > xMax) xMax = x;
        else if (x < xMin) xMin = x;
        if (y > yMax) yMax = y;
        else if (y < yMin) yMin = y;
        if (z > zMax) zMax = z;
        else if (z < zMin) zMin = z;
        this.vertices.add(theVectors);
    }

    public void draw(GL2 gl) {
        gl.glBegin(GL2.GL_POLYGON);
        for (int[] face : faces) {
            for (int i = 0; i < face.length; i++) {
                gl.glVertex3fv(this.vertices.get(face[i]-1), 0);
            }
        }
        gl.glEnd();
    }
}
