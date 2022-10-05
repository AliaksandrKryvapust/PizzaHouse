package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.exceptions.IncorrectServletWriterException;
import groupId.artifactId.service.TokenService;
import groupId.artifactId.service.api.ITokenService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TokenForm", urlPatterns = "/api/token_form")
public class ApiTokenFormServlet extends HttpServlet {
    private final ITokenService tokenService = TokenService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            resp.getWriter().write(JsonConverter.fromTokenToJson(tokenService.getTokenIdToSend()));
        } catch (IOException e){
            resp.setStatus(500);
            throw new IncorrectServletWriterException("Incorrect servlet state during response writer method", e);
        }
        resp.setStatus(200);
    }
}
