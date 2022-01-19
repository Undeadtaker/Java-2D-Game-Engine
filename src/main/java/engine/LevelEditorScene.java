package engine;

import ECS.Components.RigidBody;
import ECS.Components.SpriteRenderer;
import ECS.Components.SpriteSheet;
import ECS.GameObject;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;


public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private SpriteRenderer obj1Sprite;
    private RigidBody rigid = new RigidBody();

    // Constructor + other methods
    public LevelEditorScene() {}

    @Override
    public void init()
    {
        this.loadResources();
        this.camera = new Camera(new Vector2f(0, 0));

        // Load gameObjects from json
        if(b_levelLoaded)
        {
            this.activeGameObject = gameObjects.get(0);

            return;
        }

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/images/spritesheet.png");


        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100),
                new Vector2f(20, 20)), 2);

        System.out.print(rigid.toString());

        obj1.addComponent(rigid);


        obj1Sprite = new SpriteRenderer();
        obj1Sprite.setColor(new Vector4f(1, 0, 0, .5f));
        obj1.addComponent(obj1Sprite);
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(600, 100),
                new Vector2f(256, 256)), 3);

        SpriteRenderer obj2Sprite = new SpriteRenderer();
        obj2Sprite.setColor(new Vector4f(0, 1, 0, .5f));
        obj2.addComponent(obj2Sprite);
        this.addGameObjectToScene(obj2);

    }


    @Override
    public void update(float dt)
    {
        for (GameObject go_obj : this.gameObjects) go_obj.update(dt);
        this.renderer.render();
    }

    @Override
    public void updateSceneImgui()
    {
        ImGui.begin("Test window");
        ImGui.text("Some random text");
        ImGui.end();
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
