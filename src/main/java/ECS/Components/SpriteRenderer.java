package ECS.Components;

import ECS.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component
{

    // Variables
    private Vector4f color;
    private Sprite sprite;


    // Constructor
    public SpriteRenderer(Vector4f color)
    {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    // Constructor that will create a Sprite with a texture instead of just color
    public SpriteRenderer(Sprite sprite)
    {
        this.sprite = sprite;
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
        return sprite.getTexture();
    }
    public Vector2f[] getTexCoords() {return sprite.getTexCoords();}

}
