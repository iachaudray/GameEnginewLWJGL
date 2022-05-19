package Engine.Input;

import Engine.Objects.Camera;
import Engine.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    public static final float SPEED = 0.4f;
    private static KeyListener instance;
    private boolean[] keyPressed = new boolean[350];
    private static Vector3f cameraInc = new Vector3f();
    
    private KeyListener() {
    }
    
    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
        }
        return instance;
    }
    
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
        
    }
    
    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
    
    public static void pollMoves(Camera camera) {
        if (isKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(Window.get().getWindow(), true);
        }
        cameraInc.set(0.0f);
        if (isKeyPressed(GLFW_KEY_W)) {
            camera.position.z -= 0.1f;
            
        }
        if (isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            cameraInc.y = -1;
        } else if (isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }
        if (isKeyPressed(GLFW_KEY_GRAVE_ACCENT)) {
            glfwSetInputMode(Window.get().getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        } else if (isKeyPressed(GLFW_KEY_TAB)) {
            glfwSetInputMode(Window.get().getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }
        //cameraInc.normalize();
        movePosition(cameraInc.x * SPEED, cameraInc.y * SPEED, cameraInc.z * SPEED, camera);
        
        
    }
    
    public static void movePosition(float offsetX, float offsetY, float offsetZ, Camera camera) {
        if (offsetZ != 0) {
            camera.position.x += (float) Math.sin((float) Math.toRadians(camera.rotation.y)) * -1.0f * offsetZ;
            camera.position.z += (float) Math.cos(Math.toRadians(camera.rotation.y)) * offsetZ;
        }
        if (offsetX != 0) {
            camera.position.x += (float) Math.sin(Math.toRadians(camera.rotation.y - 90.0f)) * -1.0f * offsetX;
            camera.position.z += (float) Math.cos(Math.toRadians(camera.rotation.y - 90.0f)) * offsetX;
        }
        camera.position.y += offsetY;
    }
}

