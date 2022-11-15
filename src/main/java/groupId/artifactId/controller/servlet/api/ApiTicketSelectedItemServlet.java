package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.service.IoC.OrderServiceSingleton;
import groupId.artifactId.service.api.IOrderService;
import groupId.artifactId.core.Constants;
import groupId.artifactId.controller.utils.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TicketSelectedItem", urlPatterns = "/api/ticket/selected_item")
public class ApiTicketSelectedItemServlet extends HttpServlet {
    private final IOrderService orderService = OrderServiceSingleton.getInstance();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //Read POSITION
    //1) Read item need id param  (id = 1)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(Constants.CONTENT_TYPE);
            resp.setCharacterEncoding(Constants.ENCODING);
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                if (orderService.isTicketIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromTicketToJson(orderService.getAllData(Long.valueOf(id))));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/ticket/selected_item crashed during doGet method" + e.getMessage() + resp.getStatus());
        }
    }
}
