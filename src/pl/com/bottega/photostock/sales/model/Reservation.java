package pl.com.bottega.photostock.sales.model;

import java.util.*;

public class Reservation {

    private Client client;

    private Collection<Product> items;

    private String number;

    public Reservation(Client client) {
        this.client = client;
        this.number = UUID.randomUUID().toString();
        this.items = new LinkedList<>();
    }

    public void add(Product product) {
        if (items.contains(product))
            throw new IllegalArgumentException(String.format("Product %s is already in your this reservation", product.getNumber()));
        product.ensureAvailable();
        items.add(product);
        product.reservedPer(client);
    }

    public void remove(Product product) {
        if (!items.contains(product))
            throw new IllegalArgumentException(String.format("Product %s is not added to this reservation.", product));
        items.remove(product);
        product.unreservedPer(client);
    }

    public Offer generateOffer() {
        return new Offer(client, getActiveItems());
    }

    private Collection<Product> getActiveItems() {
        Collection<Product> activeItems = new HashSet<>();
        for (Product product : items)
            if (product.isActive())
                activeItems.add(product);
        return activeItems;
    }

    public int getItemsCount() {
        return items.size();
    }

    public String getNumber() {
        return number;
    }

    public Client getClient() {
        return client;
    }

    public boolean isOwnedBy(String clientNumber) {
        return client.getNumber().equals(clientNumber);
    }

}
