package groupId.artifactId.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groupId.artifactId.core.dto.input.*;
import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.exceptions.IncorrectJsonParseException;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.List;

public class JsonConverter {
    public static String fromMenuListToJson(List<MenuDtoOutput> menu) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(menu);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("Failed to write MenuDtoOutput:"+ menu.toString() + "as json", e);
        }
    }

    public static String fromMenuToJson(MenuDtoOutput menu) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(menu);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("Failed to write MenuDtoOutput:"+ menu.toString() + "as json", e);
        }
    }

    public static String fromMenuItemListToJson(List<MenuItemDtoOutput> items) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(items);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write List MenuItemDtoOutput as json", e);
        }
    }

    public static String fromMenuItemToJson(MenuItemDtoOutput item) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(item);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write MenuItemDtoOutput as json", e);
        }
    }

    public static String fromPizzaInfoListToJson(List<PizzaInfoDtoOutput> items) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(items);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write List PizzaInfoDtoOutput as json", e);
        }
    }

    public static String fromPizzaInfoToJson(PizzaInfoDtoOutput pizzaInfo) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(pizzaInfo);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write PizzaInfoDtoOutput as json", e);
        }
    }

    public static String fromTicketToJson(TicketDtoOutPut ticket) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ticket);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write TicketDtoOutPut as json", e);
        }
    }

    public static String fromTicketListToJson(List<TicketDtoOutPut> ticket) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ticket);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write List of TicketDtoOutPut as json", e);
        }
    }
    public static String fromOrderDataToJson(OrderDataDtoOutput output) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write OrderDataDtoOutput as json", e);
        }
    }

    public static String fromOrderDataListToJson(List<OrderDataDtoOutput> output) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write List of OrderDataDtoOutput as json", e);
        }
    }

    public static String fromCompletedOrderToJson(CompletedOrderDtoOutput output) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write CompletedOrderDtoOutput as json", e);
        }
    }

    public static String fromCompletedOrderListToJson(List<CompletedOrderDtoOutput> output) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write List of CompletedOrderDtoOutput as json", e);
        }
    }

    public static MenuDtoInput fromJsonToMenu(ServletInputStream servletInputStream) {
        try {
            return new ObjectMapper().readValue(servletInputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of MenuDtoOutput.class", e);
        }
    }

    public static MenuItemDtoInput fromJsonToMenuItem(ServletInputStream servletInputStream) {
        try {
            return new ObjectMapper().readValue(servletInputStream, MenuItemDtoInput.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of MenuItemDtoOutput.class", e);
        }
    }

    public static PizzaInfoDtoInput fromJsonToPizzaInfo(ServletInputStream servletInputStream) {
        try {
            return new ObjectMapper().readValue(servletInputStream, PizzaInfoDtoInput.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of PizzaInfoDtoInput.class", e);
        }
    }

    public static OrderDtoInput fromJsonToOrder(ServletInputStream servletInputStream) {
        try {
            return new ObjectMapper().readValue(servletInputStream, OrderDtoInput.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of OrderDtoInput.class", e);
        }
    }

    public static OrderDataDtoInput fromJsonToOrderData(ServletInputStream servletInputStream) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).readValue(servletInputStream, OrderDataDtoInput.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of OrderDataDtoInput.class",e);
        }
    }
}
