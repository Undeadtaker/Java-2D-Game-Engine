package ECS;

public abstract class Component
{
    public transient GameObject gameObject = null;

    public abstract void update(float dt);
    public void start() {}
    public void updateComponentImgui() {}

}
