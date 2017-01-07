package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.application.ProductCatalog;
import pl.com.bottega.photostock.sales.infrastructure.InMemoryProductRepository;
import pl.com.bottega.photostock.sales.model.Address;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.VipClient;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.Scanner;

public class LightBoxMain {

    private MainScreen mainScreen;
    private SearchScreen searchScreen;
    private ReservationScreen reservationScreen;
    private OfferScreen offerScreen;

    public LightBoxMain(){
        Scanner scanner = new Scanner(System.in);
        ProductCatalog productCatalog = new ProductCatalog(new InMemoryProductRepository());
        Client client = new VipClient("John Doe", new Address(), Money.ZERO, Money.valueOf(2000));
        searchScreen = new SearchScreen(scanner, productCatalog, client);
        reservationScreen = new ReservationScreen(scanner);
        offerScreen = new OfferScreen(scanner);
        mainScreen = new MainScreen(scanner, searchScreen, reservationScreen, offerScreen);

    }

    public void start(){
        mainScreen.print();
    }

    public static void main(String[] args) {
        new LightBoxMain().start();
    }

}
