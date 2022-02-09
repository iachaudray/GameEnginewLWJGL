package Engine;

import org.joml.Vector3f;

public class Transform {
    public Vector3f position;
    public Vector3f scale;
    
    
    public Transform() {
        this.position = new Vector3f();
        this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
    }
    
    public Transform(Vector3f position, Vector3f scale) {
        this.position = position;
        this.scale = scale;
    }
    
    public Transform(Vector3f position) {
        this.position = position;
    }
}
