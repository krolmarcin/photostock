package pl.com.bottega.photostock.sales.model.money;

class IntegerMoney implements Money {

    private long cents;
    private Currency currency;

    IntegerMoney(long cents, Currency currency) {
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

    //TODO napisać equals i hashcode samodzielnie

    private void ensureSameCurrenry(IntegerMoney other) {
        if (currency != other.currency)
            throw new IllegalArgumentException(String.format("Currency %s mismatch %s", currency, other));
    }

    private IntegerMoney safeConvert(Money other) {
        IntegerMoney integerMoney = other.convertToInteger();
        ensureSameCurrenry(integerMoney);
        return integerMoney;
    }

}
