package Engine.BlockUtils;

import org.joml.Vector3f;
import org.lwjgl.cuda.CU;

public class CubeModel {
    public static final float CUBESIZE = 2f;
    
    
    public enum Faces {
        PX(new float[]{
                CUBESIZE / 2, CUBESIZE / 2, CUBESIZE / 2, //0
                CUBESIZE / 2, CUBESIZE / 2, -CUBESIZE / 2, //1
                CUBESIZE / 2, -CUBESIZE / 2, CUBESIZE / 2,// 2
                CUBESIZE / 2, -CUBESIZE / 2, -CUBESIZE / 2 //3
        }, Normalpx),
        NX(new float[]{
                -CUBESIZE / 2, CUBESIZE / 2, -CUBESIZE / 2,
                -CUBESIZE / 2, CUBESIZE / 2, CUBESIZE / 2,
                -CUBESIZE / 2, -CUBESIZE / 2, -CUBESIZE / 2,
                -CUBESIZE / 2, -CUBESIZE / 2, CUBESIZE / 2,
        }, Normalnx),
        PY(new float[]{
                -CUBESIZE / 2, CUBESIZE / 2, -CUBESIZE / 2,
                CUBESIZE / 2, CUBESIZE / 2, -CUBESIZE / 2,
                -CUBESIZE / 2, CUBESIZE / 2, CUBESIZE / 2,
                CUBESIZE / 2, CUBESIZE / 2, CUBESIZE / 2,
        }, Normalpy),
        NY(new float[]{
                -CUBESIZE / 2, -CUBESIZE / 2, -CUBESIZE / 2,
                CUBESIZE / 2, -CUBESIZE / 2, -CUBESIZE / 2,
                -CUBESIZE / 2, -CUBESIZE / 2, CUBESIZE / 2,
                CUBESIZE / 2, -CUBESIZE / 2, CUBESIZE / 2,
        }, Normalny),
        PZ(new float[]{
                -CUBESIZE / 2, CUBESIZE / 2, CUBESIZE / 2,
                CUBESIZE / 2, CUBESIZE / 2, CUBESIZE / 2,
                -CUBESIZE / 2, -CUBESIZE / 2, CUBESIZE / 2,
                CUBESIZE / 2, -CUBESIZE / 2, CUBESIZE / 2
        }, Normalpz),
        NZ(new float[]{
                -CUBESIZE / 2, CUBESIZE / 2, -CUBESIZE / 2,
                CUBESIZE / 2, CUBESIZE / 2, -CUBESIZE / 2,
                -CUBESIZE / 2, -CUBESIZE / 2, -CUBESIZE / 2,
                CUBESIZE / 2, -CUBESIZE / 2, -CUBESIZE / 2
        }, Normalnz);
        public final float[] positions;
        public final Vector3f normal;
        
        Faces(float[] x, Vector3f normal) {
            this.positions = x;
            this.normal = normal;
        }
        
    }
    
    public static final Vector3f Normalpx = new Vector3f(1.0f, 0.0f, 0.0f);
    public static final Vector3f Normalnx = new Vector3f(-1.0f, 0.0f, 0.0f);
    public static final Vector3f Normalpy = new Vector3f(0.0f, 1.0f, 0.0f);
    public static final Vector3f Normalny = new Vector3f(0.0f, -1.0f, 0.0f);
    public static final Vector3f Normalpz = new Vector3f(0.0f, 0.0f, 1.0f);
    public static final Vector3f Normalnz = new Vector3f(0.0f, 0.0f, -1.0f);
    
    public static float[] convertBlockPosition(Vector3f position, Faces face) {
        float[] positions;
        positions = face.positions.clone();
        for (int i = 0; i < Faces.PZ.positions.length; i += 3) {
            positions[i] += position.x;
        }
        for (int i = 1; i < Faces.PZ.positions.length; i += 3) {
            positions[i] += position.y;
        }
        for (int i = 2; i < Faces.PZ.positions.length; i += 3) {
            positions[i] += position.z;
        }
        return positions;
        
        
    }
    
}
