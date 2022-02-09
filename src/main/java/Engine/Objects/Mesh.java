package Engine.Objects;

import Engine.Shader;
import Engine.Window;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh extends GameObject {
    
    private Vector4f color;
    private int vaoID, vboId;
    private Shader shader;
    private static final int[] elementArray = {3, 2, 0, 0, 3, 1};
    private float[] verts;
    
    public Mesh(Vector3f position, Vector3f scale, String name) {
        super(position, scale, new Vector3f(), name);
        this.color = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
        initMesh();
    }
    
    public Mesh(Vector3f position, Vector3f scale, Vector4f color, String name) {
        super(position, scale, new Vector3f(), name);
        this.color = color;
        initMesh();
    }
    
    private void initMesh() {
        verts = new float[]{
                position.x - (scale.x / 2), position.y + (scale.y / 2), position.z, color.x, color.y, color.z, color.w,
                position.x + (scale.x / 2), position.y + (scale.y / 2), position.z, color.x, color.y, color.z, color.w,
                position.x - (scale.x / 2), position.y - (scale.y / 2), position.z, color.x, color.y, color.z, color.w,
                position.x + (scale.x / 2), position.y - (scale.y / 2), position.z, color.x, color.y, color.z, color.w,
        };
        shader = new Shader("assets/default.glsl");
        shader.compile();
        shader.use();
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        //Allocate space for vertices
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verts.length * Float.BYTES, GL_DYNAMIC_DRAW);
        //Create and upload indices buffer
        int eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementArray, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * Float.BYTES, 12);
        glEnableVertexAttribArray(1);
    }
    
    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verts, GL_DYNAMIC_DRAW);
        shader.use();
        shader.uploadMat4f("projectionMatrix", Window.get().getCamera().getProjectionMatrix());
        shader.uploadMat4f("viewMatrix", Window.get().getCamera().getViewMatrix());
        shader.uploadMat4f("modelMatrix", Window.get().getCamera().getModelViewMatrix(this));
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }
    
}
