/*
    Is used to draw debug objects
*/

package renderer;

import engine.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw
{
    private static int MAX_LINES = 500;
    private static List<Line2D> Lines_li = new ArrayList<>();

    // 6 floats per vertex [x, y, z, r, g, b], 2 vertices per debug line, one for start one for end
    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean b_started = false;


    // Methods
    public static void start()
    {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create the vbo and buffer some memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Enable the vertex array attributes
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(3.0f);

    }

    public static void beginFrame()
    {
        if(!b_started)
        {
            start();
            b_started = true;
        }

        // Remove all the deadlines
        for(int i = 0; i < Lines_li.size(); i++)
        {
            if(Lines_li.get(i).beginFrame() < 0)
            {
                Lines_li.remove(i);
                i--;
            }
        }

    }

    public static void draw()
    {
        if (Lines_li.size() <= 0) return;

        int index = 0;
        for (Line2D line : Lines_li) {
            for (int i=0; i < 2; i++) {
                Vector2f position = i == 0 ? line.getStart() : line.getEnd();
                Vector3f color = line.getColor();

                // Load position
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;

                // Load the color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index += 6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, Lines_li.size() * 6 * 2));

        // Use our shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        // Bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the batch
        glDrawArrays(GL_LINES, 0, Lines_li.size() * 6 * 2);

        // Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbind shader
        shader.detach();
    }



    public static void addLine2D(Vector2f start, Vector2f end)
    {
        addLine2D(start, end, new Vector3f(0, 1, 0), 1);
    }

    public static void addLine2D(Vector2f start, Vector2f end, Vector3f color)
    {
        addLine2D(start, end, color, 1);
    }

    public static void addLine2D(Vector2f start, Vector2f end, Vector3f color, int lifetime)
    {
        if(Lines_li.size() >= MAX_LINES) return;
        DebugDraw.Lines_li.add(new Line2D(start, end, color, lifetime));
    }



}






















