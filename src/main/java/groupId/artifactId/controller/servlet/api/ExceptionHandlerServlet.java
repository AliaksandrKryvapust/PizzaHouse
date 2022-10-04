package groupId.artifactId.controller.servlet.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ExceptionHandler", urlPatterns = "/exception")
public class ExceptionHandlerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        PrintWriter writer = resp.getWriter();
        writer.write("<p> Error: " + throwable.getMessage() + "</p>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws   IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        PrintWriter writer = resp.getWriter();
        writer.write("<p> Error: " + throwable.getMessage() + "</p>");
    }
}
