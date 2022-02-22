package Engine.Objects;

import Engine.Camera;
import Engine.Shader;
import Engine.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import static Engine.Objects.CubeModel.convertBlockPosition;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Chunk {
    private Shader shader, shadowShader;
    public ArrayList<Block> blocks = new ArrayList<>();
    private final Vector3f position;
    public ArrayList<Float> vbo;
    private int vaoID, vboId;
    public ArrayList<Integer> elementArray;
    private float[] x;
    private int frameBufferID;
    private int depthBufferTex;
    private final static int SHADOW_HEIGHT = 1024;
    
    public Chunk(Vector3f position) {
        this.position = position;
        this.vbo = new ArrayList<>();
        this.elementArray = new ArrayList<>();
        this.shader = new Shader("assets/chunk.glsl");
        this.shadowShader = new Shader("assets/ShadowMapping.glsl");
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
        shadowShader.compile();
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
        
        frameBufferID = glGenFramebuffers();
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
        //render with shadow mapping first
        
        glViewport(0, 0, SHADOW_HEIGHT, SHADOW_HEIGHT);
        glBindFramebuffer(GL_FRAMEBUFFER, depthBufferTex);
        glClear(GL_DEPTH_BUFFER_BIT);
        shadowShader.use();
        shadowShader.uploadMat4f("lightProjection", Camera.lightProjection);
        shadowShader.uploadMat4f("lightView", Window.get().getCamera().lightView);
        shadowShader.uploadMat4f("modelMatrix", Window.get().getCamera().getModelViewMatrix(position));
        glBindTexture(GL_TEXTURE_2D, depthBufferTex);
        glDrawElements(GL_TRIANGLES, vbo.size(), GL_UNSIGNED_INT, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        //render normally
        glViewport(0, 0, Window.get().getWidth(), Window.get().getHeight());
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, x, GL_DYNAMIC_DRAW);
        shader.use();
        shader.uploadMat4f("projectionMatrix", Window.get().getCamera().getProjectionMatrix());
        shader.uploadMat4f("viewMatrix", Window.get().getCamera().getViewMatrix());
        shader.uploadMat4f("lightProjection", Camera.lightProjection);
        shader.uploadMat4f("lightView", Window.get().getCamera().lightView);
        glBindTexture(GL_TEXTURE_2D, depthBufferTex);
        shadowShader.uploadTexture("depthTex", depthBufferTex);
        shader.uploadMat4f("modelMatrix", Window.get().getCamera().getModelViewMatrix(position));
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
