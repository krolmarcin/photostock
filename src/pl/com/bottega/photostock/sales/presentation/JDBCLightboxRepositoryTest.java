package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.infrastructure.jdbc.JDBCClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.jdbc.JDBCLightBoxRepository;
import pl.com.bottega.photostock.sales.infrastructure.memory.InMemoryProductRepository;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.client.ClientRepository;
import pl.com.bottega.photostock.sales.model.lightbox.LightBox;
import pl.com.bottega.photostock.sales.model.lightbox.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.product.Product;
import pl.com.bottega.photostock.sales.model.product.ProductRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

public class JDBCLightboxRepositoryTest {
    public static void main(String[] args) throws SQLException {

        Connection c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/photostock", "SA", "");
        ClientRepository clientRepository = new JDBCClientRepository(c);
        ProductRepository productRepository = new InMemoryProductRepository();
        LightBoxRepository lightBoxRepository = new JDBCLightBoxRepository(c, productRepository);


        Client client1 = clientRepository.get("100");
        //Client client2 = clientRepository.get("200");
        //Client client3 = clientRepository.get("300");
        Product product1 = productRepository.get("1");
        Product product2 = productRepository.get("2");
        Product product3 = productRepository.get("3");


        LightBox l1 = lightBoxRepository.findLightBox(client1, "test1");
        //l1.add(product1);
        //l1.add(product2);
        l1.remove(product2);
        //l1.add(product3);

        lightBoxRepository.put(l1);

        printLightboxes(lightBoxRepository.getFor(client1));
        //printLightboxes(lightBoxRepository.getFor(client2));
        //printLightboxes(lightBoxRepository.getFor(client3));

    }

    private static void printLightboxes(Collection<LightBox> lightboxes) {
        int nr = 1;
        for (LightBox lightbox : lightboxes) {
            System.out.println(String.format("%d. %s - %s", nr, lightbox.getName(), lightbox.getOwner().getName()));
            printLightbox(lightbox);
            nr++;
        }
    }

    private static void printLightbox(LightBox lightbox) {
        for (Product product : lightbox) {
            System.out.println(
                    String.format("%s%s | %s",
                            (product.isActive() ? "" : "X "),
                            product.getNumber(),
                            product.calculatePrice(lightbox.getOwner())));
        }
    }

}
