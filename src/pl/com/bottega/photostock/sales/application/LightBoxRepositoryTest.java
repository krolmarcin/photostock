package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.Collection;

public class LightBoxRepositoryTest {

    public static void main(String[] args) {

        InMemoryLightBoxRepository repository = new InMemoryLightBoxRepository();
        ProductRepository productRepository = new InMemoryProductRepository();

        Client client1 = new Client("Stefan", new Address(), Money.valueOf(100));
        Client client2 = new Client("Johny Z", new Address(), Money.valueOf(80));

        Product product1 = productRepository.get("1");
        Product product2 = productRepository.get("2");
        Product product3 = productRepository.get("3");

        LightBox l1 = new LightBox(client1, "Samochody");
        LightBox l2 = new LightBox(client1, "Bmw");
        LightBox l3 = new LightBox(client2, "Wyściogowe Samochody");

        l1.add(product1);
        l1.add(product2);
        l1.add(product3);
        l2.add(product1);
        l3.add(product3);

        repository.put(l1);
        repository.put(l2);
        repository.put(l3);

        printLightboxes(repository.getFor(client1));
        printLightboxes(repository.getFor(client2));
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
