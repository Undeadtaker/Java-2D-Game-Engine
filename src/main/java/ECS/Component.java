package ECS;

import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component
{

    // Global ID for every Component rendered from serializing. Is used to avoid
    // creating phantom objects, basically a duplicate of an object that is
    // not useful to us in any way. The uid is the unique ID of every Component object.
    public static int ID_COUNTER = 0;
    private int uid = -1;

    public transient GameObject gameObject = null;



    public void updateComponentImgui()
    {
        try
        {
            // Doesn't return the fields of the Component class, rather the fields of the
            // class derived from the Component Class
            Field[] fields_li = this.getClass().getDeclaredFields();
            for(Field field : fields_li)
            {

                // Ignore transient variables
                boolean b_isTransient = Modifier.isTransient(field.getModifiers());
                if (b_isTransient) {continue;}

                // Check if modifier is private, temporarily change to public
                boolean b_isPrivate = Modifier.isPrivate(field.getModifiers());
                if (b_isPrivate) {field.setAccessible(true);
                }
                // We get all the information we need to dynamically display
                // input for the basic derived components.
                Class<?> Type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (Type == int.class) {
                    int val = (int)value;
                    int[] imInt = {val};
                    if (ImGui.dragInt(name + ": ", imInt)) {
                        field.set(this, imInt[0]);
                    }
                } else if (Type == float.class) {
                    float val = (float)value;
                    float[] imFloat = {val};
                    if (ImGui.dragFloat(name + ": ", imFloat)) {
                        field.set(this, imFloat[0]);
                    }
                } else if (Type == boolean.class) {
                    boolean val = (boolean)value;
                    if (ImGui.checkbox(name + ": ", val)) {
                        field.set(this, !val);
                    }
                } else if (Type == Vector3f.class) {
                    Vector3f val = (Vector3f)value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                } else if (Type == Vector4f.class) {
                    Vector4f val = (Vector4f)value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                }

                // Change field back from public to private
                if(b_isPrivate) { field.setAccessible(false);}

            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    // The first ID is always going to be 0 since uid is initialized at -1
    public void generateID() {if (this.uid == -1) {this.uid = ID_COUNTER++;}}
    public int getUid() {return this.uid;}
    public static void init(int maxID) {ID_COUNTER = maxID;}

    public void update(float dt) {};
    public void start() {}

}
