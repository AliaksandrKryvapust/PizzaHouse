package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.exceptions.IncorrectEncodingException;
import groupId.artifactId.exceptions.IncorrectServletWriterException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@WebServlet(name = "ExceptionHandler", urlPatterns = "/exception")
public class ExceptionHandlerServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
//        this.handle(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
//        this.handle(req, resp);
//    }
//
//    private void handle(HttpServletRequest req, HttpServletResponse resp) {
//        try {
//            req.setCharacterEncoding("UTF-8");
//            resp.setContentType("text/html; charset=UTF-8");
//            Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
//            PrintWriter writer = resp.getWriter();
//            writer.write("<p> Error: " + throwable.getMessage() + "</p>");
//        } catch (UnsupportedEncodingException e) {
//            throw new IncorrectEncodingException("Failed to set character encoding UTF-8", e);
//        } catch (IOException e) {
//            throw new IncorrectServletWriterException("Incorrect servlet state during response writer method", e);
//        }
//    }
}
