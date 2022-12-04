package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.config.AppContext;
import groupId.artifactId.controller.utils.JsonConverter;
import groupId.artifactId.controller.validator.OrderDataValidator;
import groupId.artifactId.controller.validator.api.IOrderDataValidator;
import groupId.artifactId.core.Constants;
import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.dao.entity.api.ITicket;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.service.OrderDataService;
import groupId.artifactId.service.OrderService;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.service.api.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "OrderData", urlPatterns = "/api/order_data")
public class ApiOrderDataServlet extends HttpServlet {
    private final IOrderDataService orderDataService;
    private final IOrderService orderService;
    private final IOrderDataValidator orderDataValidator;
    private final Logger logger;
    private final JsonConverter jsonConverter;

    public ApiOrderDataServlet() {
        AnnotationConfigApplicationContext context = AppContext.getContext();
        this.orderDataService = context.getBean(OrderDataService.class);
        this.orderDataValidator = context.getBean(OrderDataValidator.class);
        this.orderService = context.getBean(OrderService.class);
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.jsonConverter = context.getBean(JsonConverter.class);
    }

    //Read POSITION
    //1) Read list
    //2) Read item need ticket id param  (id = 1)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                resp.getWriter().write(jsonConverter.fromOrderDataToJson(orderDataService.getAllData(Long.valueOf(id))));
            } else {
                resp.getWriter().write(jsonConverter.fromOrderDataListToJson(orderDataService.get()));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            logger.error("/api/order_data there is no content to fulfill doGet method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/order_data crashed during doGet method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }

    //CREATE POSITION
    //body json
//    {
//        "done": false,
//            "ticket_id": 1,
//            "description": "cooking"
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            OrderDataDtoInput orderDataInput = jsonConverter.fromJsonToOrderData(req.getInputStream());
            try {
                orderDataValidator.validate(orderDataInput);
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                logger.error("/api/order_data input is not valid " + e.getMessage() + "\t" + e.getCause() +
                        "\tresponse status: " + resp.getStatus());
            }
            ITicket ticket = this.orderService.getRow(orderDataInput.getTicketId());
            OrderDataDtoInput orderData = OrderDataDtoInput.builder().ticketId(orderDataInput.getTicketId())
                    .description(orderDataInput.getDescription()).ticket(ticket).build();
            OrderDataDtoCrudOutput output = orderDataService.save(orderData);
            resp.getWriter().write(jsonConverter.fromOrderDataCrudToJson(output));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("/api/order_data there is no content to fulfill doPost method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/order_data crashed during doPost method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }
}
