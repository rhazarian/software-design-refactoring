package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddProductServletTest {
    @Test
    public void testAdd() throws SQLException, IOException {
        final Statement stmt = mock(Statement.class);
        final Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenReturn(stmt);
        final AddProductServlet servlet = new AddProductServlet(() -> connection);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("name")).thenReturn("newProduct");
        when(request.getParameter("price")).thenReturn("500");
        final String expectedQuery = "INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"newProduct\",500)";
        when(stmt.executeUpdate(any())).then(invocation -> {
            assertEquals(expectedQuery, invocation.getArgument(0, String.class));
            return 1;
        });
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
        servlet.doGet(request, response);
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "OK"
                , ""
        );
        assertEquals(expectedResult, writer.getBuffer().toString());
    }
}
