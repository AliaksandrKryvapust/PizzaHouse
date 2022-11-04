package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.exceptions.IncorrectEncodingException;
import groupId.artifactId.exceptions.IncorrectServletInputStreamException;
import groupId.artifactId.service.OrderDataService;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebServlet(name = "OrderStage", urlPatterns = "/api/order_stage")
public class ApiOrderStageServlet extends HttpServlet {
    private final IOrderDataService orderDataService = OrderDataService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            orderDataService.addOrderStage(JsonConverter.fromJsonToOrderStageWithId(req.getInputStream()));
        } catch (UnsupportedEncodingException e) {
            resp.setStatus(500);
            throw new IncorrectEncodingException("Failed to set character encoding UTF-8", e);
        } catch (IOException e) {
            resp.setStatus(500);
            throw new IncorrectServletInputStreamException("Impossible to get input stream from request", e);
        }
        resp.setStatus(201);
    }
}
//to add new Order stage by Ticket/Order id
//   {
//           "description":"Cooking",
//           "time":[
//        11,
//        7,
//        19,
//        279225100
//        ],
//           "id":1
//   }
