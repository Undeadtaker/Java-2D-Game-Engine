package ECS;

import imgui.ImGui;

import java.lang.reflect.Field;

public abstract class Component
{
    public transient GameObject gameObject = null;

    public abstract void update(float dt);
    public void start() {}


    public void updateComponentImgui()
    {
        try
        {
            // Doesn't return the fields of the Component class, rather the fields of the
            // class derived from the Component Class
            Field[] fields_li = this.getClass().getDeclaredFields();
            for(Field field : fields_li)
            {
                // We get all the information we need to dynamically display
                // input for the basic derived components.
                Class<?> Type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(Type == int.class)
                {
                    int val = (int) value;
                    int[] imInt = {val};

                    if(ImGui.dragInt(name + ": ", imInt))
                    {
                        field.set(this, imInt[0]);
                    }
                }

            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

}
