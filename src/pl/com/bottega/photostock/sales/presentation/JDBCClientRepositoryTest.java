package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.infrastructure.jdbc.JDBCClientRepository;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.client.ClientRepository;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCClientRepositoryTest {

    public static void main(String[] args) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/photostock", "SA", "");
        ClientRepository clientRepository = new JDBCClientRepository(c);



        /*
        Client client = clientRepository.get("200");
        client.recharge(Money.valueOf(20.0f));
        client.charge(Money.valueOf(20.0f), "test charge'a");
        clientRepository.update(client);
        */

        Client client = clientRepository.get("100");
        client.activate();
        clientRepository.update(client);

        System.out.println(client.getName());
        System.out.println(client.getTransactions().size());

    }

}
