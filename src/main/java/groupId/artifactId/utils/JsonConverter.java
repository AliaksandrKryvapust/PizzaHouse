package groupId.artifactId.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groupId.artifactId.core.dto.*;
import groupId.artifactId.exceptions.IncorrectJsonParseException;
import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.storage.entity.api.IToken;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.List;

public class JsonConverter {
    public static String fromMenuToJson(List<IMenu> menu) {
        try {
            return new ObjectMapper().writeValueAsString(menu);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write IMenu as json",e);
        }
    }
    public static String fromTokenToJson(IToken token) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(token);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write IToken as json",e);
        }
    }
    public static List<MenuItemDto> fromJsonToMenu(ServletInputStream servletInputStream)  {
        try {
            return new ObjectMapper().readValue(servletInputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of MenuItemDto[].class",e);
        }
    }
    public static MenuItemDtoWithId fromJsonToMenuWithId(ServletInputStream servletInputStream)  {
        try {
            return new ObjectMapper().readValue(servletInputStream, MenuItemDtoWithId.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of MenuItemDtoWithId.class",e);
        }
    }
    public static OrderDto fromJsonToOrder(ServletInputStream servletInputStream)  {
        try {
            return new ObjectMapper().readValue(servletInputStream, OrderDto.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of OrderDto.class",e);
        }
    }
    public static TokenDto fromJsonToToken(ServletInputStream servletInputStream)  {
        try {
            return new ObjectMapper().readValue(servletInputStream, TokenDto.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of TokenDto.class",e);
        }
    }
    public static OrderStageDtoWithId fromJsonToOrderStageWithId(ServletInputStream servletInputStream)  {
        try {
            return new ObjectMapper().readValue(servletInputStream, OrderStageDtoWithId.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of OrderStageDtoWithId.class",e);
        }
    }
}
