package util;

import ECS.Components.SpriteSheet;
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
    private static Map<String, SpriteSheet> hashedSpriteSheets = new HashMap<>();

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


    // Sprite Sheet
    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet)
    {
        File file = new File(resourceName);
        if(!AssetPool.hashedSpriteSheets.containsKey(file.getAbsolutePath()))
        {
            AssetPool.hashedSpriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName)
    {
        File file = new File(resourceName);
        assert AssetPool.hashedSpriteSheets.containsKey(file.getAbsolutePath()) : "Error, " +
                "tried to access SpriteSheet " + resourceName + "\nTry to add the sheet " +
                "to the assets folder";

        // Return the sprite sheet if it exists, if not return null
        return AssetPool.hashedSpriteSheets.getOrDefault(file.getAbsolutePath(), null);
    }


}
