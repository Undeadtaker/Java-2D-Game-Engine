package ECS.Components;

import org.joml.Vector2f;
import renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet
{

    // Variables
    private Texture texture;
    private List<Sprite> sprites_li;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing)
    {
        this.sprites_li = new ArrayList<>();
        this.texture = texture;

        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        for(int i = 0; i < numSprites; i++)

        {   float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            this.sprites_li.add(sprite);

            currentX += spriteWidth + spacing;

            // If all sprites have loaded from the sprite sheet, no more space left
            if(currentX >= texture.getWidth())
            {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    // Returns the sprite the user asked from this spritesheet at index
    public Sprite getSprite(int index){return this.sprites_li.get(index);}

}















