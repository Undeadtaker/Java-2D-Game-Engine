package util.Serializers;

import ECS.Component;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonSerializer<Component>, JsonDeserializer<Component>
{

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        JsonObject loadedResult = json.getAsJsonObject();
        String type = loadedResult.get("type").getAsString();
        JsonElement element = loadedResult.get("properties");

        // The following try/catch is trying to copy the functionality of
        // gson.fromJson(serialized, SpriteRenderer.class);
        // which is saying deserialize the context for this class type.
        // If you're looking to expand or upgrade this, visit the gson documentation

        try
        {
            return context.deserialize(element, Class.forName(type));
        }
        catch (ClassNotFoundException e)
        {
            throw new JsonParseException("Unknown element type: " + type, e);
        }

    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }
}
