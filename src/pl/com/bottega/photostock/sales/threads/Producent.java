package pl.com.bottega.photostock.sales.threads;

import java.util.UUID;

public class Producent implements Runnable {

    private final Store2 store;
    private final int i;

    public Producent(Store2 store, int i) {
        this.store = store;
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println(String.format("started producent %d", i));
        while (true) {
            String product = UUID.randomUUID().toString();
            try {
                Thread.sleep((long) (Math.random() * 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("%d produced %s", i, product));
            store.put(product);
        }
    }

}
