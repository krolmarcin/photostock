package pl.com.bottega.photostock.sales.threads;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Store2 {

    private BlockingQueue<String> products = new LinkedBlockingQueue<>();

    public void put(String product) {
        products.add(product);
    }

    public String get() {
        try {
            return products.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
