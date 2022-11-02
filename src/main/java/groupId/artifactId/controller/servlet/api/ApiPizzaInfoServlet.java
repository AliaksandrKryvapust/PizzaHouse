package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.controller.validator.IoC.PizzaInfoValidatorSingleton;
import groupId.artifactId.controller.validator.api.IPizzaInfoValidator;
import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.service.IoC.PizzaInfoServiceSingleton;
import groupId.artifactId.service.api.IPizzaInfoService;
import groupId.artifactId.utils.JsonConverter;

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
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_VERSION = "version";
    private static final String PARAMETER_DELETE = "delete";

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 122)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType(CONTENT_TYPE);
            resp.setCharacterEncoding(ENCODING);
            String id = req.getParameter(PARAMETER_ID);
            if (id != null) {
                if (pizzaInfoService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromPizzaInfoToJson(pizzaInfoService.get(Long.valueOf(id))));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.getWriter().write(JsonConverter.fromPizzaInfoListToJson(pizzaInfoService.get()));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
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
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(CONTENT_TYPE);
            PizzaInfoDtoInput pizzaInfo = JsonConverter.fromJsonToPizzaInfo(req.getInputStream());
            if (!pizzaInfoService.exist(pizzaInfo.getName())) {
                try {
                    pizzaInfoValidator.validate(pizzaInfo);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                PizzaInfoDtoOutput pizzaInfoDto = pizzaInfoService.save(pizzaInfo);
                resp.getWriter().write(JsonConverter.fromPizzaInfoToJson(pizzaInfoDto));
            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
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
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(CONTENT_TYPE);
            String id = req.getParameter(PARAMETER_ID);
            String version = req.getParameter(PARAMETER_VERSION);
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
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
    //DELETE POSITION
    //need param id  (id = 97)
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
                if (pizzaInfoService.isIdValid(Long.valueOf(id))) {
                    pizzaInfoService.delete(id, version, delete);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
