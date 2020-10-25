package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public abstract class AbstractProductServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.getWriter().println(processRequest(request));
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected abstract String processRequest(HttpServletRequest request) throws IOException, SQLException;
}
