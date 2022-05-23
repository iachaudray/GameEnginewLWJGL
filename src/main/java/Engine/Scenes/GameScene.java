package Engine.Scenes;

import Engine.Objects.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joml.Vector3f;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import static Engine.BlockUtils.ChunkBuilder.buildMesh;
@EqualsAndHashCode(callSuper = true)
@Data
public class GameScene extends Scene {
    private ArrayList<GameObject> gameObjects;
    private Light light;
    private Vector3f sunLightPosition;
    private Chunk chunk;
    private Random random;
    
   
    
    @Override
    public void init()  {
        
        this.random = new Random();
        
        this.gameObjects = new ArrayList<>();
        sunLightPosition = new Vector3f((float) Math.sin(Math.PI / 2), (float) Math.sin(Math.PI / 10), (float) Math.sin(Math.PI / 5));
        currentCamera = new Camera(new Vector3f(0.0f, 5.0f, 1.0f));
        
        
        for (int l = 0; l < 80; l+=8) {
            chunk = new Chunk(new Vector3f(l, 0, 0));
            for (int i = 0; i < 8; i += 2) {
                for (int j = 0; j < 8; j += 2) {
                    for (int k = 0; k < 8; k += 2) {
                        if (random.nextBoolean()) {
                            chunk.blocks.add(new Block(new Vector3f(i, j, k), Block.Type.BLOCK));
                        }
                
                    }
            
                }
            }
            buildMesh(chunk);
            chunk.compile();
            gameObjects.add(chunk);
        }
        
    
    }
    
    @Override
    public void update(float dt) {
        for (GameObject go : gameObjects) {
            go.render();
        }
        
    }
    
    public GameScene() {
        Light.lightColor = new Vector3f(1f);
        init();
    }
    
}
