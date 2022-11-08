package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.service.IoC.CompletedOrderServiceSingleton;
import groupId.artifactId.service.api.ICompletedOrderService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CompletedOrder", urlPatterns = "/api/completed_order")
public class ApiCompletedOrderServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String PARAMETER_ID = "id";
    private final ICompletedOrderService completedOrderService = CompletedOrderServiceSingleton.getInstance();

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 5)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(CONTENT_TYPE);
            resp.setCharacterEncoding(ENCODING);
            String id = req.getParameter(PARAMETER_ID);
            if (id != null) {
                if (completedOrderService.isOrderIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromCompletedOrderToJson(completedOrderService.get(Long.valueOf(id))));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.getWriter().write(JsonConverter.fromCompletedOrderListToJson(completedOrderService.get()));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}