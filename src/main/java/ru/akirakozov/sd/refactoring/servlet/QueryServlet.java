package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractProductServlet {
    final ProductRepository productRepository;

    public QueryServlet(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected String processRequest(HttpServletRequest request) throws SQLException {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);

        final String command = request.getParameter("command");

        if ("max".equals(command)) {
            final Optional<Product> result = productRepository.getMaxPriceProduct();

            writer.println("<html><body>");
            writer.println("<h1>Product with max price: </h1>");
            result.ifPresent(product -> writer.println(product.getName() + "\t" + product.getPrice() + "</br>"));
            writer.print("</body></html>");
        } else if ("min".equals(command)) {
            final Optional<Product> result = productRepository.getMinPriceProduct();

            writer.println("<html><body>");
            writer.println("<h1>Product with min price: </h1>");
            result.ifPresent(product -> writer.println(product.getName() + "\t" + product.getPrice() + "</br>"));
            writer.print("</body></html>");
        } else if ("sum".equals(command)) {
            final long result = productRepository.getSummaryPrice();

            writer.println("<html><body>");
            writer.println("Summary price: ");
            writer.println(result);
            writer.print("</body></html>");
        } else if ("count".equals(command)) {
            final int result = productRepository.count();

            writer.println("<html><body>");
            writer.println("Number of products: ");
            writer.println(result);
            writer.print("</body></html>");
        } else {
            writer.print("Unknown command: " + command);
        }

        return stringWriter.getBuffer().toString();
    }
}
