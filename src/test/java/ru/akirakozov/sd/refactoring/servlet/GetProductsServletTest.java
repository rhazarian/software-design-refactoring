package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;
import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GetProductsServletTest {
    @Test
    public void testEmptyGet() throws SQLException {
        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.getAll()).thenReturn(Collections.emptyList());
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final GetProductsServlet servlet = new GetProductsServlet(productRepository);
        final String result = servlet.processRequest(request);

        verify(productRepository, times(1)).getAll();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , "</body></html>"
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGet() throws SQLException {
        final Product product1 = new Product("product1", 5);
        final Product product2 = new Product("product2", 10);

        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.getAll()).thenReturn(Arrays.asList(product1, product2));
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final GetProductsServlet servlet = new GetProductsServlet(productRepository);
        final String result = servlet.processRequest(request);

        verify(productRepository, times(1)).getAll();
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "<html><body>"
                , product1.getName() + "\t" + product1.getPrice() + "</br>"
                , product2.getName() + "\t" + product2.getPrice() + "</br>"
                , "</body></html>"
        );
        assertEquals(expectedResult, result);
    }
}
