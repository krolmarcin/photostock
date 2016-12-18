package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.model.*;

public class LightBoxTest {

    public static void main(String[] args) {

        ProductRepository productRepository = new InMemoryProductRepository();
        Product product1 = productRepository.get("1");
        Product product2 = productRepository.get("2");
        Product product3 = productRepository.get("3");

        Client client1 = new Client("Johny X", new Address(), Money.valueOf(100));
        Client client2 = new Client("Johny X", new Address(), Money.valueOf(100));

        LightBox l1 = new LightBox(client1, "Samochody");
        LightBox l2 = new LightBox(client1, "Bmw");
        LightBox l3 = new LightBox(client2, "Wyściogowe Samochody");

        l1.add(product1);
        l1.add(product2);
        l1.add(product3);

        l2.add(product1);

        l3.add(product3);

        product3.deactivate();

        printLightboxes(l1, l2, l3);

        LightBox l4 = LightBox.joined(client1, "Joined lightboxes", l1, l2, l3);
        System.out.println("Joined lightbox");
        printLightbox(l4);
    }

    private static void printLightboxes(LightBox... lightboxes) {
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
