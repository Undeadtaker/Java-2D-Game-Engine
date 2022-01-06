package util;

import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AssetPool
{

    // Stores the pointers/references of the objects, not the objects themselves
    // which are only 1 byte in size
    private static Map<String, Shader> hashedShaders = new HashMap<>();
    private static Map<String, Texture> hashedTextures = new HashMap<>();

    // METHODS
    public static Shader getShader(String resourceName)
    {
        File file = new File(resourceName);
        if(AssetPool.hashedShaders.containsKey(file.getAbsolutePath()))
        {
            return AssetPool.hashedShaders.get(file.getAbsolutePath());
        }

        else
        {
            try
            {
                Shader shader = new Shader(resourceName);
                shader.compile();
                AssetPool.hashedShaders.put(file.getAbsolutePath(), shader);
                return shader;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Texture getTexture(String resourceName)
    {
        File file = new File(resourceName);
        if(AssetPool.hashedShaders.containsKey(file.getAbsolutePath()))
        {
            return AssetPool.hashedTextures.get(file.getAbsolutePath());
        }

        else
        {
            Texture texture = new Texture(resourceName);
            AssetPool.hashedTextures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }


}
