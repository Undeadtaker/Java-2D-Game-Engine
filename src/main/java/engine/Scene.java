package engine;

import ECS.Component;
import ECS.GameObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import renderer.Renderer;
import util.Serializers.ComponentDeserializer;
import util.Serializers.GameObjectDeserializer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{

    // Only pass camera Class to inherited classes of Scene class.
    protected Camera camera;
    protected Renderer renderer = new Renderer();
    private boolean b_isRunning = false;
    protected boolean b_levelLoaded = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;

    public Scene() {}
    public abstract void update(float dt);
    public void init() {}
    public Camera getCamera() { return this.camera; }
    public void updateSceneImgui() {}


    public void start()
    {
        for(GameObject go_obj : gameObjects)
        {
            go_obj.start();
            this.renderer.add(go_obj);
        }
        b_isRunning = true;
    }

    public void addGameObjectToScene(GameObject go_obj)
    {
        if(!b_isRunning) gameObjects.add(go_obj);

        else
        {
            gameObjects.add(go_obj);
            go_obj.start();
            this.renderer.add(go_obj);
        }

    }

    public void sceneImgui()
    {
        if (activeGameObject != null)
        {
            ImGui.begin("Inspector");
            activeGameObject.updateComponentsInObjectImgui();
            ImGui.end();
        }

        updateSceneImgui();
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // SERIALIZATION

    public void load()
    {
        // Now the registered serializer/deserializer for our Component class is the Component
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try
        {
            inFile = new String(Files.readAllBytes(Paths.get("levelEditorSave.json")));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        if(!inFile.equals(""))
        {
            GameObject[] GObjs = gson.fromJson(inFile, GameObject[].class);
            for(GameObject obj : GObjs)
            {
                this.addGameObjectToScene(obj);
            }
        }

        this.b_levelLoaded = true;

    }

    public void saveOnExit()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try
        {
            FileWriter writer = new FileWriter("levelEditorSave.json");

            // Just write all the gameObjects in this Scene, easy life.
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }



}
