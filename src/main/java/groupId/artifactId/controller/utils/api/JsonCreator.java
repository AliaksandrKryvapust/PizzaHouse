package groupId.artifactId.controller.utils.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.beans.PropertyVetoException;

public class JsonCreator {
    private static JsonCreator firstInstance = null;
    private final ObjectMapper mapper;

    private JsonCreator() {
        this.mapper = new ObjectMapper(new JsonFactory());
        mapper.registerModule(new JavaTimeModule());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static ObjectMapper getInstance() throws PropertyVetoException {
        synchronized (JsonCreator.class) {
            if (firstInstance == null) {
                firstInstance = new JsonCreator();
            }
        }
        return firstInstance.mapper;
    }
}
