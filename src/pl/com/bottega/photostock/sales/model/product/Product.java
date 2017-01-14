package pl.com.bottega.photostock.sales.model.product;

import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.money.Money;

public interface Product {

    Money calculatePrice(Client client);

    boolean isAvaliable();

    void reservedPer(Client client);

    void unreservedPer(Client client);

    void soldPer(Client client);

    String getNumber();

    boolean isActive();

    void deactivate();

    String getName();

    default void ensureAvailable() {
        if (!isAvaliable())
            throw new ProductNotAvailableException(this);
    }

}
