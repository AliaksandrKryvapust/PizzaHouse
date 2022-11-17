package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.utils.IoC.JsonConverterSingleton;
import groupId.artifactId.controller.utils.JsonConverter;
import groupId.artifactId.controller.validator.IoC.OrderDataValidatorSingleton;
import groupId.artifactId.controller.validator.api.IOrderDataValidator;
import groupId.artifactId.core.Constants;
import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.service.IoC.OrderDataServiceSingleton;
import groupId.artifactId.service.api.IOrderDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "OrderData", urlPatterns = "/api/order_data")
public class ApiOrderDataServlet extends HttpServlet {
    private final IOrderDataService orderDataService;
    private final IOrderDataValidator orderDataValidator;
    private final Logger logger;
    private final JsonConverter jsonConverter;

    public ApiOrderDataServlet() {
        this.orderDataService = OrderDataServiceSingleton.getInstance();
        this.orderDataValidator = OrderDataValidatorSingleton.getInstance();
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.jsonConverter = JsonConverterSingleton.getInstance();
    }

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
                resp.getWriter().write(jsonConverter.fromOrderDataCrudToJson(orderDataService.get(Long.valueOf(id))));
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
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            OrderDataDtoInput orderData = jsonConverter.fromJsonToOrderData(req.getInputStream());
            try {
                orderDataValidator.validate(orderData);
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                logger.error("/api/order_data input is not valid " + e.getMessage() + "\t" + e.getCause() +
                        "\tresponse status: " + resp.getStatus());
            }
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

    //UPDATE POSITION
    //need param id  (id = 3)
    //need param version/date_update - optimistic lock (version=12)
    //body json
//    {
//        "done": true,
//            "ticket_id": 1,
//            "description": "ready"
//    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            String id = req.getParameter(Constants.PARAMETER_ID);
            String version = req.getParameter(Constants.PARAMETER_VERSION);
            if (id != null && version != null) {
                OrderDataDtoInput orderData = jsonConverter.fromJsonToOrderData(req.getInputStream());
                try {
                    orderDataValidator.validate(orderData);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error("/api/order_data input is not valid " + e.getMessage() + "\t" + e.getCause() +
                            "\tresponse status: " + resp.getStatus());
                }
                OrderDataDtoCrudOutput output = orderDataService.update(orderData, id, version);
                resp.getWriter().write(jsonConverter.fromOrderDataCrudToJson(output));
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("/api/order_data there is no content to fulfill doPut method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (OptimisticLockException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            logger.error("/api/order_data optimistic lock during doPost method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/order_data crashed during doPut method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }
}
