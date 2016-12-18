package pl.com.bottega.photostock.sales.model;

import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.Collection;
import java.util.HashSet;

public class Picture extends AbstractProduct {

    private Collection<String> tags;

    public Picture(String number, String name, Collection<String> tags, Money catalogPrice, boolean active) {
        super(number, name, catalogPrice, active);
        this.tags = new HashSet<String>(tags);  //this.tags.addAll(tags); - to wpadło do ()
    }

    public Picture(String number, String name, Collection<String> tags, Money catalogPrice) {
        this(number, name, tags, catalogPrice, true);
    }

    @Override
    public Money calculatePrice(Client client) {
        return catalogPrice;
    }

}
