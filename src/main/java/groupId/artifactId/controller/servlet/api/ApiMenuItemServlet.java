package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.config.AppContext;
import groupId.artifactId.controller.utils.JsonConverter;
import groupId.artifactId.controller.validator.MenuItemValidator;
import groupId.artifactId.controller.validator.api.IMenuItemValidator;
import groupId.artifactId.core.Constants;
import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.service.MenuItemService;
import groupId.artifactId.service.api.IMenuItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.OptimisticLockException;
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
        AnnotationConfigApplicationContext context = AppContext.getContext();
        this.menuItemService = context.getBean(MenuItemService.class);
        this.menuItemValidator = context.getBean(MenuItemValidator.class);
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.jsonConverter = context.getBean(JsonConverter.class);
    }

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 93)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                resp.getWriter().write(jsonConverter.fromMenuItemToJson(menuItemService.get(Long.valueOf(id))));
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
            MenuItemDtoInput menuItemDtoInput = jsonConverter.fromJsonToMenuItem(req.getInputStream());
            try {
                menuItemValidator.validate(menuItemDtoInput);
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                logger.error("/api/menu_item input is not valid " + e.getMessage() + "\t" + e.getCause() +
                        "\tresponse status: " + resp.getStatus());
            }
            MenuItemDtoOutput dtoCrudOutput = menuItemService.save(menuItemDtoInput);
            resp.getWriter().write(jsonConverter.fromMenuItemToJson(dtoCrudOutput));
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
            String id = req.getParameter(Constants.PARAMETER_ID);
            String version = req.getParameter(Constants.PARAMETER_VERSION);
            if (id != null && version != null) {
                MenuItemDtoInput dtoInput = jsonConverter.fromJsonToMenuItem(req.getInputStream());
                try {
                    menuItemValidator.validate(dtoInput);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error("/api/menu_item input is not valid " + e.getMessage() + "\t" + e.getCause() +
                            "\tresponse status: " + resp.getStatus());
                }
                MenuItemDtoOutput crudOutput = menuItemService.update(dtoInput, id, version);
                resp.getWriter().write(jsonConverter.fromMenuItemToJson(crudOutput));
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
            logger.error("/api/menu_item crashed during doPut method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }

    //DELETE POSITION
    //need param id  (id = 76)
    //param delete - true completely delete/false delete menu_id (delete=false)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String id = req.getParameter(Constants.PARAMETER_ID);
            String delete = req.getParameter(Constants.PARAMETER_DELETE);
            if (id != null && delete != null) {
                menuItemService.delete(id, delete);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("/api/menu_item there is no content to fulfill doDelete method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu_item crashed during doDelete method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }
}
