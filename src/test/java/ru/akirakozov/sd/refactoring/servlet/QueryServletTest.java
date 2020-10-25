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

public class QueryServletTest {
    @Test
    public void testMax() throws IOException, SQLException {
        final Statement stmt = mock(Statement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn("product1");
        when(resultSet.getInt("price")).thenReturn(10);
        final String result = testCase("max", stmt);
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "<h1>Product with max price: </h1>"
                , "product1\t10</br>"
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testMin() throws IOException, SQLException {
        final Statement stmt = mock(Statement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn("product1");
        when(resultSet.getInt("price")).thenReturn(2);
        final String result = testCase("min", stmt);
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "<h1>Product with min price: </h1>"
                , "product1\t2</br>"
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSum() throws IOException, SQLException {
        final Statement stmt = mock(Statement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(stmt.executeQuery("SELECT SUM(price) FROM PRODUCT")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(100);
        final String result = testCase("sum", stmt);
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "Summary price: "
                , "100"
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCount() throws IOException, SQLException {
        final Statement stmt = mock(Statement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(25);
        final String result = testCase("count", stmt);
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "Number of products: "
                , "25"
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, result);
    }

    private String testCase(final String command, final Statement stmt) throws SQLException, IOException {
        final Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenReturn(stmt);
        final QueryServlet servlet = new QueryServlet(() -> connection);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn(command);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
        servlet.doGet(request, response);
        return writer.getBuffer().toString();
    }
}
