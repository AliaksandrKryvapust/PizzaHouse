package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.validator.IoC.OrderValidatorSingleton;
import groupId.artifactId.controller.validator.api.IOrderValidator;
import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutPut;
import groupId.artifactId.exceptions.IncorrectOrderInputException;
import groupId.artifactId.service.IoC.MenuItemServiceSingleton;
import groupId.artifactId.service.IoC.OrderServiceSingleton;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.service.api.IOrderService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Order", urlPatterns = "/api/order")
public class ApiOrderServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_VERSION = "version";
    private static final String PARAMETER_DELETE = "delete";
    private final IOrderService orderService = OrderServiceSingleton.getInstance();
    private final IMenuItemService menuItemService = MenuItemServiceSingleton.getInstance();
    private final IOrderValidator orderValidator = OrderValidatorSingleton.getInstance();

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 1)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(CONTENT_TYPE);
            resp.setCharacterEncoding(ENCODING);
            String id = req.getParameter(PARAMETER_ID);
            if (id != null) {
                if (orderService.isTicketIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromTicketToJson(orderService.get(Long.valueOf(id))));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.getWriter().write(JsonConverter.fromTicketListToJson(orderService.get()));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //CREATE POSITION
    //body json
//    {
//        "selectedItems": [
//        {
//            "menuItemId": 90,
//                "count": 2
//        }
//    ]
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(CONTENT_TYPE);
            OrderDtoInput order = JsonConverter.fromJsonToOrder(req.getInputStream());
            try {
                orderValidator.validate(order);
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            for (SelectedItemDtoInput input : order.getSelectedItems()) {
                if (!menuItemService.isIdValid(input.getMenuItemId())) {
                    throw new IncorrectOrderInputException("Such menu item id do not exist");
                }
            }
            TicketDtoOutPut outPut = orderService.save(order);
            resp.getWriter().write(JsonConverter.fromTicketToJson(outPut));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IncorrectOrderInputException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE POSITION
    //need param id  (id = 1)
    //need param version/date_update - optimistic lock (version=1)
    //param delete - true/false completely delete (delete=false)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(CONTENT_TYPE);
            String id = req.getParameter(PARAMETER_ID);
            String version = req.getParameter(PARAMETER_VERSION);
            String delete = req.getParameter(PARAMETER_DELETE);
            if (id != null && version != null && delete != null) {
                if (orderService.isTicketIdValid(Long.valueOf(id))) {
                    orderService.delete(id, version, delete);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}