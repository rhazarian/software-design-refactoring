package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractProductServlet {
    final ProductRepository productRepository;

    public GetProductsServlet(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected String processRequest(HttpServletRequest request) throws SQLException {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);

        final List<Product> result = productRepository.getAll();

        writer.println("<html><body>");
        for (final Product product : result) {
            writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
        }
        writer.print("</body></html>");

        return stringWriter.getBuffer().toString();
    }
}
