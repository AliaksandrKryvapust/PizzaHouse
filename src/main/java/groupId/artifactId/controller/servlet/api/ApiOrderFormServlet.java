package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.exceptions.IncorrectEncodingException;
import groupId.artifactId.exceptions.IncorrectServletInputStreamException;
import groupId.artifactId.exceptions.IncorrectServletRedirectException;
import groupId.artifactId.exceptions.IncorrectServletWriterException;
import groupId.artifactId.service.TokenService;
import groupId.artifactId.service.api.ITokenService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebServlet(name = "OrderForm", urlPatterns = "/api/order_form")
public class ApiOrderFormServlet extends HttpServlet {
    private final ITokenService tokenService = TokenService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            resp.getWriter().write(JsonConverter.fromTokenToJson(tokenService.getTokenIdToSend()));
        } catch (IOException e) {
            resp.setStatus(500);
            throw new IncorrectServletWriterException("Incorrect servlet state during response writer method", e);
        }
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            tokenService.add(JsonConverter.fromJsonToOrder(req.getInputStream()));
        } catch (UnsupportedEncodingException e) {
            resp.setStatus(500);
            throw new IncorrectEncodingException("Failed to set character encoding UTF-8", e);
        } catch (IOException e) {
            resp.setStatus(500);
            throw new IncorrectServletInputStreamException("Impossible to get input stream from request", e);
        }
        resp.setStatus(201);
        try {
            resp.sendRedirect(req.getContextPath() + "/api/order_form");
        } catch (IOException e) {
            resp.setStatus(500);
            throw new IncorrectServletRedirectException("Wrong location for Servlet redirect", e);
        }
    }
}
//to add new Order in Storage
//{
//        "selectedItems":[
//        {
//        "menuItem":{
//        "price":20.0,
//        "pizzaInfo":{
//        "name":"AMERICANA PIZZA",
//        "description":"Mozzarella cheese, basilica, ham",
//        "size":32
//        }
//        },
//        "count":2
//        }
//        ]
//        }