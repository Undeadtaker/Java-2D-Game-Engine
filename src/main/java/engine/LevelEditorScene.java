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

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100),
                new Vector2f(32, 32)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/kekw.png")));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 1", new Transform(new Vector2f(800, 100),
                new Vector2f(32, 32)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/kekw.png")));
        this.addGameObjectToScene(obj2);

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
