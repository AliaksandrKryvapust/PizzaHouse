package groupId.artifactId.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.exceptions.IncorrectJsonParseException;
import groupId.artifactId.storage.entity.api.IMenuItem;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonConverter {
    public static String toJson(List<Product> product) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(product);
    }
    public static List<MenuItemDto> fromJson(ServletInputStream servletInputStream)  {
        try {
            return Arrays.asList(new ObjectMapper().readValue(servletInputStream, MenuItemDto[].class));
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of MenuItemDto[].class",e);
        }
    }
}
