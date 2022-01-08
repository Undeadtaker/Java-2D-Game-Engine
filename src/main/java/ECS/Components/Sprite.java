package ECS.Components;

import org.joml.Vector2f;
import renderer.Texture;

public class Sprite
{
    // Variables
    private final Texture texture;
    private final Vector2f[] texCoords;

    // Constructors
    public Sprite(Texture texture, Vector2f[] texCoords)
    {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    public Sprite(Texture texture)
    {
        /*
        Texture coordinates passed to the GPU have to have the following coordinates

        (0, 1) -> bottom right
        (0, 0) -> bottom left
        (1, 1) -> top right
        (1, 0) -> top left

        That's why we need a Vector2f to store the coordinates of the texture
    */

        // Overloading main constructor
        this(texture, new Vector2f[] {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)});
    }

    // Methods
    public Texture getTexture(){return this.texture;}
    public Vector2f[] getTexCoords(){return this.texCoords;}
}
