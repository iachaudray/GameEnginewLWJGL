package Engine.Objects;


import Engine.Transform;
import org.joml.Vector3f;

import java.util.List;

public abstract class GameObject {
    private List<GameObject> components;
    public GameObject parentObj;
    public Transform transform;
    public Vector3f position;
    
    public Vector3f scale;
    public Vector3f rotation;
    
    public <T extends GameObject> T getObj(Class<T> componentClass) {
        for (GameObject c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error casting component";
                }
            }
        }
        return null;
    }
    
    public GameObject(Vector3f position, Vector3f scale, Vector3f rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        
    }
    
    public <T extends GameObject> void removeChildObj(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            GameObject c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }
    
    public void addChildObj(GameObject c) {
        this.components.add(c);
        c.setParentObj(this);
    }
    
    public void update(float dt) {
    }
    
    public void start() {
        for (GameObject component : components) {
            component.start();
        }
    }
    
    public void addPosition(Vector3f add) {
        transform.position.x += add.x;
        transform.position.y += add.y;
        transform.position.z += add.z;
    }
    
    public Vector3f getPosition() {
        return position;
    }
    
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    
    public Vector3f getRotation() {
        return rotation;
    }
    
    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
    
    public Vector3f getScale() {
        return scale;
    }
    
    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
    
    public GameObject getParentObj() {
        return parentObj;
    }
    
    public void setParentObj(GameObject parentObj) {
        this.parentObj = parentObj;
    }
    
    public abstract void render();
}
