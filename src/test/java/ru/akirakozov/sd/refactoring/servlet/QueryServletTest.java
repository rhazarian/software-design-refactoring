package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;
import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.repository.ProductRepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class QueryServletTest {
    @Test
    public void testMax() throws IOException, SQLException {
        final Product product = new Product("maxProduct", 10);

        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.getMaxPriceProduct()).thenReturn(Optional.of(product));
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("max");

        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);

        final QueryServlet servlet = new QueryServlet(productRepository);
        servlet.doGet(request, response);

        verify(productRepository, times(1)).getMaxPriceProduct();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "<h1>Product with max price: </h1>"
                , product.getName() + "\t" + product.getPrice() + "</br>"
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, writer.getBuffer().toString());
    }

    @Test
    public void testMin() throws IOException, SQLException {
        final Product product = new Product("minProduct", 2);

        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.getMinPriceProduct()).thenReturn(Optional.of(product));
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("min");

        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);

        final QueryServlet servlet = new QueryServlet(productRepository);
        servlet.doGet(request, response);

        verify(productRepository, times(1)).getMinPriceProduct();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "<h1>Product with min price: </h1>"
                , product.getName() + "\t" + product.getPrice() + "</br>"
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, writer.getBuffer().toString());
    }

    @Test
    public void testSum() throws IOException, SQLException {
        final long summaryPrice = 100;

        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.getSummaryPrice()).thenReturn(summaryPrice);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("sum");

        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);

        final QueryServlet servlet = new QueryServlet(productRepository);
        servlet.doGet(request, response);

        verify(productRepository, times(1)).getSummaryPrice();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "Summary price: "
                , Long.toString(summaryPrice)
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, writer.getBuffer().toString());
    }

    @Test
    public void testCount() throws IOException, SQLException {
        final int numberOfProducts = 25;

        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.count()).thenReturn(numberOfProducts);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("count");

        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);

        final QueryServlet servlet = new QueryServlet(productRepository);
        servlet.doGet(request, response);

        verify(productRepository, times(1)).count();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "Number of products: "
                , Integer.toString(numberOfProducts)
                , "</body></html>"
                , ""
        );
        assertEquals(expectedResult, writer.getBuffer().toString());
    }
}
