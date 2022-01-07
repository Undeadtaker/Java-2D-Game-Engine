package ECS.Components;

import ECS.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component
{

    // Variables
    private Vector4f color;

    /*
        Texture coordinates passed to the GPU have to have the following coordinates

        (0, 1) -> bottom right
        (0, 0) -> bottom left
        (1, 1) -> top right
        (1, 0) -> top left

        That's why we need a Vector2f to store the coordinates of the texture
    */
    private Vector2f[] texCoords;
    private Texture texture;


    // Constructor
    public SpriteRenderer(Vector4f color)
    {
        this.color = color;
        this.texture = null;
    }

    // Constructor that will create a Sprite with a texture instead of just color
    public SpriteRenderer(Texture texture)
    {
        this.texture = texture;
        this.color = new Vector4f(1, 1, 1, 1); // white
    }


    // Methods
    @Override
    public void update(float dt) {}

    @Override
    public void start() {}

    public Vector4f getColor()
    {
        return this.color;
    }


    public Texture getTexture()
    {
        return this.texture;
    }

    public Vector2f[] getTexCoords()
    {
        Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

        return texCoords;
    }

}
