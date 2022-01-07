package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
    // Variables
    private String filepath;
    private final int texID;


    public Texture(String filepath)
    {
        this.filepath = filepath;

        // Generate the texture on the GPU
        this.texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set texture parameters
        // Repeat the image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // When stretching image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        // When shrinking also pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // load image upwards since OpenGL does it automatically so it will be rendered the way its supposed to be
        stbi_set_flip_vertically_on_load(true);

        // stbi is an image loading library
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        // Uploads image data to the shader/GPU, depends on whether image has alpha or not
        if (image != null)
        {
            if (channels.get(0) == 3)
            {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }
            else if (channels.get(0) == 4)
            {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
            else
            {
                assert false : "Error: (Texture) Unknown number of channesl '" + channels.get(0) + "'";
            }
        }
        else
        {
            assert false : "Error: (Texture) Could not load image '" + filepath + "'";
        }

        // Unless freed will cause a memory leak
        stbi_image_free(image);

    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, this.texID);
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
