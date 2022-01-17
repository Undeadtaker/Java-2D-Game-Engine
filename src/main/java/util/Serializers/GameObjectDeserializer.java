package util.Serializers;

import ECS.Component;
import ECS.GameObject;
import com.google.gson.*;
import engine.Transform;

import java.lang.reflect.Type;



public class GameObjectDeserializer implements JsonDeserializer<GameObject>
{
    // Serialization of GameObject, we know we need to deserialize name, Components_li, transform, z_index
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();

        // Get name, Components_li, transform, z_index
        String name = jsonObject.get("name").getAsString();
        JsonArray Components_li = jsonObject.getAsJsonArray("Components_li");
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int z_index = context.deserialize(jsonObject.get("z_index"), Integer.class);

        // Now we construct the game Object
        GameObject GObj = new GameObject(name, transform, z_index);

        // Add back all components
        for(JsonElement cmp : Components_li)
        {
            // Deserialize the component using our custom component deserializer
            Component component = context.deserialize(cmp, Component.class);
            GObj.addComponent(component);
        }

        return GObj;

    }

}

