package Engine.Input;

import static Engine.Window.get;

public class MouseListener {
    private static double lastX = -1;
    private static double lastY = -1;
    private static double xOffset, yOffset;
    private static final float sensitivity = 0.1f;
    
    
    public static void cursorPositionCallback(long window, double xPos, double yPos) {
        if (lastX == -1 && lastY == -1) {
            lastX = xPos;
            lastY = yPos;
        }
        xOffset = xPos - lastX;
        yOffset = yPos - lastY;
        lastX = xPos;
        lastY = yPos;
        get().getCamera().rotation.x += yOffset * sensitivity;
        get().getCamera().rotation.y += xOffset * sensitivity;
        get().getCamera().rotation.z = 0;
        if (get().getCamera().rotation.x > 89.0f)
            get().getCamera().rotation.x = 89.0f;
        if (get().getCamera().rotation.x < -89.0f)
            get().getCamera().rotation.x = -89.0f;
        
    }
}
