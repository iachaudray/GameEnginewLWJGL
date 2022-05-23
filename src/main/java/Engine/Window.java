package Engine;

import Engine.Input.KeyListener;
import Engine.Input.MouseListener;
import Engine.Objects.*;
import Engine.Scenes.GameScene;
import Engine.Scenes.Scene;
import lombok.*;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Data
public class Window {
    private static Window instance = null;
    private final int height, width;
    public static float r, g, b, a;
    private final String title;
    private long window;
    private List<GameObject> gameObjects;
    private ArrayList<Chunk> chunks;
    private Light light;
    private Camera camera;
    private Vector3f sunLightDirection;
    private Chunk chunk;
    
    
    
    private Random random;
    private Scene currentScene;
    
    private Window(int height, int width, String title) {
        r = 0.0f; g = 0.72f; b = 0.76f;
        this.height = height;
        this.width = width;
        this.title = title;
        this.chunks = new ArrayList<>();
        this.random = new Random();
        this.gameObjects = new LinkedList<>();
        sunLightDirection = new Vector3f((float) Math.sin(Math.PI / 2), (float) Math.sin(Math.PI / 10), (float) Math.sin(Math.PI / 5));
    }
    
    public void run() {
        System.out.println("Version " + Version.getVersion());
        init();
        loop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        System.out.println((((((((double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000)))))) + "KB used");
    }
    
    /**
     * Creates the window, Implements glfwCallbacks for keys and cursor, connects glfw Context to OpenGL, Creates
     */
    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to init GLFW");
        }
        glfwHints();
        window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Didn't make glfw window");
        }
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetKeyCallback(window, KeyListener::keyCallback);
        glfwSetCursorPosCallback(window, MouseListener::cursorPositionCallback);
        glfwMakeContextCurrent(window);
        glfwSetWindowPos(window, 1440 - this.width, 900 - this.height);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        currentScene = new GameScene();
        
        
        
        
        
        
        
        glEnable(GL_DEPTH_TEST);
        
        
    }
    
    
    /**
     * Main application loop.
     * Updates all current objects.
     * Updates delta.
     * Swaps buffers.
     * Polls glfw events.
     */
    private void loop() {
        float beginTime = getTime();
        float endTime;
        float dt = 0;
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        while (!glfwWindowShouldClose(window)) {
            
            glfwPollEvents();
            KeyListener.pollMoves(currentScene.currentCamera);
            glClearColor(r, g, b, 0.5f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            for (GameObject e : gameObjects) {
                e.render();
            }
            for (Chunk c : chunks) {
                c.render();
            }
            currentScene.update(dt);
            glfwSwapBuffers(window);
            endTime = getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
            
        }
    }
    
    
    
    
    
    /**
     * returns the window singleton and creates one if there is no Window.
     * @return Window singleton.
     */
    public static Window get() {
        
        if (instance == null) {
            instance = new Window(400, 600, "Engine.Window");
        }
        return instance;
    }
    
    
    /**
     * The time the application started.
     * Used for delta time
     *
     */
    public static float timeStarted = System.nanoTime();
    
    
    
    /**
     * Calculates the time passed since the application started in seconds
     * @return time in seconds
     */
    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
        
    }
    
    /**
     * Gives glfw hints for creating a window. Also changes opengl profile to be compatible with macOS
     */
    public void glfwHints() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    }
    
    
    
    
}