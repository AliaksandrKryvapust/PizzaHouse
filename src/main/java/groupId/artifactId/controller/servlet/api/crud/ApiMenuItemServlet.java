package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.utils.IoC.JsonConverterSingleton;
import groupId.artifactId.controller.utils.JsonConverter;
import groupId.artifactId.controller.validator.IoC.MenuItemValidatorSingleton;
import groupId.artifactId.controller.validator.api.IMenuItemValidator;
import groupId.artifactId.core.Constants;
import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.crud.MenuItemDtoCrudOutput;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.service.IoC.MenuItemServiceSingleton;
import groupId.artifactId.service.api.IMenuItemService;
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
    private final IMenuItemService menuItemService;
    private final IMenuItemValidator menuItemValidator;
    private final Logger logger;
    private final JsonConverter jsonConverter;

    public ApiMenuItemServlet() {
        this.menuItemService = MenuItemServiceSingleton.getInstance();
        this.menuItemValidator = MenuItemValidatorSingleton.getInstance();
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.jsonConverter = JsonConverterSingleton.getInstance();
    }

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
                resp.getWriter().write(jsonConverter.fromMenuItemToCrudJson(menuItemService.get(Long.valueOf(id))));
            } else {
                resp.getWriter().write(jsonConverter.fromMenuItemListToJson(menuItemService.get()));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            logger.error("/api/menu_item there is no content to fulfill doGet method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu_item crashed during doGet method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }

    //CREATE POSITION
    //body json
//   {
//           "price":20.0,
//           "pizza_info_id":123,
//           "menu_id":1
//           }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            MenuItemDtoInput menuItem = jsonConverter.fromJsonToMenuItem(req.getInputStream());
            try {
                menuItemValidator.validate(menuItem);
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                logger.error("/api/menu_item input is not valid " + e.getMessage() + "\t" + e.getCause() +
                        "\tresponse status: " + resp.getStatus());
            }
            MenuItemDtoCrudOutput menuItemDto = menuItemService.save(menuItem);
            resp.getWriter().write(jsonConverter.fromMenuItemToCrudJson(menuItemDto));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("/api/menu_item there is no content to fulfill doPost method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu_item crashed during doPost method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }

    //UPDATE POSITION
    //need param id  (id = 93)
    //need param version/date_update - optimistic lock (version=1)
    //body json
//   {
//           "price":25.0,
//           "pizza_info_id":123,
//           "menu_id":1
//           }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            String id = req.getParameter(Constants.PARAMETER_ID);
            String version = req.getParameter(Constants.PARAMETER_VERSION);
            if (id != null && version != null) {
                MenuItemDtoInput menuItem = jsonConverter.fromJsonToMenuItem(req.getInputStream());
                try {
                    menuItemValidator.validate(menuItem);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error("/api/menu_item input is not valid " + e.getMessage() + "\t" + e.getCause() +
                            "\tresponse status: " + resp.getStatus());
                }
                MenuItemDtoCrudOutput menuItemDto = menuItemService.update(menuItem, id, version);
                resp.getWriter().write(jsonConverter.fromMenuItemToCrudJson(menuItemDto));
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("/api/menu_item there is no content to fulfill doPut method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (OptimisticLockException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            logger.error("/api/menu_item optimistic lock during doPut method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu_item crashed during doPut method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
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
            logger.error("/api/menu_item optimistic lock during doDelete method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu_item crashed during doDelete method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }
}
