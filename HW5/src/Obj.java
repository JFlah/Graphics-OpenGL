import com.jogamp.opengl.GL2;

import java.util.ArrayList;
import java.util.Arrays;
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
        for(int i=0; i<this.faces.size();i++){
            int[] face = this.faces.get(i);
            int glDrawFunc = (face.length==4) ? GL2.GL_QUADS : (face.length==3) ? GL2.GL_TRIANGLES : GL2.GL_POLYGON;
            gl.glBegin(glDrawFunc);
            for (int j = 0; j < face.length; j++) {
                int vertNum = face[j]-1;
                float[] vertNorm = vertNormals.get(vertNum);
                gl.glNormal3fv(vertNorm,0);
                gl.glVertex3fv(this.vertices.get(vertNum), 0);
            }
            gl.glEnd();
        }
    }

    void vectorNormals(){
        List<List<float[]>> vertNormals = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            vertNormals.add(new ArrayList<>());
        }

        for(int i=0; i<this.faces.size();i++) {
            int[] face = this.faces.get(i);

            float[] vecNorm = normal(vertices.get(face[0]-1), vertices.get(face[1]-1), vertices.get(face[2]-1));
            for (int j = 0; j < face.length; j++) {
                int vecNum = face[j]-1;
                vertNormals.get(vecNum).add(vecNorm);
            }

        }
        List<float[]> avgVectorNormals = new ArrayList<>();
        for (int i = 0; i < vertNormals.size(); i++) {
            List<float[]> vecs = vertNormals.get(i);
            int divisor = vecs.size();
            float[] vec = new float[3];
            for (float[] v : vecs){
                vec[0]+=v[0];
                vec[1]+=v[1];
                vec[2]+=v[2];
            }
            vec[0]/=divisor;
            vec[1]/=divisor;
            vec[2]/=divisor;
            avgVectorNormals.add(vec);
        }
        this.vertNormals = avgVectorNormals;
    }

    public static float[] normal(float[] p1, float[]p2, float[] p3){
        float[] v1 = new float[]{p1[0] - p2[0], p1[1]- p2[1], p1[2] - p2[2]};
        float[] v2 = new float[]{p1[0] - p3[0], p1[1]- p3[1], p1[2] - p3[2]};
        float i = v1[1]*v2[2] - v2[1]*v1[2];
        float j = -1 * (v1[0]*v2[2] - v2[0]*v1[2]);
        float k = v1[0] * v2[1] - v2[0] * v1[1];
        float[] nv = new float[]{i,j,k};
        float len = (float)Math.sqrt(nv[0]*nv[0] + nv[1]*nv[1] + 
                nv[2]*nv[2]);
        return new float[]{nv[0]/len, nv[1]/len, nv[2]/len};
    }
}