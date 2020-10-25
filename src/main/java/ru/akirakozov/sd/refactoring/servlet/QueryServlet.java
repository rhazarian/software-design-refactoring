package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    final ProductRepository productRepository;

    public QueryServlet(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            try {
                final Optional<Product> result = productRepository.getMaxPriceProduct();

                final PrintWriter writer = response.getWriter();
                writer.println("<html><body>");
                writer.println("<h1>Product with max price: </h1>");
                result.ifPresent(product -> writer.println(product.getName() + "\t" + product.getPrice() + "</br>"));
                writer.println("</body></html>");
            } catch (final SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if ("min".equals(command)) {
            try {
                final Optional<Product> result = productRepository.getMinPriceProduct();

                final PrintWriter writer = response.getWriter();
                writer.println("<html><body>");
                writer.println("<h1>Product with min price: </h1>");
                result.ifPresent(product -> writer.println(product.getName() + "\t" + product.getPrice() + "</br>"));
                writer.println("</body></html>");
            } catch (final SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if ("sum".equals(command)) {
            try {
                final long result = productRepository.getSummaryPrice();

                final PrintWriter writer = response.getWriter();
                writer.println("<html><body>");
                writer.println("Summary price: ");
                writer.println(result);
                writer.println("</body></html>");
            } catch (final SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if ("count".equals(command)) {
            try {
                final int result = productRepository.count();

                final PrintWriter writer = response.getWriter();
                writer.println("<html><body>");
                writer.println("Number of products: ");
                writer.println(result);
                writer.println("</body></html>");
            } catch (final SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
