package pl.com.bottega.photostock.sales.threads;

import java.util.LinkedList;
import java.util.List;

public class Store {

    private volatile List<String> products = new LinkedList<>();

    public synchronized void put(String product) {
        products.add(product);
        notify();
    }

    public synchronized String get() {
        while (products.isEmpty())
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        String product = products.remove(0);
        return product;
    }

}
