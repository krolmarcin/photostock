package pl.com.bottega.photostock.sales.infrastructure.jdbc;

import com.sun.deploy.util.StringUtils;
import pl.com.bottega.photostock.sales.infrastructure.csv.DataAccessException;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.lightbox.LightBox;
import pl.com.bottega.photostock.sales.model.lightbox.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.product.Product;
import pl.com.bottega.photostock.sales.model.product.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class JDBCLightBoxRepository implements LightBoxRepository {

    private static final String GET_LIGHTBOX_NAME_SQL = "" +
            "SELECT lightboxes.name " +
            "FROM lightboxes " +
            "WHERE lightboxes.client_id = ? AND lightboxes.name LIKE ?";

    private static final String GET_LIGHTBOXES_NAMES_SQL = "" +
            "SELECT lightboxes.name " +
            "FROM lightboxes " +
            "WHERE client_id = ?";

    private static final String GET_LIGHTBOX_FOR_CLIENT_ID_SQL = "" +
            "SELECT lightboxes.name, lightboxes.numberofproducts " +
            "FROM lightboxes " +
            "WHERE lightboxes.client_id = ?";

    private static final String INSERT_LIGHTBOX_SQL = "" +
            "INSERT INTO lightboxes (client_id, name, numberofproducts) VALUES (? , ?, ?)";

    private static final String UPDATE_LIGHTBOX_SQL = "" +
            "UPDATE lightboxes " +
            "SET lightboxes.numberofproducts = ? " +
            "WHERE lightboxes.name LIKE ? AND lightboxes.client_id = ?";

    private static final String GET_LIGHTBOX_NAME_AND_PRODUCTS_SQL = "" +
            "SELECT lightboxes.name , lightboxes.numberofproducts " +
            "FROM lightboxes " +
            "WHERE lightboxes.client_id = ? AND lightboxes.name LIKE ?";

    private ProductRepository productRepository;

    private Connection connection;

    public JDBCLightBoxRepository(Connection connection, ProductRepository productRepository) {
        this.connection = connection;
        this.productRepository = productRepository;
    }

    //client_id, name, numberofproducts|numberofproducts
    @Override
    public void put(LightBox lightBox) throws SQLException {
        Client owner = lightBox.getOwner();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_LIGHTBOX_NAME_SQL);
            preparedStatement.setInt(1, getClientId(owner));
            preparedStatement.setString(2, lightBox.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                insertNewLightbox(lightBox, owner);
            updateProducts(lightBox, owner);

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private void insertNewLightbox(LightBox lightBox, Client owner) throws SQLException {
        Collection<String> productNumbers = new LinkedList<>();
        for (Product product : lightBox) {
            productNumbers.add(product.getNumber());
        }
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LIGHTBOX_SQL);
        preparedStatement.setInt(1, getClientId(owner));
        preparedStatement.setString(2, lightBox.getName());
        preparedStatement.setString(3, StringUtils.join(productNumbers, "|"));
        preparedStatement.executeUpdate();
    }

    private void updateProducts(LightBox lightBox, Client owner) throws SQLException {
        Collection<String> productNumbers = new LinkedList<>();
        for (Product product : lightBox) {
            productNumbers.add(product.getNumber());
        }
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LIGHTBOX_SQL);
        preparedStatement.setString(1, (StringUtils.join(productNumbers, "|")));
        preparedStatement.setString(2, lightBox.getName());
        preparedStatement.setInt(3, getClientId(owner));
        preparedStatement.executeUpdate();
    }

    @Override
    public Collection<LightBox> getFor(Client client) throws SQLException {
        Collection<LightBox> lightBoxes = new HashSet<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_LIGHTBOX_FOR_CLIENT_ID_SQL);
            preparedStatement.setInt(1, getClientId(client));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LightBox lightBox = parseLightBox(client, resultSet);
                lightBoxes.add(lightBox);
            }
            return lightBoxes;

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private LightBox parseLightBox(Client client, ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        String numberOfProducts = resultSet.getString("numberofproducts");
        return new LightBox(client, name, getProducts(numberOfProducts));
    }

    private Collection<Product> getProducts(String components) {
        Collection<Product> products = new LinkedList<>();
        String[] numbers = components.split("\\|");
        for (String number : numbers) {
            Product product = productRepository.get(number);
            if (product == null)
                throw new IllegalArgumentException(String.format("Product %s does not exist in productRepository", product.getNumber()));
            products.add(product);
        }
        return products;
    }

    @Override
    public LightBox findLightBox(Client client, String lightBoxName) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_LIGHTBOX_NAME_AND_PRODUCTS_SQL);
            preparedStatement.setInt(1, getClientId(client));
            preparedStatement.setString(2, lightBoxName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String numberOfProducts = resultSet.getString("numberofproducts");
                if (numberOfProducts.equals(""))
                    return new LightBox(client, name);
                return new LightBox(client, name, getProducts(numberOfProducts));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return null;
    }

    @Override
    public Collection<String> getLightBoxNames(Client client) throws SQLException {
        Collection<String> lightBoxNames = new LinkedList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_LIGHTBOXES_NAMES_SQL);
            preparedStatement.setInt(1, getClientId(client));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                lightBoxNames.add(name);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return lightBoxNames;
    }

    private int getClientId(Client client) throws SQLException {
        JDBCClientRepository jdbcClientRepository = new JDBCClientRepository(connection);
        int clientId = jdbcClientRepository.getClientId(client);
        return clientId;
    }

}