package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {
    final ProductRepository productRepository;

    public AddProductServlet(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String name = request.getParameter("name");
        final long price = Long.parseLong(request.getParameter("price"));

        try {
            productRepository.addProduct(new Product(name, price));
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
