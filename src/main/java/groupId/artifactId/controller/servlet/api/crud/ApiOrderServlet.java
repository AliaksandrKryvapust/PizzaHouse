package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.utils.IoC.JsonConverterSingleton;
import groupId.artifactId.controller.utils.JsonConverter;
import groupId.artifactId.controller.validator.IoC.OrderValidatorSingleton;
import groupId.artifactId.controller.validator.api.IOrderValidator;
import groupId.artifactId.core.Constants;
import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.exceptions.IncorrectOrderInputException;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.service.IoC.MenuItemServiceSingleton;
import groupId.artifactId.service.IoC.OrderServiceSingleton;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.service.api.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Order", urlPatterns = "/api/order")
public class ApiOrderServlet extends HttpServlet {
    private final IOrderService orderService = OrderServiceSingleton.getInstance();
    private final IMenuItemService menuItemService = MenuItemServiceSingleton.getInstance();
    private final IOrderValidator orderValidator = OrderValidatorSingleton.getInstance();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JsonConverter jsonConverter = JsonConverterSingleton.getInstance();

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 1)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(Constants.CONTENT_TYPE);
            resp.setCharacterEncoding(Constants.ENCODING);
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                if (orderService.isTicketIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(jsonConverter.fromTicketCrudToJson(orderService.get(Long.valueOf(id))));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.getWriter().write(jsonConverter.fromTicketListToJson(orderService.get()));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/order crashed during doGet method" + e.getMessage() + resp.getStatus());
        }
    }

    //CREATE POSITION
    //body json
//    {
//        "selectedItems": [
//        {
//            "menu_item_id": 90,
//                "count": 2
//        }
//    ]
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            OrderDtoInput order = jsonConverter.fromJsonToOrder(req.getInputStream());
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
            TicketDtoCrudOutput outPut = orderService.save(order);
            resp.getWriter().write(jsonConverter.fromTicketCrudToJson(outPut));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IncorrectOrderInputException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            logger.error("/api/order crashed during doPost method menu item id do not exist" + e.getMessage() + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/order crashed during doPost method" + e.getMessage() + resp.getStatus());
        }
    }

    //DELETE POSITION
    //need param id  (id = 1)
    //need param version/date_update - optimistic lock (version=1)
    //param delete - true/false completely delete (delete=false)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            String id = req.getParameter(Constants.PARAMETER_ID);
            String version = req.getParameter(Constants.PARAMETER_VERSION);
            String delete = req.getParameter(Constants.PARAMETER_DELETE);
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
        } catch (OptimisticLockException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            logger.error("/api/order optimistic lock during doDelete method" + e.getMessage() + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/order crashed during doDelete method" + e.getMessage() + resp.getStatus());
        }
    }
}
