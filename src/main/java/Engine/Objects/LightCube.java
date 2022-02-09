package Engine.Objects;

import Engine.Camera;
import Engine.Shader;
import Engine.Window;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LightCube extends GameObject {
    private final Vector3f color;
    int vaoID, vboId;
    private static final int[] elementArray = {3, 0, 2, 0, 3, 1, 7, 6, 4, 4, 5, 7, 1, 4, 0, 1, 5, 4, 3, 2, 6, 3, 6, 7, 0, 4, 6, 6, 2, 0, 1, 3, 5, 5, 3, 7};
    private float[] verts = new float[28];
    private static final Shader shader = new Shader("assets/LightCube.glsl");
    
    
    public LightCube(Vector3f position, Vector3f scale, String name) {
        super(position, scale, new Vector3f(), name);
        this.color = new Vector3f(0.0f, 0.0f, 0.0f);
        initMesh();
    }
    
    public LightCube(Vector3f position, Vector3f scale, Vector3f color, String name) {
        super(position, scale, new Vector3f(), name);
        this.color = color;
        initMesh();
        
    }
    
    public LightCube(Vector3f position, Vector3f scale, Vector3f color, Vector3f rotation, String name) {
        super(position, scale, rotation, name);
        this.color = color;
        initMesh();
        
    }
    
    private void initMesh() {
        verts = new float[]{
                position.x - (scale.x / 2), position.y + (scale.y / 2), position.z - (scale.z / 2),
                position.x + (scale.x / 2), position.y + (scale.y / 2), position.z - (scale.z / 2),
                position.x - (scale.x / 2), position.y - (scale.y / 2), position.z - (scale.z / 2),
                position.x + (scale.x / 2), position.y - (scale.y / 2), position.z - (scale.z / 2),
                position.x - (scale.x / 2), position.y + (scale.y / 2), position.z + (scale.z / 2),
                position.x + (scale.x / 2), position.y + (scale.y / 2), position.z + (scale.z / 2),
                position.x - (scale.x / 2), position.y - (scale.y / 2), position.z + (scale.z / 2),
                position.x + (scale.x / 2), position.y - (scale.y / 2), position.z + (scale.z / 2),
        };
        shader.use();
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        //Allocate space for vertices
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) verts.length * Float.BYTES, GL_DYNAMIC_DRAW);
        //Create and upload indices buffer
        int eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementArray, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
    }
    
    
    @Override
    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verts, GL_DYNAMIC_DRAW);
        shader.use();
        shader.uploadMat4f("projectionMatrix", Window.get().getCamera().getProjectionMatrix());
        shader.uploadMat4f("viewMatrix", Window.get().getCamera().getViewMatrix());
        shader.uploadMat4f("modelMatrix", Window.get().getCamera().getModelViewMatrix(this));
        shader.uploadVec3f("objectColor", color);
        shader.uploadVec3f("lightColor", Light.lightColor);
        //shader.uploadVec3f("lightPos", Window.get().getLight().position);
        shader.uploadMat3f("normalMatrix", Camera.getNormalMatrix(Window.get().getCamera().getModelViewMatrix(this)));
        shader.uploadVec3f("cameraPos", Window.get().getCamera().position);
        shader.uploadVec3f("sunLightDirection", Window.get().getSunLightDirection());
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        shader.detach();
    }
}
