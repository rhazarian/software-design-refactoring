package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;
import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class QueryServletTest {
    @Test
    public void testMax() throws SQLException {
        final Product product = new Product("maxProduct", 10);

        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.getMaxPriceProduct()).thenReturn(Optional.of(product));
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("max");

        final QueryServlet servlet = new QueryServlet(productRepository);
        final String result = servlet.processRequest(request);

        verify(productRepository, times(1)).getMaxPriceProduct();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "<h1>Product with max price: </h1>"
                , product.getName() + "\t" + product.getPrice() + "</br>"
                , "</body></html>"
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testMin() throws SQLException {
        final Product product = new Product("minProduct", 2);

        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.getMinPriceProduct()).thenReturn(Optional.of(product));
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("min");

        final QueryServlet servlet = new QueryServlet(productRepository);
        final String result = servlet.processRequest(request);

        verify(productRepository, times(1)).getMinPriceProduct();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "<h1>Product with min price: </h1>"
                , product.getName() + "\t" + product.getPrice() + "</br>"
                , "</body></html>"
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSum() throws SQLException {
        final long summaryPrice = 100;

        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.getSummaryPrice()).thenReturn(summaryPrice);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("sum");

        final QueryServlet servlet = new QueryServlet(productRepository);
        final String result = servlet.processRequest(request);

        verify(productRepository, times(1)).getSummaryPrice();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "Summary price: "
                , Long.toString(summaryPrice)
                , "</body></html>"
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCount() throws SQLException {
        final int numberOfProducts = 25;

        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.count()).thenReturn(numberOfProducts);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("count");

        final QueryServlet servlet = new QueryServlet(productRepository);
        final String result = servlet.processRequest(request);

        verify(productRepository, times(1)).count();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "Number of products: "
                , Integer.toString(numberOfProducts)
                , "</body></html>"
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUnknownCommand() throws SQLException {
        final String command = "unknown";

        final ProductRepository productRepository = mock(ProductRepository.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn(command);

        final QueryServlet servlet = new QueryServlet(productRepository);
        final String result = servlet.processRequest(request);

        final String expectedResult = "Unknown command: " + command;
        assertEquals(expectedResult, result);
    }
}
