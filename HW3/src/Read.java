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
                    case "f":
                        int[] faces = new int[tokens.length-1];
                        for (int i = 1; i < tokens.length; i++) {
                            String vS = tokens[i];
                            String v = vS.split("/+")[0];
                            faces[i - 1] = Integer.parseInt(v);
                        }
                        object.newFace(faces);
                        break;
                }
            }
        }
        return object;
    }
}
