package pl.com.bottega.photostock.sales.model;

public class VipClient extends Client {

    private Money creditLimit;

    public VipClient(String name, Address address, Money initialBalance, Money creditLimit) {
        super(name, address, ClientStatus.VIP, initialBalance);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean canAfford(Money money) {
        return balance.add(creditLimit).gte(money);
    }

}
