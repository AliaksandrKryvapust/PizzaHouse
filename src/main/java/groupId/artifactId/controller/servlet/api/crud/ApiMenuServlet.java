package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.utils.IoC.JsonConverterSingleton;
import groupId.artifactId.controller.utils.JsonConverter;
import groupId.artifactId.controller.validator.IoC.MenuValidatorSingleton;
import groupId.artifactId.controller.validator.api.IMenuValidator;
import groupId.artifactId.core.Constants;
import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.service.IoC.MenuServiceSingleton;
import groupId.artifactId.service.api.IMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//CRUD controller
//IMenu
@WebServlet(name = "Menu", urlPatterns = "/api/menu")
public class ApiMenuServlet extends HttpServlet {
    private final IMenuService menuService;
    private final IMenuValidator menuValidator;
    private final Logger logger;
    private final JsonConverter jsonConverter;

    public ApiMenuServlet() {
        this.menuService = MenuServiceSingleton.getInstance();
        this.menuValidator = MenuValidatorSingleton.getInstance();
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.jsonConverter = JsonConverterSingleton.getInstance();
    }
    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 1)

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                resp.getWriter().write(jsonConverter.fromMenuToCrudJson(menuService.get(Long.valueOf(id))));
            } else {
                resp.getWriter().write(jsonConverter.fromMenuListToJson(menuService.get()));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            logger.error("/api/menu there is no content to fulfill doGet method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu crashed during doGet method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
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
            MenuDtoInput menu = jsonConverter.fromJsonToMenu(req.getInputStream());
            try {
                menuValidator.validate(menu);
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                logger.error("/api/menu input is not valid " + e.getMessage() + "\t" + e.getCause() +
                        "\tresponse status: " + resp.getStatus());
            }
            MenuDtoCrudOutput menuDto = menuService.save(menu);
            resp.getWriter().write(jsonConverter.fromMenuToCrudJson(menuDto));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("/api/menu there is no content to fulfill doPost method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu crashed during doPost method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
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
            String id = req.getParameter(Constants.PARAMETER_ID);
            String version = req.getParameter(Constants.PARAMETER_VERSION);
            if (id != null && version != null) {
                MenuDtoInput menu = jsonConverter.fromJsonToMenu(req.getInputStream());
                try {
                    menuValidator.validate(menu);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error("/api/menu input is not valid " + e.getMessage() + "\t" + e.getCause() +
                            "\tresponse status: " + resp.getStatus());
                }
                MenuDtoCrudOutput menuDto = menuService.update(menu, id, version);
                resp.getWriter().write(jsonConverter.fromMenuToCrudJson(menuDto));
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("/api/menu there is no content to fulfill doPut method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (OptimisticLockException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            logger.error("/api/menu optimistic lock during doPut method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu crashed during doPut method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }

    //DELETE POSITION
    //need param id  (id = 3)
    //param delete - true/false completely delete (delete=false)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String id = req.getParameter(Constants.PARAMETER_ID);
            String delete = req.getParameter(Constants.PARAMETER_DELETE);
            if (id != null && delete != null) {
                menuService.delete(id, delete);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (NoContentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("/api/menu there is no content to fulfill doDelete method " + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/menu crashed during doDelete method" + e.getMessage() + "\t" + e.getCause() +
                    "\tresponse status: " + resp.getStatus());
        }
    }
}
