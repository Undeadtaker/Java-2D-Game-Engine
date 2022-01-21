
/* Prefabs are a special type of component that
   allows fully configured GameObjects to be saved
   in the Project for reuse. These assets can then
   be shared between scenes, or even other projects
   without having to be configured again. */

package ECS;
import ECS.Components.Sprite;
import ECS.Components.SpriteRenderer;
import engine.Transform;
import org.joml.Vector2f;

public class Prefab
{
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY)
    {
        GameObject block = new GameObject("Sprite_Object_Gen",
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

}
