package pl.com.bottega.photostock.sales.infrastructure.jdbc;

import pl.com.bottega.photostock.sales.infrastructure.csv.DataAccessException;
import pl.com.bottega.photostock.sales.model.client.*;
import pl.com.bottega.photostock.sales.model.money.IntegerMoney;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class JDBCClientRepository implements ClientRepository {

    private static final String GET_CLIENT_SQL = "SELECT * FROM clients WHERE number = ?";

    private Connection connection;

    public JDBCClientRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client get(String clientNumber) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENT_SQL);
            preparedStatement.setString(1, clientNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            return parseClient(resultSet);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private Client parseClient(ResultSet resultSet) throws SQLException {
        String number = resultSet.getString("number");
        String name = resultSet.getString("name");
        Boolean active = resultSet.getBoolean("active");
        ClientStatus status = ClientStatus.valueOf(resultSet.getString("status").trim());
        Money balance = new IntegerMoney(resultSet.getLong("balance"), Money.DEFAULT_CURRENCY);
        if (status == ClientStatus.VIP) {
            Money creditLimit = new IntegerMoney(resultSet.getLong("creditLimit"), Money.DEFAULT_CURRENCY);
            return new VipClient(number, name, new Address(), balance, creditLimit, active, getTransactions(number));
        }
        return new Client(number, name, new Address(), status, balance, active, getTransactions(number));
    }

    private Collection<Transaction> getTransactions(String number) {
        return new LinkedList<>();
    }

    @Override
    public void update(Client client) {

    }
}
