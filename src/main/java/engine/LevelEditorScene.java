package engine;

import ECS.Components.SpriteRenderer;
import ECS.Components.SpriteSheet;
import ECS.GameObject;
import org.joml.Vector2f;
import util.AssetPool;


public class LevelEditorScene extends Scene {

    private GameObject obj1;

    // Constructor + other methods
    public LevelEditorScene() {}

    @Override
    public void init()
    {
        this.loadResources();

        this.camera = new Camera(new Vector2f(0, 0));

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100),
                new Vector2f(32, 32)));
        obj1.addComponent(new SpriteRenderer(spriteSheet.getSprite(0))); // index is which sprite from the spritesheet we want loaded
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(200, 100),
                new Vector2f(32, 32)));
        obj2.addComponent(new SpriteRenderer(spriteSheet.getSprite(2))); // index is which sprite from the spritesheet we want loaded
        this.addGameObjectToScene(obj2);

    }


    @Override
    public void update(float dt)
    {
        this.obj1.transform.position.x += 10 * dt;

        for (GameObject go_obj : this.gameObjects) go_obj.update(dt);

        this.renderer.render();
    }


    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));

        AssetPool.getTexture("assets/images/slav superstar.png");

    }
}
