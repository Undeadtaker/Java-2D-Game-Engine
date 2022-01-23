/*
    This class is used to draw a debug
    line that goes from start until the end.
    It will also have a color, as well as the
    lifetime for how long it can stay on the screen.
*/


package renderer;


import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D
{
    private Vector2f start;
    private Vector2f end;
    private Vector3f color;
    private int lifetime;


    // Constructor
    public Line2D(Vector2f start, Vector2f end, Vector3f color, int lifetime)
    {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifetime = lifetime;
    }


    // Methods
    public int beginFrame()
    {
        this.lifetime--;
        return this.lifetime;
    }


    // Getters
    public Vector2f getStart() {return start;}
    public Vector2f getEnd() {return end;}
    public Vector3f getColor() {return color;}

}






















