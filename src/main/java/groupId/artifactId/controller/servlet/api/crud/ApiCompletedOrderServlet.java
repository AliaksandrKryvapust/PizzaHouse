package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.utils.IoC.JsonConverterSingleton;
import groupId.artifactId.controller.utils.JsonConverter;
import groupId.artifactId.core.Constants;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.service.IoC.CompletedOrderServiceSingleton;
import groupId.artifactId.service.api.ICompletedOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CompletedOrder", urlPatterns = "/api/completed_order")
public class ApiCompletedOrderServlet extends HttpServlet {
    private final ICompletedOrderService completedOrderService;
    private final JsonConverter jsonConverter;
    private final Logger logger;

    public ApiCompletedOrderServlet() {
        this.completedOrderService = CompletedOrderServiceSingleton.getInstance();
        this.jsonConverter = JsonConverterSingleton.getInstance();
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

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
                resp.getWriter().write(jsonConverter.fromCompletedOrderCrudToJson(completedOrderService.get(Long.valueOf(id))));
            } else {
                resp.getWriter().write(jsonConverter.fromCompletedOrderListToJson(completedOrderService.get()));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            logger.error("/api/completed_order there is no content to fulfill doGet method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/completed_order crashed during doGet method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }

}