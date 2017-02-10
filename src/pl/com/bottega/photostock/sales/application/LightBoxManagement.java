package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.client.ClientRepository;
import pl.com.bottega.photostock.sales.model.lightbox.LightBox;
import pl.com.bottega.photostock.sales.model.lightbox.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.product.Product;
import pl.com.bottega.photostock.sales.model.product.ProductRepository;

import java.sql.SQLException;
import java.util.Collection;

public class LightBoxManagement {

    private PurchaseProcess purchaseProcess;
    private final LightBoxRepository lightBoxRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    public LightBoxManagement(PurchaseProcess purchaseProcess, LightBoxRepository lightBoxRepository, ProductRepository productRepository,
                              ClientRepository clientRepository) {
        this.purchaseProcess = purchaseProcess;
        this.lightBoxRepository = lightBoxRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    public Collection<String> getLightBoxNames(String customerNumber) throws SQLException {
        Client client = getClient(customerNumber);
        return lightBoxRepository.getLightBoxNames(client);
    }

    private Client getClient(String customerNumber) {
        Client client = clientRepository.get(customerNumber);
        if (client == null)
            throw new IllegalArgumentException(String.format("No client with number %s", customerNumber));
        return client;
    }

    public LightBox getLightBox(String customerNumber, String lightBoxName) throws SQLException {
        Client client = getClient(customerNumber);
        LightBox lightBox = lightBoxRepository.findLightBox(client, lightBoxName);
        ensureLightBoxFound(lightBoxName, lightBox);
        return lightBox;
    }

    public void addProduct(String customerNumber, String lightBoxName, String productNumber) throws SQLException {
        Client client = getClient(customerNumber);
        Product product = productRepository.get(productNumber);
        if (product == null) {
            throw new IllegalArgumentException(String.format("No product with number %s", productNumber));
        }
        LightBox lightBox = getOrCreateLightBox(lightBoxName, client);
        lightBox.add(product);
        lightBoxRepository.put(lightBox);
    }

    public void removeProduct(String customerNumber, String lightBoxName, String productNumber) throws SQLException {
        Client client = getClient(customerNumber);
        Product product = productRepository.get(productNumber);
        if (product == null){
            throw new IllegalStateException(String.format("No product with number %s", productNumber));
        }
        LightBox lightBox = getOrCreateLightBox(lightBoxName, client);
        lightBox.remove(product);
        lightBoxRepository.put(lightBox);
    }

    private LightBox getOrCreateLightBox(String lightBoxName, Client client) throws SQLException {
        LightBox lightBox = lightBoxRepository.findLightBox(client, lightBoxName);
        if (lightBox == null) {
            lightBox = new LightBox(client, lightBoxName);
        }
        return lightBox;
    }

    private void ensureLightBoxFound(String lightBoxName, LightBox lightBox) {
        if (lightBox == null)
            throw new IllegalArgumentException(String.format("No LightBox with the given name %s", lightBoxName));
    }

    public void reserve(String clientNumber, String lightBoxName) throws SQLException {
        LightBox lightBox = getLightBox(clientNumber, lightBoxName);
        String reservationNumber = purchaseProcess.getReservation(clientNumber);
        for (Product product : lightBox) {
            if (product.isAvaliable())
                purchaseProcess.add(reservationNumber, product.getNumber());
        }
    }


}

