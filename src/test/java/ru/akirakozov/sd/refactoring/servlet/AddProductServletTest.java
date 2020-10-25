package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;
import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AddProductServletTest {
    @Test
    public void testAdd() throws SQLException, IOException {
        final Product product = new Product("newProduct", 500);

        final ProductRepository productRepository = mock(ProductRepository.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("name")).thenReturn(product.getName());
        when(request.getParameter("price")).thenReturn(Long.toString(product.getPrice()));

        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);

        final AddProductServlet servlet = new AddProductServlet(productRepository);
        servlet.doGet(request, response);

        verify(productRepository, times(1)).addProduct(eq(product));
        final String expectedResult = String.join(System.getProperty("line.separator")
                , "OK"
                , ""
        );
        assertEquals(expectedResult, writer.getBuffer().toString());
    }
}
