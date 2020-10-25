package ru.akirakozov.sd.refactoring.repository;

import org.junit.Test;
import org.mockito.InOrder;
import ru.akirakozov.sd.refactoring.domain.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ProductRepositoryTest {
    @Test
    public void testAdd() throws SQLException {
        final Product product = new Product("newProduct", 500);

        final Connection connection = mock(Connection.class);
        final ProductRepository productRepository = new ProductRepositoryImpl(() -> connection);
        final PreparedStatement statement = mock(PreparedStatement.class);
        when(connection.prepareStatement("INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?)")).thenReturn(statement);

        productRepository.addProduct(product);

        final InOrder inOrder = inOrder(statement);
        inOrder.verify(statement, times(1)).setString(1, product.getName());
        inOrder.verify(statement, times(1)).setLong(2, product.getPrice());
        inOrder.verify(statement, times(1)).executeUpdate();
    }

    @Test
    public void testEmptyGet() throws SQLException {
        final Connection connection = mock(Connection.class);
        final ProductRepository productRepository = new ProductRepositoryImpl(() -> connection);
        final PreparedStatement statement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(connection.prepareStatement("SELECT * FROM PRODUCT")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        final List<Product> result = productRepository.getAll();

        verify(statement, times(1)).executeQuery();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGet() throws SQLException {
        final Product product1 = new Product("product1", 5);
        final Product product2 = new Product("product2", 10);

        final Connection connection = mock(Connection.class);
        final ProductRepository productRepository = new ProductRepositoryImpl(() -> connection);
        final PreparedStatement statement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(connection.prepareStatement("SELECT * FROM PRODUCT")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn(product1.getName()).thenReturn(product2.getName());
        when(resultSet.getLong("price")).thenReturn(product1.getPrice()).thenReturn(product2.getPrice());

        final List<Product> result = productRepository.getAll();

        verify(statement, times(1)).executeQuery();
        assertEquals(result.size(), 2);
        assertEquals(product1, result.get(0));
        assertEquals(product2, result.get(1));
    }

    @Test
    public void testMax() throws SQLException {
        final Product product = new Product("product", 5);

        final Connection connection = mock(Connection.class);
        final ProductRepository productRepository = new ProductRepositoryImpl(() -> connection);
        final PreparedStatement statement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(connection.prepareStatement("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn(product.getName());
        when(resultSet.getLong("price")).thenReturn(product.getPrice());

        final Optional<Product> result = productRepository.getMaxPriceProduct();

        verify(statement, times(1)).executeQuery();
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    public void testMin() throws SQLException {
        final Product product = new Product("product", 5);

        final Connection connection = mock(Connection.class);
        final ProductRepository productRepository = new ProductRepositoryImpl(() -> connection);
        final PreparedStatement statement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(connection.prepareStatement("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn(product.getName());
        when(resultSet.getLong("price")).thenReturn(product.getPrice());

        final Optional<Product> result = productRepository.getMinPriceProduct();

        verify(statement, times(1)).executeQuery();
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    public void testSum() throws SQLException {
        final long summaryPrice = 100;

        final Connection connection = mock(Connection.class);
        final ProductRepository productRepository = new ProductRepositoryImpl(() -> connection);
        final PreparedStatement statement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(connection.prepareStatement("SELECT SUM(price) FROM PRODUCT")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(summaryPrice);

        final long result = productRepository.getSummaryPrice();

        verify(statement, times(1)).executeQuery();
        assertEquals(summaryPrice, result);
    }

    @Test
    public void testCount() throws SQLException {
        final int count = 25;

        final Connection connection = mock(Connection.class);
        final ProductRepository productRepository = new ProductRepositoryImpl(() -> connection);
        final PreparedStatement statement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        when(connection.prepareStatement("SELECT COUNT(*) FROM PRODUCT")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(count);

        final long result = productRepository.count();

        verify(statement, times(1)).executeQuery();
        assertEquals(count, result);
    }
}
