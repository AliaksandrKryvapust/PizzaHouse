package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.validator.IoC.MenuItemValidatorSingleton;
import groupId.artifactId.controller.validator.api.IMenuItemValidator;
import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.service.IoC.MenuItemServiceSingleton;
import groupId.artifactId.service.IoC.MenuServiceSingleton;
import groupId.artifactId.service.IoC.PizzaInfoServiceSingleton;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.service.api.IPizzaInfoService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//CRUD controller
//IMenuItem
@WebServlet(name = "MenuItem", urlPatterns = "/api/menu_item")
public class ApiMenuItemServlet extends HttpServlet {
    private final IMenuItemService menuItemService = MenuItemServiceSingleton.getInstance();
    private final IMenuService menuService = MenuServiceSingleton.getInstance();
    private final IPizzaInfoService pizzaInfoService = PizzaInfoServiceSingleton.getInstance();
    private final IMenuItemValidator menuItemValidator = MenuItemValidatorSingleton.getInstance();
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_VERSION = "version";
    private static final String PARAMETER_DELETE = "delete";

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 93)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(CONTENT_TYPE);
            resp.setCharacterEncoding(ENCODING);
            String id = req.getParameter(PARAMETER_ID);
            if (id != null) {
                if (menuItemService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromMenuItemToJson(menuItemService.get(Long.valueOf(id))));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.getWriter().write(JsonConverter.fromMenuItemListToJson(menuItemService.get()));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //CREATE POSITION
    //body json
//   {
//           "price":20.0,
//           "pizzaInfoId":123,
//           "menuId":1
//           }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(CONTENT_TYPE);
            MenuItemDtoInput menuItem = JsonConverter.fromJsonToMenuItem(req.getInputStream());
            if (menuService.isIdValid(menuItem.getMenuId()) && pizzaInfoService.isIdValid(menuItem.getPizzaInfoId())) {
                try {
                    menuItemValidator.validate(menuItem);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                MenuItemDtoOutput menuItemDto = menuItemService.save(menuItem);
                resp.getWriter().write(JsonConverter.fromMenuItemToJson(menuItemDto));
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //UPDATE POSITION
    //need param id  (id = 93)
    //need param version/date_update - optimistic lock (version=1)
    //body json
//   {
//           "price":25.0,
//           "pizzaInfoId":123,
//           "menuId":1
//           }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(CONTENT_TYPE);
            String id = req.getParameter(PARAMETER_ID);
            String version = req.getParameter(PARAMETER_VERSION);
            if (id != null && version != null) {
                if (menuItemService.isIdValid(Long.valueOf(id))) {
                    MenuItemDtoInput menuItem = JsonConverter.fromJsonToMenuItem(req.getInputStream());
                    try {
                        menuItemValidator.validate(menuItem);
                    } catch (IllegalArgumentException e) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    MenuItemDtoOutput menuItemDto = menuItemService.update(menuItem, id, version);
                    resp.getWriter().write(JsonConverter.fromMenuItemToJson(menuItemDto));
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (OptimisticLockException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE POSITION
    //need param id  (id = 76)
    //need param version/date_update - optimistic lock (version=2)
    //param delete - true/false completely delete (delete=false)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(CONTENT_TYPE);
            String id = req.getParameter(PARAMETER_ID);
            String version = req.getParameter(PARAMETER_VERSION);
            String delete = req.getParameter(PARAMETER_DELETE);
            if (id != null && version != null && delete != null) {
                if (menuItemService.isIdValid(Long.valueOf(id))) {
                    menuItemService.delete(id, version, delete);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (OptimisticLockException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
