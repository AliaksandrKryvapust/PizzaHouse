package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.service.IoC.MenuItemServiceSingleton;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MenuItemPizzaInfo", urlPatterns = "/api/menu_item/pizza_info")
public class ApiMenuItemPizzaInfoServlet extends HttpServlet {
    private final IMenuItemService menuItemService = MenuItemServiceSingleton.getInstance();
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String PARAMETER_ID = "id";

    //Read POSITION
    //1) Read item with all enclosed data need id param  (id = 92)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(CONTENT_TYPE);
            resp.setCharacterEncoding(ENCODING);
            String id = req.getParameter(PARAMETER_ID);
            if (id != null) {
                if (menuItemService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromMenuItemToJson(menuItemService.getAllData(Long.valueOf(id))));
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
