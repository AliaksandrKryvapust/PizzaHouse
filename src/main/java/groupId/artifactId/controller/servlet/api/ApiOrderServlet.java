package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.controller.utils.IoC.JsonConverterSingleton;
import groupId.artifactId.controller.utils.JsonConverter;
import groupId.artifactId.controller.validator.IoC.OrderValidatorSingleton;
import groupId.artifactId.controller.validator.api.IOrderValidator;
import groupId.artifactId.core.Constants;
import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.service.IoC.OrderServiceSingleton;
import groupId.artifactId.service.api.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Order", urlPatterns = "/api/order")
public class ApiOrderServlet extends HttpServlet {
    private final IOrderService orderService;
    private final IOrderValidator orderValidator;
    private final Logger logger;
    private final JsonConverter jsonConverter;

    public ApiOrderServlet() {
        this.orderService = OrderServiceSingleton.getInstance();
        this.orderValidator = OrderValidatorSingleton.getInstance();
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.jsonConverter = JsonConverterSingleton.getInstance();
    }

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 1)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                resp.getWriter().write(jsonConverter.fromTicketToJson(orderService.getAllData(Long.valueOf(id))));
            } else {
                resp.getWriter().write(jsonConverter.fromTicketListToJson(orderService.get()));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            logger.error("/api/order there is no content to fulfill doGet method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/order crashed during doGet method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }

    //CREATE POSITION
    //body json
//    {
//        "selected_items": [
//        {
//            "menu_item_id": 90,
//                "count": 2
//        }
//    ]
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            OrderDtoInput order = jsonConverter.fromJsonToOrder(req.getInputStream());
            try {
                orderValidator.validate(order);
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                logger.error("/api/order input is not valid " + e.getMessage() + "\t" + e.getCause() +
                        "\tresponse status: " + resp.getStatus());
            }
            TicketDtoCrudOutput outPut = orderService.save(order);
            resp.getWriter().write(jsonConverter.fromTicketCrudToJson(outPut));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("/api/order there is no content to fulfill doPost method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/order crashed during doPost method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }
}
