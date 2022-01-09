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

    public Transform copy()
    {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    // Copy to the new transform object the contents of this transform
    public void copy(Transform obj_to_copy_values_to)
    {
        obj_to_copy_values_to.position.set(this.position);
        obj_to_copy_values_to.scale.set(this.scale);
    }


    // Overriding the OBJECT method equals, check if this transform is the same as the passed one
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof Transform)) return false;

        Transform t = (Transform) obj;
        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }

}
