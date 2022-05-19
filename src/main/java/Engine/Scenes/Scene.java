package Engine.Scenes;

import Engine.Objects.Camera;
import Engine.Objects.Chunk;
import Engine.Objects.GameObject;
import lombok.Data;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Data
public abstract class Scene {
    
    public Camera currentCamera;
    public abstract void init();
    public abstract void update(float dt);
}
