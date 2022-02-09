package Engine;

import Engine.Objects.GameObject;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends GameObject {
    
    
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private static final Vector3f xRight = new Vector3f(1, 0, 0);
    private static final Vector3f forward = new Vector3f(0.0f, 0.0f, 1.0f);
    private static final Vector3f yUp = new Vector3f(0.0f, 1.0f, 0.0f);
    private static final Matrix4f projectionMatrix = new Matrix4f().perspective(FOV, (float) Window.get().getWidth() / Window.get().getHeight(),
            Z_NEAR, Z_FAR);
    private static Matrix4f matrix4f = new Matrix4f();
    private static final Matrix3f normal = new Matrix3f();
    private static final Matrix3f normalmat = new Matrix3f();
    
    public Camera(Vector3f position) {
        super(position, new Vector3f(1), new Vector3f(), "");
        
    }
    
    public Matrix4f getViewMatrix() {
        matrix4f.identity();
        matrix4f.rotate((float) Math.toRadians(rotation.x), xRight)
                .rotate((float) Math.toRadians(rotation.y), yUp);
        matrix4f.translate(-position.x, -position.y, -position.z).normal(normal)
                .invert()
                .transform(forward.set(0, 0, -1))
                .mul(0.2f);
        return matrix4f;
    }
    
    
    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }
    
    public Matrix4f getModelViewMatrix(GameObject gameObject) {
        Vector3f rotationGO = gameObject.getRotation();
        matrix4f.identity().
                rotateX((float) Math.toRadians(rotationGO.x)).
                rotateY((float) Math.toRadians(rotationGO.y)).
                rotateZ((float) Math.toRadians(rotationGO.z)).translate(gameObject.getPosition());
        return matrix4f;
    }
    
    public Matrix4f getModelViewMatrix(Vector3f position) {
        Vector3f rotationGO = new Vector3f(0);
        matrix4f.identity().
                rotateX((float) Math.toRadians(rotationGO.x)).
                rotateY((float) Math.toRadians(rotationGO.y)).
                rotateZ((float) Math.toRadians(rotationGO.z)).
                translate(position);
        return matrix4f;
    }
    
    public static Matrix3f getNormalMatrix(Matrix4f ModelViewMatrix) {
        return ModelViewMatrix.normal(normalmat);
    }
    
    
    @Override
    public void render() {
    }
}
