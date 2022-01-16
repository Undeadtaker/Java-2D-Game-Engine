package ECS.Components;

import org.joml.Vector2f;
import renderer.Texture;

public class Sprite
{
    // Variables
    private Texture texture = null;
    private Vector2f[] texCoords = new Vector2f[] {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)};

    // Setters
    public void setTexture(Texture texture) {this.texture = texture;}
    public void setTexCoords(Vector2f[] texCoords) {this.texCoords = texCoords;}

    // Getters
    public Texture getTexture(){return this.texture;}
    public Vector2f[] getTexCoords(){return this.texCoords;}
}
