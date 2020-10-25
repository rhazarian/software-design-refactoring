package ru.akirakozov.sd.refactoring.repository;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void addProduct(Product product) throws SQLException;

    Optional<Product> getMaxPriceProduct() throws SQLException;
    Optional<Product> getMinPriceProduct() throws SQLException;
    List<Product> getAll() throws SQLException;
    long getSummaryPrice() throws SQLException;
    int count() throws SQLException;
}
