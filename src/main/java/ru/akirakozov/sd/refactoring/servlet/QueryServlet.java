package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.utility.ResponseBuilder;

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
        final ResponseBuilder builder = new ResponseBuilder();

        final String command = request.getParameter("command");

        if ("max".equals(command)) {
            final Optional<Product> result = productRepository.getMaxPriceProduct();

            builder.append("<h1>Product with max price: </h1>");
            result.ifPresent(builder::append);
        } else if ("min".equals(command)) {
            final Optional<Product> result = productRepository.getMinPriceProduct();

            builder.append("<h1>Product with min price: </h1>");
            result.ifPresent(builder::append);
        } else if ("sum".equals(command)) {
            final long result = productRepository.getSummaryPrice();

            builder.append("Summary price: ");
            builder.append(result);
        } else if ("count".equals(command)) {
            final int result = productRepository.count();

            builder.append("Number of products: ");
            builder.append(result);
        } else {
            return "Unknown command: " + command;
        }

        return builder.toString();
    }
}
