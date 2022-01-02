package engine;

public abstract class Scene
{

    // Only pass camera Class to inherited classes of Scene class.
    protected Camera camera;

    public Scene()
    {

    }

    public abstract void update(float dt);

    public void init()
    {

    }

}
