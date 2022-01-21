package ECS;

import engine.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{

    // For global ID system
    private static int ID_COUNTER = 0;
    public int uid = -1;

    // Variables
    private String name;
    private List<Component> Components_li;
    public Transform transform;

    // Every game object will now have a z-index
    private int z_index;


    // Constructor
    public GameObject(String name, Transform transform, int z_index)
    {
        this.name = name;
        this.Components_li = new ArrayList<>();
        this.transform = transform;
        this.z_index = z_index;
        this.uid = ID_COUNTER++;
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
        // Every time we add a component, we generate an ID
        cmp.generateID();

        this.Components_li.add(cmp);
        cmp.gameObject = this;
    }

    public void updateComponentsInObjectImgui() {for (Component cmp : Components_li) cmp.updateComponentImgui();}
    public void update(float dt)
    {
        for(Component cmp : Components_li) cmp.update(dt);
    }
    public void start()
    {
        for(Component cmp : Components_li) cmp.start();
    }
    public int getZ_index(){return this.z_index;}

    // For ID system
    public void generateID() {if (this.uid == -1) {this.uid = ID_COUNTER++;}}
    public int getUid() {return this.uid;}
    public static void init(int maxID) {ID_COUNTER = maxID;}
    public List<Component> getAllComponents() {return this.Components_li;}
}
