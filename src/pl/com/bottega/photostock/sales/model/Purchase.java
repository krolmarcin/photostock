package pl.com.bottega.photostock.sales.model;

import java.util.*;

public class Purchase {

    private Client client;
    private Date purchaseDate;
    private List<Product> items;

    public Purchase(Client client, Collection<Product> items) {
        this.client = client;
        this.items = new LinkedList<>(items);
        sortProductsByNumberAsc();
    }

    public Purchase(Client client, Product... items) {
        this(client, Arrays.asList(items));
    }

    public int getItemsCount() {
        return items.size();
    }

    private void sortProductsByNumberAsc() {
        this.items.sort(new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                String n1 = p1.getNumber();
                String n2 = p2.getNumber();
                return n1.compareTo(n2);
            }
        });
    }

}
