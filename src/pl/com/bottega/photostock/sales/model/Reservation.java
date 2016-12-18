package pl.com.bottega.photostock.sales.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class Reservation {

    private Client client;
    private Collection<Product> items;

    public Reservation(Client client) {
        this.client = client;
        this.items = new LinkedList<>();
    }

    public void add(Product product) {
        if (items.contains(product))
            throw new IllegalArgumentException(String.format("Product %s is already in your this reservation", product.getNumber()));
        product.ensureAvailable();
        items.add(product);
    }

    public void remove(Product product) {
        if (!items.contains(product))
            throw new IllegalArgumentException(String.format("Product %s is not added to this reservation.", product));
        items.remove(product);
    }

    public Offer generateOffer() {
        return new Offer(client, getActiveItems());
    }

    private Collection<Product> getActiveItems(){
    Collection<Product> activeItems = new HashSet<>();
        for (Product product : items)
            if (product.isActive())
            activeItems.add(product);
        return activeItems;
    }

    public int getItemsCount() {
        return items.size();
    }
}
