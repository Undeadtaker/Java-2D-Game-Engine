package ECS.Components;

import ECS.Component;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{

    // Variables
    private Vector4f color;

    // Constructor
    public SpriteRenderer(Vector4f color)
    {
        this.color = color;
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

}
