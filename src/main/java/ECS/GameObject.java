package ECS;

import engine.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    // Variables
    private String name;
    private List<Component> Components_li;
    public Transform transform;


    // Constructor
    public GameObject(String name, Transform transform)
    {
        this.name = name;
        this.Components_li = new ArrayList<>();
        this.transform = transform;
    }

    public GameObject(String name)
    {
        // Overload the previous constructor
        this(name, new Transform());
    }

    // Methods
    public <Type extends Component> Type getComponent(Class<Type> componentClass)
    {
        for(Component cmp : Components_li)
        {
            if(componentClass.isAssignableFrom(cmp.getClass()))
            {
                // Probably will never happen due to the fact that it firstly checks if
                // the componentClass is a subclass of component in the component list
                try
                {
                    return componentClass.cast(cmp);
                }
                catch(ClassCastException e)
                {
                    e.printStackTrace();
                    assert false: "Error: casting Component failed.";
                }
            }
        }
        return null;
    }

    public <Type extends Component> void removeComponent(Class<Type> componentClass)
    {
        for(int i = 0; i < Components_li.size(); i++)
        {
            Component cmp = Components_li.get(i);
            if(componentClass.isAssignableFrom(cmp.getClass()))
            {
                try
                {
                    Components_li.remove(i);
                    return;
                }
                catch(ClassCastException e)
                {
                    e.printStackTrace();
                    assert false: "Error: could not remove Component.";
                }
            }
        }
    }

    public void addComponent(Component cmp)
    {
        this.Components_li.add(cmp);
        cmp.gameObject = this;
    }

    public void update(float dt)
    {
        for(Component cmp : Components_li) cmp.update(dt);
    }

    public void start()
    {
        for(Component cmp : Components_li) cmp.start();
    }

}
