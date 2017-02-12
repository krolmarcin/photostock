package pl.com.bottega.photostock.sales.threads;

public class App {

    private static final int CONSUMENT_COUNT = 15;
    private static final int PRODUCENT_COUNT = 10;

    public static void main(String[] args) {

        Store2 store = new Store2();

        for (int i = 0; i < CONSUMENT_COUNT; i++) {
            new Thread(new Consument(store, i)).start();
        }

        for (int i = 0; i < PRODUCENT_COUNT; i++) {
            new Thread(new Producent(store, i)).start();
        }
    }

}
