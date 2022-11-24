package groupId.artifactId.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import groupId.artifactId.core.dto.input.*;
import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.*;
import groupId.artifactId.exceptions.IncorrectJsonParseException;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.List;

public class JsonConverter {
    private final ObjectMapper mapper;

    public JsonConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String fromMenuListToJson(List<MenuDtoCrudOutput> menu) {
        try {
            return mapper.writeValueAsString(menu);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("Failed to write MenuDtoOutput:" + menu.toString() + "as json", e);
        }

    }

    public String fromMenuToCrudJson(MenuDtoCrudOutput menu) {
        try {
            return mapper.writeValueAsString(menu);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("Failed to write MenuDtoCrudOutput:" + menu.toString() + "as json", e);
        }
    }

    public String fromMenuToJson(MenuDtoOutput menu) {
        try {
            return mapper.writeValueAsString(menu);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("Failed to write MenuDtoOutput:" + menu.toString() + "as json", e);
        }
    }

    public String fromMenuItemListToJson(List<MenuItemDtoOutput> items) {
        try {
            return mapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write List MenuItemDtoOutput as json", e);
        }
    }

    public String fromMenuItemToJson(MenuItemDtoOutput item) {
        try {
            return mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write MenuItemDtoOutput as json", e);
        }
    }

    public String fromTicketCrudToJson(TicketDtoCrudOutput ticket) {
        try {
            return mapper.writeValueAsString(ticket);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write TicketDtoCrudOutput as json", e);
        }
    }

    public String fromTicketToJson(TicketDtoOutput ticket) {
        try {
            return mapper.writeValueAsString(ticket);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write TicketDtoOutput as json", e);
        }
    }

    public String fromTicketListToJson(List<TicketDtoCrudOutput> ticket) {
        try {
            return mapper.writeValueAsString(ticket);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write List of TicketDtoCrudOutput as json", e);
        }
    }

    public String fromOrderDataCrudToJson(OrderDataDtoCrudOutput output) {
        try {
            return mapper.writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write OrderDataDtoCrudOutput as json", e);
        }
    }

    public String fromOrderDataToJson(OrderDataDtoOutput output) {
        try {
            return mapper.writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write OrderDataDtoOutput as json", e);
        }
    }

    public String fromOrderDataListToJson(List<OrderDataDtoCrudOutput> output) {
        try {
            return mapper.writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write List of OrderDataDtoCrudOutput as json", e);
        }
    }

    public String fromCompletedOrderCrudToJson(CompletedOrderDtoCrudOutput output) {
        try {
            return mapper.writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write CompletedOrderDtoCrudOutput as json", e);
        }
    }

    public String fromCompletedOrderToJson(CompletedOrderDtoOutput output) {
        try {
            return mapper.writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write CompletedOrderDtoOutput as json", e);
        }
    }

    public String fromCompletedOrderListToJson(List<CompletedOrderDtoCrudOutput> output) {
        try {
            return mapper.writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonParseException("failed to write List of CompletedOrderDtoCrudOutput as json", e);
        }
    }

    public MenuDtoInput fromJsonToMenu(ServletInputStream servletInputStream) {
        try {
            return mapper.readValue(servletInputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of MenuDtoOutput.class", e);
        }
    }

    public MenuItemDtoInput fromJsonToMenuItem(ServletInputStream servletInputStream) {
        try {
            return mapper.readValue(servletInputStream, MenuItemDtoInput.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of MenuItemDtoOutput.class", e);
        }
    }

    public OrderDtoInput fromJsonToOrder(ServletInputStream servletInputStream) {
        try {
            return mapper.readValue(servletInputStream, OrderDtoInput.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of OrderDtoInput.class", e);
        }
    }

    public OrderDataDtoInput fromJsonToOrderData(ServletInputStream servletInputStream) {
        try {
            return mapper.readValue(servletInputStream, OrderDataDtoInput.class);
        } catch (IOException e) {
            throw new IncorrectJsonParseException("failed to read servletInputStream of OrderDataDtoInput.class", e);
        }
    }
}
