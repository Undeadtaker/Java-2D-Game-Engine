package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader
{

    // Variables
    private int shaderProgramID;
    private String vertexSource, fragmentSource, filepath;
    private boolean b_isUsed = false;



    // Constructor + methods
    public Shader(String filepath) throws IOException
    {
        this.filepath = filepath;

        try
        {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // + 6 is for the beginning of the end of index of word type, first vertex
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);

            // Cut off the space, vertex
            String firstPattern = source.substring(index, eol).trim();


            // Second type fragment
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);

            // Cut off the space, fragment
            String secondPattern = source.substring(index, eol).trim();


            // firstPattern
            if(firstPattern.equals("vertex")) vertexSource = splitString[1];
            else if(firstPattern.equals("fragment")) fragmentSource = splitString[1];
            else throw new IOException("Unexpected token " + firstPattern + " found in file");

            // secondPattern
            if(secondPattern.equals("vertex")) vertexSource = splitString[2];
            else if(secondPattern.equals("fragment")) fragmentSource = splitString[2];
            else throw new IOException("Unexpected token " + secondPattern + " found in file");

        }
        catch (IOException e)
        {
            e.printStackTrace();
            assert false: "Could not open file for shader" + filepath;
        }

    }

    // Compiles the shader
    public void compile()
    {
        // Compile and link the shaders
        int vertexID, fragmentID;

        // First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation, glGetShaderi (shaderi = shader information)
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if (success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Shader " + filepath + " Vertex shader comilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));

            // Exit out of program once error finished displaying
            assert false: "";
        }

        // First load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors in compilation, glGetShaderi (shaderi = shader information)
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);

        if (success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Shader " + filepath + " Fragment shader comilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));

            // Exit out of program once error finished displaying
            assert false: "";
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking error
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Shader " + filepath + " Linking of the two shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));

            // Exit out of program once error finished displaying
            assert false: "";
        }
    }

    public void use()
    {
        if(!this.b_isUsed)
        {
            // Bind shader program
            glUseProgram(shaderProgramID);
            this.b_isUsed = true;
        }
    }

    public void detach()
    {
        glUseProgram(0);
        this.b_isUsed = false;
    }


    // Function that uploads the matrix4f into the shader, is then
    // sent to the GPU. Is in the format of the 16 bit matrix as
    // [1,1,1,1, ... 1] - 16 ones.
    public void uploadMat4f(String varName, Matrix4f mat4)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        this.use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16); // capacity is 16 since its 4x4 matrix
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    // Function that uploads matrix3f to the shader
    public void uploadMat3f(String varName, Matrix3f mat3)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        this.use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9); // capacity is 16 since its 4x4 matrix
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }


    // Function that uploads Vector4f to the shader
    public void uploadVec4f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        this.use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    // Function that uploads Vector3f to the shader
    public void uploadVec3f(String varName, Vector3f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        this.use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    // Function that uploads Vector2f to the shader
    public void uploadVec2f(String varName, Vector2f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        this.use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    // Function that uploads Float to the shader
    public void uploadFloat(String varName, float floatToUpload)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        this.use();
        glUniform1f(varLocation, floatToUpload);
    }

    // Function that uploads Int to the shader
    public void uploadInt(String varName, int intToUpload)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        this.use();
        glUniform1i(varLocation, intToUpload);
    }

    // Function that uploads Texture to the shader
    public void uploadTexture(String varName, int slot)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        this.use();
        glUniform1i(varLocation, slot);
    }

}


