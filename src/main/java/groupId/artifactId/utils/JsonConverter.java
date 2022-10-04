package groupId.artifactId.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.exceptions.IncorrectJsonParseException;
import groupId.artifactId.storage.entity.api.IMenu;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.List;

public class JsonConverter {
    public static String toJson(List<IMenu> menu) {
        try {
            return new ObjectMapper().writeValueAsString(menu);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write IMenu as json",e);
        }
    }
    public static List<MenuItemDto> fromJsonToList(ServletInputStream servletInputStream)  {
        try {
            return new ObjectMapper().readValue(servletInputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of MenuItemDto[].class",e);
        }
    }
    public static MenuItemDtoWithId fromJsonToItemWithId(ServletInputStream servletInputStream)  {
        try {
            return new ObjectMapper().readValue(servletInputStream, MenuItemDtoWithId.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of MenuItemDtoWithId.class",e);
        }
    }
}
