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

    public List<float[]> vertNormals;
    public List<int[]> faceNormals;

    public Obj() {
        this.faces = new ArrayList<>();
        this.vertices = new ArrayList<>();
        this.vertNormals = new ArrayList<>();
        this.faceNormals = new ArrayList<>();
    }

    public void newFace(int[] theFaces) {
        this.faces.add(theFaces);
    }

    public void addVertNormal3f(float xn, float yn, float zn){
        this.vertNormals.add(new float[]{xn,yn,zn});
    }

    public void addFaceNormal(int[] n){
        this.faceNormals.add(n);
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

    public void draw(GL2 gl){
        if (this.faces.get(0).length==4) gl.glBegin(GL2.GL_QUADS);
        else gl.glBegin(GL2.GL_TRIANGLES);
        for(int i=0; i<this.faces.size();i++){
            int[] face = this.faces.get(i);
            int[] norm = null;
            if (!faceNormals.isEmpty()) norm=this.faceNormals.get(i);
            for (int j = 0; j < face.length; j++) {
                int vecNum = face[j]-1;
                if (!vertNormals.isEmpty()){
                    float[] vecNorm = this.vertNormals.get((norm==null) ? vecNum : norm[j]-1);
                    gl.glNormal3fv(vecNorm,0);
                }
                gl.glVertex3fv(this.vertices.get(vecNum), 0);
            }
        }
        gl.glEnd();
    }
}
