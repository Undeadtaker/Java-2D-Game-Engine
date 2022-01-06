package engine;

import ECS.Components.SpriteRenderer;
import ECS.GameObject;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.io.IOException;


public class LevelEditorScene extends Scene {


    // Constructor + other methods
    public LevelEditorScene() {}

    @Override
    public void init()
    {
        this.camera = new Camera(new Vector2f(0, 0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float) (600 - xOffset * 2);
        float totalHeight = (float) (300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for(int x = 0; x < 100; x++)
        {
            for(int y = 0; y < 100; y++)
            {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject go_obj = new GameObject("Obj" + x + "" + y,
                        new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));

                go_obj.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(go_obj);
            }
        }

        loadResources();
    }


    @Override
    public void update(float dt)
    {
        for (GameObject go_obj : this.gameObjects) go_obj.update(dt);

        this.renderer.render();
    }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");
    }
}
