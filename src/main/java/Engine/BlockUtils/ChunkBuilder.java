package Engine.BlockUtils;


import Engine.Objects.Block;
import Engine.Objects.Chunk;

import static Engine.BlockUtils.CubeModel.Faces.*;

public class ChunkBuilder {
    
    private static boolean px, nx, py, ny, pz, nz;
    private static boolean rightX, rightY, rightZ;
    
    public static void buildMesh(Chunk chunk) {
        for (int i = 0; i < chunk.blocks.size(); i++) {
            Block blockI = chunk.blocks.get(i);
            px = true;
            nx = true;
            py = true;
            ny = true;
            pz = true;
            nz = true;
            rightX = false;
            rightY = false;
            rightZ = false;
            for (int j = 0; j < chunk.blocks.size(); j++) {
                Block blockJ = chunk.blocks.get(j);
                float x1 = blockI.position.x;
                rightX = false;
                rightY = false;
                rightZ = false;
                if (blockI.position.x == blockJ.position.x) {
                    rightX = true;
                }
                if (blockI.position.y == blockJ.position.y) {
                    rightY = true;
                }
                if (blockI.position.z == blockJ.position.z) {
                    rightZ = true;
                }
                if ((blockI.position.x + CubeModel.CUBESIZE == blockJ.position.x) && rightY && rightZ) {
                    px = false;
                }
                if ((blockI.position.x - CubeModel.CUBESIZE == blockJ.position.x) && rightY && rightZ) {
                    nx = false;
                }
                if ((blockI.position.y + CubeModel.CUBESIZE == blockJ.position.y) && rightX && rightZ) {
                    py = false;
                    
                }
                if (((blockI.position.y - CubeModel.CUBESIZE) == blockJ.position.y) && rightX && rightZ) {
                    ny = false;
                    
                }
                if (((blockI.position.z + CubeModel.CUBESIZE) == blockJ.position.z) && rightX && rightY) {
                    pz = false;
                }
                if (((blockI.position.z - CubeModel.CUBESIZE) == blockJ.position.z) && rightX && rightY) {
                    nz = false;
                }
                
                
            }
            if (px) {
                chunk.addFace(blockI.position, PX);
            }
            if (nx) {
                chunk.addFace(blockI.position, NX);
            }
            if (py) {
                chunk.addFace(blockI.position, PY);
            }
            if (ny) {
                chunk.addFace(blockI.position, NY);
            }
            if (pz) {
                chunk.addFace(blockI.position, PZ);
            }
            if (nz) {
                chunk.addFace(blockI.position, NZ);
            }
        }
        
        
    }
}
