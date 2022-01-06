package engine;

import org.joml.Vector2f;

public class Transform
{

    // Variables
    public Vector2f position, scale;

    // Constructor
    public Transform(Vector2f position, Vector2f scale)
    {
        this.init(position, scale);
    }

    public Transform(Vector2f position)
    {
        // Overloading Transform(Vector2f position, Vector2f scale) constructor
        this(position, new Vector2f());
    }

    public Transform()
    {
        // Overloading Transform(Vector2f position, Vector2f scale) constructor
        this(new Vector2f(), new Vector2f());
    }


    // Methods
    public void init(Vector2f position, Vector2f scale)
    {
        this.position = position;
        this.scale = scale;
    }


}
