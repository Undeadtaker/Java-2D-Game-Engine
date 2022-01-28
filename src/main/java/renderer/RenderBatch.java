package renderer;

import ECS.Components.SpriteRenderer;
import engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>
{
    // Vertex
    // ======
    // Pos               Color                           Tex coords      Tex id
    // float, float,     float, float, float, float      float, float    float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private List<Texture> textures;
    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean b_hasRoom;
    public float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;
    private int z_index;

    // Constructor
    public RenderBatch(int maxBatchSize, int z_index)
    {

        this.z_index = z_index;

        // Instead of creating 10 new shaders, we only create one and only use the
        // reference to it, therefore reducing memory consumption
        shader = AssetPool.getShader("assets/shaders/default.glsl");
        assert shader != null;

        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // 4 vertices quads
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.b_hasRoom = true;
        this.textures = new ArrayList<>();

    }

    // METHODS
    public void start()
    {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        // Two new attributes float tex coords and float tex id
        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    private int[] generateIndices()
    {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++)
        {
            this.loadElementIndices(elements, i);
        }

        return elements;
    }

    public void render()
    {
        boolean b_rebufferData = false;
        for(int i = 0; i < this.numSprites; i++)
        {
            SpriteRenderer sr = sprites[i];
            if(sr.isDirty())
            {
                this.loadVertexProperties(i);
                sr.setClean();
                b_rebufferData = true;
            }
        }
        if(b_rebufferData)
        {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        // We have multiple texture sheets we want to bind, so bind them all and use the ones we want
        for(int i = 0; i < this.textures.size(); i++)
        {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            this.textures.get(i).bind();
        }

        // These are all texture sheets we are uploading
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (Texture texture : this.textures) texture.unbind();

        shader.detach();
    }

    // Loading different indices for the individual triangles, [X, Y, R, G, B, A]
    // so offsetArrayIndex is for the entire triangle size, 6
    // offset is when does the next triangle start, which is for X and Y + 4.
    private void loadElementIndices(int[] elements, int index)
    {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public void addSprite(SpriteRenderer sr_obj)
    {
        // Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = sr_obj;
        this.numSprites++;

        // Passed texture from the Sprite object
        Texture passed_texture = sr_obj.getTexture();
        if(passed_texture != null)
        {
            if(!textures.contains(passed_texture))
            {
                this.textures.add(passed_texture);
            }
        }

        // Add properties to local vertices array
        loadVertexProperties(index);

        if(this.numSprites >= this.maxBatchSize)
        {
            this.b_hasRoom = false;
        }
    }

    private void loadVertexProperties(int index)
    {
        SpriteRenderer sprite = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        int texID = 0;
        if (sprite.getTexture() != null)
        {
            for (int i = 0; i < this.textures.size(); i++)
            {
                // Loop over textures with i as their index, if any equal to the
                // current texture we are trying to pass, assign texID
                if(this.textures.get(i).equals(sprite.getTexture()))
                {
                    texID = i + 1;
                    break;
                }
            }
        }
        // Add vertices with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for(int i = 0; i < 4; i++)
        {

            if (i == 1)
            {
                yAdd = 0.0f;
            }
            else if (i == 2)
            {
                xAdd = 0.0f;
            }
            else if (i == 3)
            {
                yAdd = 1.0f;
            }

            // POSITION -> X, Y
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);

            // COLOR -> R, G, B, A
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // TEX_COORDS -> X, Y
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            // TEX_ID -> ID
            vertices[offset + 8] = texID;


            offset += VERTEX_SIZE;

        }
    }

    public boolean hasRoom()
    {
        return this.b_hasRoom;
    }
    public boolean hasTextureRoom() {return this.textures.size() < 8;}
    public boolean hasTexture(Texture tex) {return this.textures.contains(tex);}
    public int getZ_index(){return this.z_index;}

    @Override
    public int compareTo(RenderBatch o)
    {
        return Integer.compare(this.z_index, o.z_index);
    }
}


















