package pl.com.bottega.photostock.sales.model.client;

import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.Collection;
import java.util.LinkedList;

public class Client {

    private static int clientNumber;

    private String name;

    private Address address;

    private ClientStatus status;

    protected Money balance;

    private boolean active = true;

    private String number;

    private Collection<Transaction> transactions;

    public Client(String name, Address address, ClientStatus status, Money initialBalance) {
        this(nextNumber(), name, address, status, initialBalance, true, new LinkedList<>());
        if (!initialBalance.equals(Money.ZERO))
            this.transactions.add(new Transaction(initialBalance, "Openning account"));
    }

    public Client(String number, String name, Address address, ClientStatus status,
                  Money initialBalance, boolean active, Collection<Transaction> transactions) {
        this.number = number;
        this.name = name;
        this.address = address;
        this.status = status;
        this.balance = initialBalance;
        this.active = active;
        this.transactions = new LinkedList<>(transactions);
    }


    private static String nextNumber() {
        clientNumber += 100;
        return String.valueOf(clientNumber);
    }

    public Client(String name, Address address, Money balance) {
        this(name, address, ClientStatus.STANDARD, balance);
    }

    public boolean canAfford(Money money) {
        return balance.gte(money);
    }

    public void charge(Money money, String reason) {
        if (money.lte(Money.ZERO))
            throw new IllegalArgumentException("Negative charge amount prohibited");
        if (canAfford(money)) {
            Transaction chargeTransaction = new Transaction(money.opposite(), reason);
            transactions.add(chargeTransaction);
            balance = balance.subtract(money);
        } else {
            String template = "Client balance is %s and requested amount was %s";
            String mesage = String.format(template, balance, money);
            throw new CantAffordException(mesage);
        }
    }

    public void recharge(Money money) {
        if (money.lte(Money.ZERO))
            throw new IllegalArgumentException("Negative recharge amount prohibited");
        Transaction transaction = new Transaction(money, "Recharge account");
        transactions.add(transaction);
        balance = balance.add(money);
    }

    public String getName() {
        return name;
    }

    public String introduce() {
        return String.format("%s - %s", name, status.getStatusName());
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public String getNumber() {
        return number;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public Money getBalance() {
        return balance;
    }

    public Collection<Transaction> getTransactions() {
        return new LinkedList<>(transactions);
    }

}
