package Engine.Objects;

import Engine.BlockUtils.CubeModel;
import Engine.Shader;
import Engine.Window;
import org.joml.Vector3f;

import java.util.ArrayList;

import static Engine.BlockUtils.CubeModel.convertBlockPosition;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Chunk extends GameObject{
    private final Shader shader;
    public ArrayList<Block> blocks = new ArrayList<>();
    public ArrayList<Float> vbo;
    private int vaoID, vboId;
    public ArrayList<Integer> elementArray;
    private float[] x;
    private int depthBufferTex;
    
    public Chunk(Vector3f position) {
        super(position, new Vector3f(1), new Vector3f(0));
        
        this.vbo = new ArrayList<>();
        this.elementArray = new ArrayList<>();
        this.shader = new Shader("assets/chunk.glsl");
    }
    public void addFace(Vector3f position, CubeModel.Faces face) {
        float[] x = convertBlockPosition(position, face);
        short xx = (short) x.length;
        for (int i = 0; i < xx; i += 3) {
            vbo.add(x[i]);
            vbo.add(x[i + 1]);
            vbo.add(x[i + 2]);
            vbo.add(face.normal.x);
            vbo.add(face.normal.y);
            vbo.add(face.normal.z);
        }
        elementArray.add(3 + 4 * ((vbo.size() / 24) - 1));
        elementArray.add(2 + 4 * ((vbo.size() / 24) - 1));
        elementArray.add(4 * ((vbo.size() / 24) - 1));
        elementArray.add(4 * ((vbo.size() / 24) - 1));
        elementArray.add(1 + 4 * ((vbo.size() / 24) - 1));
        elementArray.add(3 + 4 * ((vbo.size() / 24) - 1));
        
        
    }
    
    public void compile() {
        
        shader.compile();
        shader.use();
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        //Allocate space for vertices
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        int z = 0;
        x = new float[vbo.size()];
        for (Float f : vbo) {
            x[z++] = f;
        }
        glBufferData(GL_ARRAY_BUFFER, x, GL_DYNAMIC_DRAW);
        //Create and upload indices buffer
        int eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementArray.stream().mapToInt(i -> i).toArray(), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        int frameBufferID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
        depthBufferTex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthBufferTex);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, 1024, 1024, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthBufferTex, 0, 0);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
    }
    
    
   
    
    public void render() {
        
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, x, GL_DYNAMIC_DRAW);
        shader.use();
        shader.uploadMat4f("projectionMatrix", Window.get().getCurrentScene().currentCamera.getProjectionMatrix());
        shader.uploadMat4f("viewMatrix", Window.get().getCurrentScene().currentCamera.getViewMatrix());
        shader.uploadMat4f("lightProjection", Camera.lightProjection);
        shader.uploadMat4f("lightView", Window.get().getCurrentScene().currentCamera.lightView);
        glBindTexture(GL_TEXTURE_2D, depthBufferTex);
        shader.uploadMat4f("modelMatrix", Window.get().getCurrentScene().currentCamera.getModelViewMatrix(position));
        shader.uploadVec3f("sunLightDirection", Window.get().getSunLightDirection());
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, vbo.size(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        shader.detach();
    }
}
