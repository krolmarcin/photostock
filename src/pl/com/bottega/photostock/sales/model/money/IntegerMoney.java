package pl.com.bottega.photostock.sales.model.money;

public class IntegerMoney implements Money {

    private long cents;
    private Currency currency;

    public IntegerMoney(long cents, Currency currency) {
        this.cents = cents;
        this.currency = currency;
    }

    @Override
    public Money add(Money add) {
        IntegerMoney integerMoney = safeConvert(add);
        ensureSameCurrenry(integerMoney);
        return new IntegerMoney(cents + integerMoney.cents, currency);
    }

    @Override
    public Money subtract(Money subtrahend) {
        IntegerMoney integerMoney = safeConvert(subtrahend);
        ensureSameCurrenry(integerMoney);
        return new IntegerMoney(cents - integerMoney.cents, currency);
    }

    @Override
    public Money multiply(long factor) {
        if (factor < 0)
            throw new IllegalArgumentException("IntegerMoney cannot be negative");
        return new IntegerMoney(cents * factor, currency);
    }

    @Override
    public Money opposite() {
        return new IntegerMoney(-cents, currency);
    }

    @Override
    public RationalMoney convertToRational() {
        Rational rational = Rational.valueOf(cents, 100);
        return new RationalMoney(rational, currency);
    }

    @Override
    public IntegerMoney convertToInteger() {
        return this;
    }

    @Override
    public int compareTo(Money o) {
        IntegerMoney integerMoney = safeConvert(o);
        if (cents == integerMoney.cents)
            return 0;
        if (cents < integerMoney.cents)
            return -1;
        else
            return 1;
//        return cents < integerMoney.cents ? -1 : 1;
    }

    private void ensureSameCurrenry(IntegerMoney other) {
        if (currency != other.currency)
            throw new IllegalArgumentException(String.format("Currency %s mismatch %s", currency, other));
    }

    private IntegerMoney safeConvert(Money other) {
        IntegerMoney integerMoney = other.convertToInteger();
        ensureSameCurrenry(integerMoney);
        return integerMoney;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Money))
            return false;
        IntegerMoney integerMoney;
        if (other instanceof RationalMoney)
            integerMoney = ((RationalMoney) other).convertToInteger();
        else
            integerMoney = (IntegerMoney) other;
        return integerMoney.cents == cents && integerMoney.currency == currency;
    }

    @Override
    public int hashCode() {
        int result = (int) (cents ^ (cents >>> 32));
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d.%02d %s", cents / 100, cents % 100, currency.name());
    }

    public long toCents() {
        return cents;
    }
}
