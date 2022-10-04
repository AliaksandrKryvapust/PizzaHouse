package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.service.MenuService;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MenuForm", urlPatterns = "/api/menu_form")
public class ApiMenuFormServlet extends HttpServlet {
    private final IMenuService menuService = MenuService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
//        try {
//            resp.getWriter().write(JsonConverter.toJson(productService.get()));
//        } catch (Exception e) {
//            resp.setStatus(500);
//        }
//        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        try {
            menuService.add(MenuMapper.menuMapping(JsonConverter.fromJson(req.getInputStream())));
        } catch (Exception e) {
            resp.setStatus(500);
        }
        resp.setStatus(201);
    }
}
