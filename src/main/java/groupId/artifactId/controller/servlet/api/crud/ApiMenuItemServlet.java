package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.validator.IoC.MenuItemValidatorSingleton;
import groupId.artifactId.controller.validator.api.IMenuItemValidator;
import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.crud.MenuItemDtoCrudOutput;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.service.IoC.MenuItemServiceSingleton;
import groupId.artifactId.service.IoC.MenuServiceSingleton;
import groupId.artifactId.service.IoC.PizzaInfoServiceSingleton;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.service.api.IPizzaInfoService;
import groupId.artifactId.core.Constants;
import groupId.artifactId.utils.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 93)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(Constants.CONTENT_TYPE);
            resp.setCharacterEncoding(Constants.ENCODING);
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                if (menuItemService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromMenuItemToCrudJson(menuItemService.get(Long.valueOf(id))));
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
            logger.error("/api/menu_item crashed during doGet method" + e.getMessage() + resp.getStatus());
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
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            MenuItemDtoInput menuItem = JsonConverter.fromJsonToMenuItem(req.getInputStream());
            if (menuService.isIdValid(menuItem.getMenuId()) && pizzaInfoService.isIdValid(menuItem.getPizzaInfoId())) {
                try {
                    menuItemValidator.validate(menuItem);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                MenuItemDtoCrudOutput menuItemDto = menuItemService.save(menuItem);
                resp.getWriter().write(JsonConverter.fromMenuItemToCrudJson(menuItemDto));
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu_item crashed during doPost method" + e.getMessage() + resp.getStatus());
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
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            String id = req.getParameter(Constants.PARAMETER_ID);
            String version = req.getParameter(Constants.PARAMETER_VERSION);
            if (id != null && version != null) {
                if (menuItemService.isIdValid(Long.valueOf(id))) {
                    MenuItemDtoInput menuItem = JsonConverter.fromJsonToMenuItem(req.getInputStream());
                    try {
                        menuItemValidator.validate(menuItem);
                    } catch (IllegalArgumentException e) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    MenuItemDtoCrudOutput menuItemDto = menuItemService.update(menuItem, id, version);
                    resp.getWriter().write(JsonConverter.fromMenuItemToCrudJson(menuItemDto));
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (OptimisticLockException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            logger.error("/api/menu_item optimistic lock during doPut method" + e.getMessage() + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu_item crashed during doPut method" + e.getMessage() + resp.getStatus());
        }
    }

    //DELETE POSITION
    //need param id  (id = 76)
    //need param version/date_update - optimistic lock (version=2)
    //param delete - true/false completely delete (delete=false)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            String id = req.getParameter(Constants.PARAMETER_ID);
            String version = req.getParameter(Constants.PARAMETER_VERSION);
            String delete = req.getParameter(Constants.PARAMETER_DELETE);
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
            logger.error("/api/menu_item optimistic lock during doDelete method" + e.getMessage() + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu_item crashed during doDelete method" + e.getMessage() + resp.getStatus());
        }
    }
}
