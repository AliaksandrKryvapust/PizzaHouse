package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.service.IoC.CompletedOrderServiceSingleton;
import groupId.artifactId.service.IoC.OrderDataServiceSingleton;
import groupId.artifactId.service.api.ICompletedOrderService;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.utils.Constants;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TicketCompletedOrder", urlPatterns = "/api/ticket/completed_order")
public class ApiTicketCompletedOrderServlet extends HttpServlet {
    private final ICompletedOrderService completedOrderService = CompletedOrderServiceSingleton.getInstance();
    private final IOrderDataService orderDataService = OrderDataServiceSingleton.getInstance();

    //Read POSITION
    //1) Read item need id param  (id = 1)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(Constants.CONTENT_TYPE);
            resp.setCharacterEncoding(Constants.ENCODING);
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                if (orderDataService.isTicketIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromCompletedOrderToJson(completedOrderService.getAllData(Long.valueOf(id))));
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

