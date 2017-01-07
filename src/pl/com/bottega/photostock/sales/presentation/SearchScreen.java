package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.application.ProductCatalog;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SearchScreen {

    private final Scanner scanner;
    private final ProductCatalog productCatalog;
    private final Client client;

    public SearchScreen(Scanner scanner, ProductCatalog productCatalog, Client client){
        this.scanner = scanner;
        this.productCatalog = productCatalog;
        this.client = client;
    }

    public void print(){
        String name = getQuery();
        String[] tags = getTags();
        Money priceFrom = getMoney("Cena od");
        Money priceTo = getMoney("Cena do");

        List<Product> products = productCatalog.find(client, name, tags, priceFrom, priceTo);
        printProducts(client, products);


    }

    private void printProducts(Client client, List<Product> products) {
        System.out.println("Matching products: ");
        for (Product product : products) {
            System.out.println(String.format("%s | %s %s",
                    product.getNumber(),
                    product.getName(),
                    product.calculatePrice(client)));
        }
        System.out.println("-------------------------------");
    }

    private Money getMoney(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                float f = scanner.nextFloat();
                scanner.nextLine();
                return Money.valueOf(f);
            } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.out.println("Wprowadź poprawna cenę np.: 9,99");
            }
        }
    }

    private String[] getTags() {
        System.out.print("Tagi: ");
        String tagsRead = scanner.nextLine().trim();
        if (tagsRead.length() == 0)
            return null;
        return tagsRead.split(" ");
    }

    private String getQuery() {
        System.out.print("Nazwa: ");
        return scanner.nextLine();
    }

}
