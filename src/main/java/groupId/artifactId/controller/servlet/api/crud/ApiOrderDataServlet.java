package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.validator.IoC.OrderDataValidatorSingleton;
import groupId.artifactId.controller.validator.api.IOrderDataValidator;
import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.service.IoC.OrderDataServiceSingleton;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "OrderData", urlPatterns = "/api/order_data")
public class ApiOrderDataServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_VERSION = "version";
    private final IOrderDataService orderDataService = OrderDataServiceSingleton.getInstance();
    private final IOrderDataValidator orderDataValidator = OrderDataValidatorSingleton.getInstance();

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
                if (orderDataService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromOrderDataToJson(orderDataService.get(Long.valueOf(id))));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.getWriter().write(JsonConverter.fromOrderDataListToJson(orderDataService.get()));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //CREATE POSITION
    //body json
//    {
//        "done": false,
//            "ticketId": 1,
//            "description": "cooking"
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(CONTENT_TYPE);
            OrderDataDtoInput orderData = JsonConverter.fromJsonToOrderData(req.getInputStream());
            try {
                orderDataValidator.validate(orderData);
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            OrderDataDtoOutput output = orderDataService.save(orderData);
            resp.getWriter().write(JsonConverter.fromOrderDataToJson(output));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //UPDATE POSITION
    //need param id  (id = 3)
    //need param version/date_update - optimistic lock (version=4)
    //body json
//   {
//           "done":true,
//           "ticketId":1,
//           "description":"ready"
//           }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(CONTENT_TYPE);
            String id = req.getParameter(PARAMETER_ID);
            String version = req.getParameter(PARAMETER_VERSION);
            if (id != null && version != null) {
                if (orderDataService.isIdValid(Long.valueOf(id))) {
                    OrderDataDtoInput orderData = JsonConverter.fromJsonToOrderData(req.getInputStream());
                    try {
                        orderDataValidator.validate(orderData);
                    } catch (IllegalArgumentException e) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    OrderDataDtoOutput output = orderDataService.update(orderData, id, version);
                    resp.getWriter().write(JsonConverter.fromOrderDataToJson(output));
                    resp.setStatus(HttpServletResponse.SC_CREATED);
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
