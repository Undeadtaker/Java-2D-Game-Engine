package engine;

import ECS.Components.RigidBody;
import ECS.Components.Sprite;
import ECS.Components.SpriteRenderer;
import ECS.Components.SpriteSheet;
import ECS.GameObject;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;


public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private SpriteRenderer obj1Sprite;
    private RigidBody rigid = new RigidBody();
    private SpriteSheet spriteSheet;

    // Constructor + other methods
    public LevelEditorScene() {}

    @Override
    public void init()
    {
        this.loadResources();
        this.camera = new Camera(new Vector2f(0, 0));

        spriteSheet = AssetPool.getSpriteSheet("assets/images/decorationsAndBlocks.png");

        // Load gameObjects from json
        if(b_levelLoaded)
        {
            this.activeGameObject = gameObjects.get(0);

            return;
        }


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
        ImGui.begin("Sprite Window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;

        for(int i = 0; i < spriteSheet.size(); i++)
        {
            Sprite sprite = spriteSheet.getSprite(i);
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int texId = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            // The way ImGui recognizes which button is clicked is through ID. Since all the
            // sprites come from the same texture, they will all have the same ID. No matter what button
            // we click it will always display one number. Thats why we assign a new ID to the sprites
            // rendered from the spriteSheet
            ImGui.pushID(i);
            if(ImGui.imageButton(texId, spriteWidth, spriteHeight,
                    texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y))
            {
                System.out.println("Button " + i + " clicked!");
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            if(i + 1 < spriteSheet.size() && nextButtonX2 < windowX2)
            {
                ImGui.sameLine();
            }


        }

        ImGui.end();
    }


    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/decorationsAndBlocks.png"),
                        16, 16, 81, 0));

        AssetPool.getTexture("assets/images/slav superstar.png");

    }
}
