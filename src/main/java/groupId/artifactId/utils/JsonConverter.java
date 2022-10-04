package groupId.artifactId.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupId.artifactId.storage.entity.api.IMenuItem;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonConverter {
//    public static String toJson(List<Product> product) throws JsonProcessingException {
//        return new ObjectMapper().writeValueAsString(product);
//    }
    public static List<IMenuItem> fromJson(ServletInputStream servletInputStream) throws IOException {
        return Arrays.asList(new ObjectMapper().readValue(servletInputStream, IMenuItem[].class));
    }
}
