package pl.com.bottega.photostock.sales.model;

import java.util.Collection;

public interface ProductRepository {

    void put(Product product);

    Product get(String number);

}
