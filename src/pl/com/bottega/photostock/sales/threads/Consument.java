package pl.com.bottega.photostock.sales.threads;

public class Consument implements Runnable {

    private final Store2 store;
    private final int i;

    public Consument(Store2 store, int i) {
        this.store = store;
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println(String.format("Started consument %d", i));
        while (true) {
            String product = store.get();
            try {
                Thread.sleep((long) Math.random() * 3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("%d consumed %s", i, product));
        }
    }

}
