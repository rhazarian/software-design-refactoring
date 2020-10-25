package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.utility.ResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractProductServlet {
    final ProductRepository productRepository;

    public QueryServlet(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @FunctionalInterface
    interface Command {
        String execute(ProductRepository productRepository) throws SQLException;
    }

    private static final HashMap<String, Command> COMMANDS = new HashMap<String, Command>() {{
        put("max", productRepository -> {
            final ResponseBuilder builder = new ResponseBuilder();

            final Optional<Product> result = productRepository.getMaxPriceProduct();

            builder.append("<h1>Product with max price: </h1>");
            result.ifPresent(builder::append);

            return builder.toString();
        });
        put("min", productRepository -> {
            final ResponseBuilder builder = new ResponseBuilder();

            final Optional<Product> result = productRepository.getMinPriceProduct();

            builder.append("<h1>Product with min price: </h1>");
            result.ifPresent(builder::append);

            return builder.toString();
        });
        put("sum", productRepository -> {
            final ResponseBuilder builder = new ResponseBuilder();

            final long result = productRepository.getSummaryPrice();

            builder.append("Summary price: ");
            builder.append(result);

            return builder.toString();
        });
        put("count", productRepository -> {
            final ResponseBuilder builder = new ResponseBuilder();

            final int result = productRepository.count();

            builder.append("Number of products: ");
            builder.append(result);

            return builder.toString();
        });
    }};

    @Override
    protected String processRequest(HttpServletRequest request) throws SQLException {
        final String commandId = request.getParameter("command");
        final Command command = COMMANDS.get(commandId);

        if (command == null) {
            return "Unknown command: " + commandId;
        }
        return command.execute(this.productRepository);
    }
}
