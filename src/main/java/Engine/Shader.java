package Engine;


import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Shader {
    public int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filepath;
    private String geometrySource;
    public String thirdPattern = null;
    private boolean biengUsed = false;
    
    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-z+]+)");
            //First Shader type
            int index = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf("\n", index);
            String firstPattern = source.substring(index, endOfLine).trim();
            //Second type pattern
            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\n", index);
            String secondPattern = source.substring(index, endOfLine).trim();
            if (splitString.length > 3) {
                index = source.indexOf("#type", endOfLine);
                endOfLine = source.indexOf("\n", index);
                thirdPattern = source.substring(index, endOfLine).trim();
            }
            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
                
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token " + firstPattern);
            }
            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
                
            } else {
                throw new IOException("Unexpected token " + secondPattern);
            }
            if (thirdPattern != null) {
                geometrySource = splitString[3];
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error could not open shader file " + filepath;
        }
    }
    
    public void compile() {
        int vertexId, fragmentId, geometryId = 0;
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, vertexSource);
        glCompileShader(vertexId);
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println(filepath + "\n Vertex Shader compilation fail");
            System.out.println(glGetShaderInfoLog(vertexId, len));
            assert false : "";
        }
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, fragmentSource);
        glCompileShader(fragmentId);
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println(filepath + "\n Fragment Shader compilation fail");
            System.out.println(glGetShaderInfoLog(fragmentId, len));
            assert false : "";
        }
        if (thirdPattern != null) {
            geometryId = glCreateShader(GL_GEOMETRY_SHADER);
            glShaderSource(geometryId, geometrySource);
            glCompileShader(geometryId);
            success = glGetShaderi(geometryId, GL_COMPILE_STATUS);
            if (success == GL_FALSE) {
                int len = glGetShaderi(geometryId, GL_INFO_LOG_LENGTH);
                System.out.println(filepath + "\n Geometry Shader compilation fail");
                System.out.println(glGetShaderInfoLog(geometryId, len));
                assert false : "";
            }
        }
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexId);
        glAttachShader(shaderProgramID, fragmentId);
        if (geometryId != 0) {
            glAttachShader(shaderProgramID, geometryId);
        }
        glLinkProgram(shaderProgramID);
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println(filepath + "\n Shader program linking compilation fail");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
            
        }
        
    }
    
    public void use() {
        if (!biengUsed) {
            glUseProgram(shaderProgramID);
            biengUsed = true;
        }
        
    }
    
    public void detach() {
        biengUsed = false;
        glUseProgram(0);
    }
    
    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
    
    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
        
    }
    
    public void uploadFloat(String varName, float value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, value);
        
    }
    
    public void uploadInt(String varName, int value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, value);
    }
    
    public void uploadVec2f(String varName, Vector2f value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, value.x, value.y);
    }
    
    public void uploadVec3f(String varName, Vector3f value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, value.x, value.y, value.z);
    }
    
    public void uploadMat3f(String varName, Matrix3f value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        value.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }
    
    public void uploadMat2f(String varName, Matrix2f value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4);
        value.get(matBuffer);
        glUniformMatrix2fv(varLocation, false, matBuffer);
    }
    
    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }
    
}
