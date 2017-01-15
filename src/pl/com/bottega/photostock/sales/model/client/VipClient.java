package pl.com.bottega.photostock.sales.model.client;

import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.Collection;

public class VipClient extends Client {

    private Money creditLimit;

    public VipClient(String name, Address address, Money initialBalance, Money creditLimit) {
        super(name, address, ClientStatus.VIP, initialBalance);
        this.creditLimit = creditLimit;
    }

    public VipClient(String number, String name, Address address,
                     Money initialBalance, Money creditLimit, boolean active, Collection<Transaction> transactions) {
        super(number, name, address, ClientStatus.VIP, initialBalance, active, transactions);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean canAfford(Money money) {
        return balance.add(creditLimit).gte(money);
    }

    public Money getCreditLimit() {
        return creditLimit;
    }
}
