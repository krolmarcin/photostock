package pl.com.bottega.photostock.sales.model.money;


public class MoneyTest {

    public static void main(String[] args) {

        System.out.println(new IntegerMoney(0, Money.Currency.CREDIT).equals(new RationalMoney(Rational.valueOf(0), Money.Currency.CREDIT)));
        System.out.println(new IntegerMoney(100, Money.Currency.CREDIT).equals(new RationalMoney(Rational.valueOf(1), Money.Currency.CREDIT)));
        System.out.println(new IntegerMoney(150, Money.Currency.CREDIT).equals(new RationalMoney(Rational.valueOf(3, 2), Money.Currency.CREDIT)));
        System.out.println(new RationalMoney(Rational.valueOf(2), Money.Currency.CREDIT).equals(new RationalMoney(Rational.valueOf(2), Money.Currency.CREDIT)));
        System.out.println(new IntegerMoney(100, Money.Currency.CREDIT).equals(new IntegerMoney(100, Money.Currency.CREDIT)));
    }

}
