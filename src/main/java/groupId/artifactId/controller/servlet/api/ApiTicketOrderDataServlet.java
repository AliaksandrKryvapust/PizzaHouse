package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.service.IoC.OrderDataServiceSingleton;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TicketOrderData", urlPatterns = "/api/ticket/order_data")
public class ApiTicketOrderDataServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String PARAMETER_ID = "id";
    private final IOrderDataService orderDataService = OrderDataServiceSingleton.getInstance();

    //Read POSITION
    //1) Read item need id param  (id = 1)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(CONTENT_TYPE);
            resp.setCharacterEncoding(ENCODING);
            String id = req.getParameter(PARAMETER_ID);
            if (id != null) {
                if (orderDataService.isTicketIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromOrderDataToJson(orderDataService.getAllData(Long.valueOf(id))));
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

