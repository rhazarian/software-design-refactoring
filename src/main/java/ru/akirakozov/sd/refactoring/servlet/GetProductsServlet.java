package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    final ProductRepository productRepository;

    public GetProductsServlet(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            final List<Product> result = productRepository.getAll();

            final PrintWriter writer = response.getWriter();
            response.getWriter().println("<html><body>");
            for (final Product product : result) {
                response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
            }
            response.getWriter().println("</body></html>");
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
