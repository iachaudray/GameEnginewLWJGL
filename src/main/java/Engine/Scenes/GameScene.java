package Engine.Scenes;

import Engine.Objects.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static Engine.BlockUtils.ChunkBuilder.buildMesh;
@EqualsAndHashCode(callSuper = true)
@Data
public class GameScene extends Scene {
    private ArrayList<Chunk> chunks;
    private Light light;
    private Vector3f sunLightDirection;
    private Chunk chunk;
    private Random random;
   
    
    @Override
    public void init() {
        
        this.random = new Random();
        
        this.chunks = new ArrayList<>();
        sunLightDirection = new Vector3f((float) Math.sin(Math.PI / 2), (float) Math.sin(Math.PI / 10), (float) Math.sin(Math.PI / 5));
        currentCamera = new Camera(new Vector3f(0.0f, 5.0f, 1.0f));
        
        chunk = new Chunk(new Vector3f(0, 0, 0));
        for (int i = 8; i < 16; i += 2) {
            for (int j = 8; j < 16; j += 2) {
                for (int k = 8; k < 16; k += 2) {
                    if (random.nextBoolean()) {
                        chunk.blocks.add(new Block(new Vector3f(i, j, k), Block.Type.BLOCK));
                    }
                
                }
            
            }
        }
        buildMesh(chunk);
        chunk.compile();
        chunks.add(chunk);
    
    }
    
    @Override
    public void update(float dt) {
        for (Chunk c : chunks) {
            c.render();
        }
        
    }
    
    public GameScene() {
        init();
    }
    
}
