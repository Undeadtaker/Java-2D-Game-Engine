package engine;

import ECS.Components.SpringRenderer;
import ECS.GameObject;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    private final String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";


    private final String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";


    private int vertexID, fragmentID, shaderProgram, vaoID, vboID, eboID;

    private float[] vertexArray = {
            // position                 // color                  // UV Coordinates
            100.5f, 0.5f  , 0.0f,       1.0f, 0.0f, 0.0f, 1.0f,   1, 1,// Bottom right 0
            0.5f  , 100.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f,   0, 0,// Top left     1
            100.5f, 100.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f,   1, 0,// Top right    2
            0.5f  , 0.5f  , 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,   0, 1 // Bottom left  3
    };


    // IMPORTANT, must be in counter-clockwise order
    private final int[] elementArray = {

            2,1,0, // top right triangle
            0,1,3  // bottom left triangle
    };

    // Declare shader
    private Shader defaultShader;
    private Texture testTexture;

    public GameObject gameObject;
    private boolean b_firstTime = false;









    // Constructor + other methods
    public LevelEditorScene()
    {

    }

    @Override
    public void init()
    {
        // Creating Test Object
        System.out.println("Creating test object in INIT method");
        this.gameObject = new GameObject("test object");
        this.gameObject.addComponent(new SpringRenderer());
        this.addGameObjectToScene(this.gameObject);

        // Initialize all objects
        this.camera = new Camera(new Vector2f());

        try {
            defaultShader = new Shader("assets/shaders/shaders.dlsl");
            defaultShader.compile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.testTexture = new Texture("assets/images/kekw.png");

        // Generating VAO, VBO and EBO buffer objects, and send them to the GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);


        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false,
                vertexSizeBytes, 0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false,
                vertexSizeBytes, positionsSize * Float.BYTES);
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false,
                vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES );

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

    }

    @Override
    public void update(float dt)
    {

        this.camera.position.x -= dt * 10.0f;
        this.camera.position.y -= dt * 5.0f;

        // Bind shader program, upload variables to shader
        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        if(!this.b_firstTime)
        {
            System.out.println("Creating test object in LOOP method");
            GameObject obj = new GameObject("test object in loop");
            obj.addComponent(new SpringRenderer());
            this.addGameObjectToScene(obj);
            this.b_firstTime = true;
        }

        for (GameObject go_obj : this.gameObjects) go_obj.update(dt);

    }
}
