package ECS.Components;

import imgui.ImGui;
import ECS.Component;
import engine.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component
{

    // Variables
    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();

    boolean b_isDirty = true;
    private Transform lastTransform;


    // Methods
    @Override
    public void start()
    {
        // Create a new Transform that is equal to the gameObject's current Transform,
        // the SpriteRenderer extends from Component which means it inherits the
        // gameObject object
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt)
    {
        // Check if the lastTransform is equal to the new version of Transform's values
        // by using the equals overridden method in Transform class
        if(!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.copy(this.lastTransform);
            this.b_isDirty = true;
        }
    }

    @Override
    public void updateComponentImgui()
    {
        float[] compColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorPicker4("Color Picker: ", compColor))
        {
            this.color.set(compColor[0], compColor[1], compColor[2], compColor[3]);
            this.b_isDirty = true;
        }
    }

    public void setSprite(Sprite sprite)
    {
        this.sprite = sprite;

        // Set isDirty to true if sprite has changed
        this.b_isDirty = true;
    }

    public void setColor(Vector4f color)
    {
        if(!this.color.equals(color))
        {
            this.color.set(color);

            // Set isDirty to true if the color has changed
            this.b_isDirty = true;
        }
    }



    public Vector4f getColor() {return this.color;}
    public Texture getTexture() {return sprite.getTexture();}
    public Vector2f[] getTexCoords() {return sprite.getTexCoords();}

    public boolean isDirty() {return this.b_isDirty;}
    public void setClean() {this.b_isDirty = false;}
    public void setTexture(Texture texture) {this.sprite.setTexture(texture);}


}
