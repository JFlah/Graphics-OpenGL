import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Jack on 3/8/2016.
 */
public class Read {
    private Scanner sc;
    String line;

    public Obj readFile(File file) throws FileNotFoundException {
        sc = new Scanner(file);
        Obj object = new Obj();
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] tokens = line.split("\\s+");
            if (tokens.length > 0) {
                switch (tokens[0]) {
                    case "v":
                        float x = Float.parseFloat(tokens[1]);
                        float y = Float.parseFloat(tokens[2]);
                        float z = Float.parseFloat(tokens[3]);
                        float[] vec = { x, y, z };
                        object.newVector(vec);
                        break;
                    case "vn":
                        float xn = Float.parseFloat(tokens[1]);
                        float yn = Float.parseFloat(tokens[2]);
                        float zn = Float.parseFloat(tokens[3]);
                        object.addVertNormal3f(xn, yn, zn);
                        break;
                    case "f":
                        int[] faceVerts = new int[tokens.length-1];
                        int[] faceNorms = null;
                        for (int i = 1; i < tokens.length; i++) {
                            String[] faceToks = tokens[i].split("/");
                            String vertStr = faceToks[0];
                            faceVerts[i-1] = Integer.parseInt(vertStr);
                            if (faceToks.length==3) {
                                if (faceNorms==null) faceNorms = new int[tokens.length-1];
                                String normStr = faceToks[2];
                                faceNorms[i-1]=Integer.parseInt(normStr);
                            }
                        }
                        object.newFace(faceVerts);
                        if (faceNorms!=null) object.addFaceNormal(faceVerts);
                        break;
                }
            }
        }
        return object;
    }
}
