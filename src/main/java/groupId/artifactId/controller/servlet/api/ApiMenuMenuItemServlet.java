package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.service.IoC.MenuServiceSingleton;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MenuMenuItem", urlPatterns = "/api/menu/menu_item")
public class ApiMenuMenuItemServlet extends HttpServlet {
    private final IMenuService menuService = MenuServiceSingleton.getInstance();
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String PARAMETER_ID = "id";

    //Read POSITION
    //1) Read item with all enclosed data need id param  (id = 1)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(CONTENT_TYPE);
            resp.setCharacterEncoding(ENCODING);
            String id = req.getParameter(PARAMETER_ID);
            if (id != null) {
                if (menuService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromMenuToJson(menuService.getAllData(Long.valueOf(id))));
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
