package pl.com.bottega.photostock.sales.model.lightbox;

import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.product.Product;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class LightBox implements Iterable<Product> {

    private Client client;
    private String name;

    public LightBox(Client client, String name) {
        this.client = client;
        this.name = name;
    }

    private Collection<Product> items = new LinkedList<>();

    public void add(Product product) {
        if (items.contains(product))
            throw new IllegalArgumentException(String.format("This product %s already in this LightBox %s", product.getNumber(), name));
        product.ensureAvailable();
        items.add(product);
    }

    public void remove(Product product) {
        if (!items.contains(product))
            throw new IllegalArgumentException(String.format("This product %s is not in this Lightbox %s", product.getNumber(), name));
        items.remove(product);
    }

    public void rename(String newName) {
        this.name = newName;
    }

    @Override
    public Iterator<Product> iterator() {
        return items.iterator();
    }

    public String getName() {
        return name;
    }

    public Client getOwner() {
        return client;
    }

    public static LightBox joined(Client client, String name, LightBox... lightboxes) {
        LightBox output = new LightBox(client, name);
        output.join(lightboxes);
        return output;
    }

    private void join(LightBox... lightboxes) {
        for (LightBox lightbox : lightboxes)
            for (Product product : lightbox) {
                if (!items.contains(product) && product.isAvaliable())
                    items.add(product);
            }
    }

}
