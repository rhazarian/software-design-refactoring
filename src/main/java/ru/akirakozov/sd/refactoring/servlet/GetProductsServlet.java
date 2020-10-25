package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.utility.ResponseBuilder;

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
        final ResponseBuilder builder = new ResponseBuilder();

        final List<Product> result = productRepository.getAll();

        for (final Product product : result) {
            builder.append(product);
        }

        return builder.toString();
    }
}
