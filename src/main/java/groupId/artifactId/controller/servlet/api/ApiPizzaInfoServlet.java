package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.exceptions.IncorrectEncodingException;
import groupId.artifactId.exceptions.IncorrectServletInputStreamException;
import groupId.artifactId.exceptions.IncorrectServletWriterException;
import groupId.artifactId.service.PizzaInfoService;
import groupId.artifactId.service.api.IPizzaInfoService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

//CRUD controller
//IPizzaInfo
@WebServlet(name = "PizzaInfo", urlPatterns = "/api/menu/item/pizza_info")
public class ApiPizzaInfoServlet extends HttpServlet {
    private final IPizzaInfoService pizzaInfoService = PizzaInfoService.getInstance();

    //Read POSITION
    //1) Read list
    //2) Read item need id param  (id = 95)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String id = req.getParameter("id");
            if (id != null) {
                if (pizzaInfoService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromPizzaInfoToJson(pizzaInfoService.get(Long.valueOf(id))));
                } else {
                    resp.setStatus(400);
                    throw new IllegalArgumentException("PizzaInfo id is not exist");
                }
            } else {
                resp.getWriter().write(JsonConverter.fromPizzaInfoListToJson(pizzaInfoService.get()));
            }
        } catch (IOException e) {
            resp.setStatus(500);
            throw new IncorrectServletWriterException("Incorrect servlet state during response writer method", e);
        }
        resp.setStatus(200);
    }

    //CREATE POSITION
    //body json
    //to add new PizzaInfo in Storage
//   {
//           "name":"ITALIANO PIZZA",
//           "description":"Mozzarella cheese, basilica, ham",
//           "size":32
//           }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            pizzaInfoService.save(JsonConverter.fromJsonToPizzaInfo(req.getInputStream()));
        } catch (UnsupportedEncodingException e) {
            resp.setStatus(500);
            throw new IncorrectEncodingException("Failed to set character encoding UTF-8", e);
        } catch (IOException e) {
            resp.setStatus(500);
            throw new IncorrectServletInputStreamException("Impossible to get input stream from request", e);
        }
        resp.setStatus(201);
    }
}
