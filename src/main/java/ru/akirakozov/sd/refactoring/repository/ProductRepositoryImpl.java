package ru.akirakozov.sd.refactoring.repository;

import org.jetbrains.annotations.NotNull;
import ru.akirakozov.sd.refactoring.connection.ConnectionProvider;
import ru.akirakozov.sd.refactoring.domain.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ru.akirakozov.sd.refactoring.repository.ProductRepository {
    final ConnectionProvider connectionProvider;

    public ProductRepositoryImpl(final @NotNull ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        try (final Connection connection = connectionProvider.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?)")) {
                statement.setString(1, product.getName());
                statement.setLong(2, product.getPrice());
                statement.executeUpdate();
            }
        }
    }

    @Override @NotNull
    public Optional<Product> getMaxPriceProduct() throws SQLException {
        try (final Connection connection = connectionProvider.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")) {
                return getOptionalDomainObject(statement.executeQuery());
            }
        }
    }

    @Override @NotNull
    public Optional<Product> getMinPriceProduct() throws SQLException {
        try (final Connection connection = connectionProvider.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")) {
                return getOptionalDomainObject(statement.executeQuery());
            }
        }
    }

    @Override @NotNull
    public List<Product> getAll() throws SQLException {
        try (final Connection connection = connectionProvider.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCT")) {
                return getDomainObjects(statement.executeQuery());
            }
        }
    }

    @Override
    public int count() throws SQLException {
        try (final Connection connection = connectionProvider.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM PRODUCT")) {
                return statement.executeQuery().getInt(1);
            }
        }
    }

    @Override
    public long getSummaryPrice() throws SQLException {
        try (final Connection connection = connectionProvider.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("SELECT SUM(price) FROM PRODUCT")) {
                return statement.executeQuery().getLong(1);
            }
        }
    }

    @NotNull
    private List<Product> getDomainObjects(final @NotNull ResultSet resultSet) throws SQLException {
        final List<Product> list = new ArrayList<>();
        while (resultSet.next()) {
            final String name = resultSet.getString("name");
            final long price = resultSet.getLong("price");
            list.add(new Product(name, price));
        }
        return list;
    }

    @NotNull
    private Optional<Product> getOptionalDomainObject(final @NotNull ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            final String name = resultSet.getString("name");
            final long price = resultSet.getLong("price");
            return Optional.of(new Product(name, price));
        }
        return Optional.empty();
    }
}
