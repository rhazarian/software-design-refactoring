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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetProductsServletTest {
    @Test
    public void testEmptyGet() throws IOException, SQLException {
        final Statement stmt = mock(Statement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(stmt.executeQuery("SELECT * FROM PRODUCT")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        final String result = testCase(stmt);
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGet() throws IOException, SQLException {
        final Statement stmt = mock(Statement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(stmt.executeQuery("SELECT * FROM PRODUCT")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn("product1").thenReturn("product2");
        when(resultSet.getInt("price")).thenReturn(5).thenReturn(10);
        final String result = testCase(stmt);
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "product1\t5</br>"
                , "product2\t10</br>"
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, result);
    }

    private String testCase(final Statement stmt) throws SQLException, IOException {
        final Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenReturn(stmt);
        final GetProductsServlet servlet = new GetProductsServlet(() -> connection);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
        servlet.doGet(request, response);
        return writer.getBuffer().toString();
    }
}
