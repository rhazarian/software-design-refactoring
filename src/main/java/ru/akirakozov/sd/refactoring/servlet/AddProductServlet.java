package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractProductServlet {
    final ProductRepository productRepository;

    public AddProductServlet(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected String processRequest(HttpServletRequest request) throws SQLException {
        final String name = request.getParameter("name");
        final long price = Long.parseLong(request.getParameter("price"));

        productRepository.addProduct(new Product(name, price));

        return "OK";
    }
}
