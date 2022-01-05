package engine;

import ECS.GameObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{

    // Only pass camera Class to inherited classes of Scene class.
    protected Camera camera;
    private boolean b_isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene()
    {

    }

    public abstract void update(float dt);
    public void init() {}


    public void start()
    {
        for(GameObject go_obj : gameObjects)
        {
            go_obj.start();
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
        }

    }
}
