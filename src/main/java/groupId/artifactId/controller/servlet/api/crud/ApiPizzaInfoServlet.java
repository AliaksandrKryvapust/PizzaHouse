package groupId.artifactId.controller.servlet.api.crud;

import groupId.artifactId.controller.validator.IoC.PizzaInfoValidatorSingleton;
import groupId.artifactId.controller.validator.api.IPizzaInfoValidator;
import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.service.IoC.PizzaInfoServiceSingleton;
import groupId.artifactId.service.api.IPizzaInfoService;
import groupId.artifactId.utils.Constants;
import groupId.artifactId.utils.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//CRUD controller
//IPizzaInfo
@WebServlet(name = "PizzaInfo", urlPatterns = "/api/pizza_info")
public class ApiPizzaInfoServlet extends HttpServlet {
    private final IPizzaInfoService pizzaInfoService = PizzaInfoServiceSingleton.getInstance();
    private final IPizzaInfoValidator pizzaInfoValidator = PizzaInfoValidatorSingleton.getInstance();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 122)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(Constants.CONTENT_TYPE);
            resp.setCharacterEncoding(Constants.ENCODING);
            String id = req.getParameter(Constants.PARAMETER_ID);
            if (id != null) {
                if (pizzaInfoService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromPizzaInfoToJson(pizzaInfoService.get(Long.valueOf(id))));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.getWriter().write(JsonConverter.fromPizzaInfoListToJson(pizzaInfoService.get()));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/pizza_info crashed during doGet method" + e.getMessage() + resp.getStatus());
        }
    }

    //CREATE POSITION
    //body json
//   {
//           "name":"ITALIANO PIZZA",
//           "description":"Mozzarella cheese, basilica, ham",
//           "size":32
//           }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            PizzaInfoDtoInput pizzaInfo = JsonConverter.fromJsonToPizzaInfo(req.getInputStream());
            if (!pizzaInfoService.exist(pizzaInfo.getName())) {
                try {
                    pizzaInfoValidator.validate(pizzaInfo);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                PizzaInfoDtoOutput pizzaInfoDto = pizzaInfoService.save(pizzaInfo);
                resp.getWriter().write(JsonConverter.fromPizzaInfoToJson(pizzaInfoDto));
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/pizza_info crashed during doPost method" + e.getMessage() + resp.getStatus());
        }
    }
    //UPDATE POSITION
    //need param id  (id = 97)
    //need param version/date_update - optimistic lock (version=1)
    //body json
//   {
//           "name":"ITALIANO PIZZA",
//           "description":"Mozzarella cheese, basilica, ham",
//           "size":48
//           }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding(Constants.ENCODING);
            resp.setContentType(Constants.CONTENT_TYPE);
            String id = req.getParameter(Constants.PARAMETER_ID);
            String version = req.getParameter(Constants.PARAMETER_VERSION);
            if (id != null && version != null) {
                if (pizzaInfoService.isIdValid(Long.valueOf(id))) {
                    PizzaInfoDtoInput pizzaInfo = JsonConverter.fromJsonToPizzaInfo(req.getInputStream());
                    try {
                        pizzaInfoValidator.validate(pizzaInfo);
                    } catch (IllegalArgumentException e) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    PizzaInfoDtoOutput pizzaInfoDto = pizzaInfoService.update(pizzaInfo, id, version);
                    resp.getWriter().write(JsonConverter.fromPizzaInfoToJson(pizzaInfoDto));
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (OptimisticLockException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            logger.error("/api/pizza_info optimistic lock during doPut method" + e.getMessage() + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/pizza_info crashed during doPut method" + e.getMessage() + resp.getStatus());
        }
    }
    //DELETE POSITION
    //need param id  (id = 97)
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
                if (pizzaInfoService.isIdValid(Long.valueOf(id))) {
                    pizzaInfoService.delete(id, version, delete);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (OptimisticLockException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            logger.error("/api/pizza_info optimistic lock during doDelete method" + e.getMessage() + resp.getStatus());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("/api/pizza_info crashed during doDelete method" + e.getMessage() + resp.getStatus());
        }
    }
}
