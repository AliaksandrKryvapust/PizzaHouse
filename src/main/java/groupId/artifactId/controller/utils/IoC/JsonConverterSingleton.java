package groupId.artifactId.controller.utils.IoC;

import groupId.artifactId.controller.utils.JsonConverter;
import groupId.artifactId.controller.utils.api.JsonCreator;

import java.beans.PropertyVetoException;

public class JsonConverterSingleton {

    private final JsonConverter jsonConverter;
    private volatile static JsonConverterSingleton instance = null;

    public JsonConverterSingleton() {
        try {
            this.jsonConverter = new JsonConverter(JsonCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Failed to get Object mapper at Json Converter class");
        }
    }

    public static JsonConverter getInstance() {
        synchronized (JsonConverterSingleton.class) {
            if (instance == null) {
                instance = new JsonConverterSingleton();
            }
            return instance.jsonConverter;
        }
    }
}
