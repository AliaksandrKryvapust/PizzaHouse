package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.exceptions.IncorrectServletWriterException;
import groupId.artifactId.service.MenuItemService;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//CRUD controller
//IMenuItem
@WebServlet(name = "MenuItem", urlPatterns = "/api/menu/item")
public class ApiMenuItemServlet extends HttpServlet {
    private final IMenuItemService menuItemService = MenuItemService.getInstance();
    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 1)

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String id = req.getParameter("id");
            if (id != null) {
                if (menuItemService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromMenuItemToJson(menuItemService.get(Long.valueOf(id))));
                } else {
                    resp.setStatus(400);
                    throw new IllegalArgumentException("Menu id is not exist");
                }
            } else {
                resp.getWriter().write(JsonConverter.fromMenuItemListToJson(menuItemService.get()));
            }
        } catch (IOException e) {
            resp.setStatus(500);
            throw new IncorrectServletWriterException("Incorrect servlet state during response writer method", e);
        }
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
//        try {
//            req.setCharacterEncoding("UTF-8");
//            resp.setContentType("application/json");
//            menuService.addMenuItem(JsonConverter.fromJsonToMenuWithId(req.getInputStream()));
//        } catch (UnsupportedEncodingException e) {
//            resp.setStatus(500);
//            throw new IncorrectEncodingException("Failed to set character encoding UTF-8", e);
//        } catch (IOException e) {
//            resp.setStatus(500);
//            throw new IncorrectServletInputStreamException("Impossible to get input stream from request", e);
//        }
//        resp.setStatus(201);
    }
}
//to add new Menu item by Menu id
//   {
//           "price":20.0,
//           "id":1,
//           "pizzaInfo":{
//           "name":"ITALIANO PIZZA",
//           "description":"Mozzarella cheese, basilica, ham",
//           "size":32
//           }
//           }