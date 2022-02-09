package Engine.Objects;

import lombok.Data;
import org.joml.Vector3f;

@Data
public class Block {
    public enum Type {
        BLOCK, TYPE;
    }
    
    public Vector3f position;
    
    public Block(Vector3f position, Type type) {
        this.position = position;
        
    }
}
