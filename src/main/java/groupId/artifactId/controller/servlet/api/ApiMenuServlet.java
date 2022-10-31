package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.controller.validator.MenuValidator;
import groupId.artifactId.controller.validator.api.IMenuValidator;
import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.exceptions.IncorrectEncodingException;
import groupId.artifactId.exceptions.IncorrectServletInputStreamException;
import groupId.artifactId.exceptions.IncorrectServletWriterException;
import groupId.artifactId.service.MenuService;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

//CRUD controller
//IMenu
@WebServlet(name = "Menu", urlPatterns = "/api/menu")
public class ApiMenuServlet extends HttpServlet {
    private final IMenuService menuService = MenuService.getInstance();
    private final IMenuValidator menuValidator = MenuValidator.getInstance();
    private static final String contentType = "application/json";
    private static final String encoding = "UTF-8";
    private static final String parameterId = "id";
    private static final String parameterVersion = "version";
    private static final String parameterDelete = "delete";

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 1)

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(contentType);
            resp.setCharacterEncoding(encoding);
            String id = req.getParameter(parameterId);
            if (id != null) {
                if (menuService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromMenuToJson(menuService.get(Long.valueOf(id))));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    throw new IllegalArgumentException("Menu with id:" + id + "is not exist");
                }
            } else {
                resp.getWriter().write(JsonConverter.fromMenuListToJson(menuService.get()));
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new IncorrectServletWriterException("Incorrect servlet state during response writer method", e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    //CREATE POSITION
    //body json
//    {
//            "name": "Optional Menu",
//            "enable": false
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding(encoding);
            resp.setContentType(contentType);
            MenuDtoInput menu = JsonConverter.fromJsonToMenu(req.getInputStream());
            if (!menuService.exist(menu.getName())){
                try {
                    menuValidator.validateMenu(menu);
                }
                catch (IllegalArgumentException e){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
                menuService.save(menu);
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                throw new IllegalArgumentException("Menu with such name:" + menu.getName() + "is already exist");
            }

        } catch (UnsupportedEncodingException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new IncorrectEncodingException("Failed to set character encoding UTF-8", e);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new IncorrectServletInputStreamException("Impossible to get input stream from request", e);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    //UPDATE POSITION
    //need param id  (id = 3)
    //need param version/date_update - optimistic lock (version=1)
    //body json
//    {
//            "name": "Optional Menu",
//            "enable": true
//    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding(encoding);
            resp.setContentType(contentType);
            String id = req.getParameter(parameterId);
            String version = req.getParameter(parameterVersion);
            if (id != null && version != null) {
                if (menuService.isIdValid(Long.valueOf(id))) {
                    MenuDtoInput menu = JsonConverter.fromJsonToMenu(req.getInputStream());
                    try {
                        menuValidator.validateMenu(menu);
                    }
                    catch (IllegalArgumentException e){
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        throw new IllegalArgumentException(e.getMessage(), e);
                    }
                    menuService.update(menu, id, version);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    throw new IllegalArgumentException("Menu with id:" + id + "is not exist");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
                throw new IllegalArgumentException("Field Menu id:" + id + "or Menu version:" + version + "is empty");
            }
        } catch (UnsupportedEncodingException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new IncorrectEncodingException("Failed to set character encoding UTF-8", e);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new IncorrectServletInputStreamException("Impossible to get input stream from request", e);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    //DELETE POSITION
    //need param id  (id = 3)
    //need param version/date_update - optimistic lock (version=2)
    //param delete - true/false completely delete (delete=false)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding(encoding);
            resp.setContentType(contentType);
            String id = req.getParameter(parameterId);
            String version = req.getParameter(parameterVersion);
            String delete = req.getParameter(parameterDelete);
            if (id != null && version != null && delete!=null) {
                if (menuService.isIdValid(Long.valueOf(id))) {
                    menuService.delete(id, version, delete);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    throw new IllegalArgumentException("Menu with id:" + id + "is not exist");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
                throw new IllegalArgumentException("Field Menu id:" + id + "or Menu version:" + version +
                        "or Menu delete status" + delete + "is empty");
            }
        } catch (UnsupportedEncodingException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new IncorrectEncodingException("Failed to set character encoding UTF-8", e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}