package groupId.artifactId.controller.utils.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.beans.PropertyVetoException;

public class JsonCreator {
    private static JsonCreator firstInstance = null;
    private final ObjectMapper mapper;

    private JsonCreator() {
        this.mapper = new ObjectMapper(new JsonFactory());
        mapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
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
