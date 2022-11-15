package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.service.IoC.CompletedOrderServiceSingleton;
import groupId.artifactId.service.api.ICompletedOrderService;
import groupId.artifactId.core.Constants;
import groupId.artifactId.utils.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CompletedOrder", urlPatterns = "/api/completed_order")
public class ApiCompletedOrderServlet extends HttpServlet {
    private final ICompletedOrderService completedOrderService = CompletedOrderServiceSingleton.getInstance();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 5)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(Constants.CONTENT_TYPE);
            resp.setCharacterEncoding(Constants.ENCODING);
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                if (completedOrderService.isOrderIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromCompletedOrderCrudToJson(completedOrderService.get(Long.valueOf(id))));
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
            logger.error("/api/completed_order crashed during doGet method" + e.getMessage() + resp.getStatus());
        }
    }

}