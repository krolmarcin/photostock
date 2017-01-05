package pl.com.bottega.photostock.sales.model.money;

class RationalMoney implements Money {

    public Money opposite() {
        return new RationalMoney(value.negative(), currency);
    }

    public Money add(Money addend) {
        RationalMoney rationalMoney = addend.convertToRational();
        if (currency != rationalMoney.currency)
            throw new IllegalArgumentException("The currencies do not match.");
        return new RationalMoney(value.add(rationalMoney.value), currency);
    }

    public Money subtract(Money subtrahend) {
        RationalMoney rationalMoney = subtrahend.convertToRational();
        if (currency != rationalMoney.currency)
            throw new IllegalArgumentException("The currencies do not match.");
        return new RationalMoney(value.subtract(rationalMoney.value), currency);
    }

    public Money multiply(long factor) {
        if (factor < 0)
            throw new IllegalArgumentException("RationalMoney cannot be negative");
        return new RationalMoney(value.multiply(factor), currency);
    }

    @Override
    public int compareTo(Money other) {
        RationalMoney rationalMoney = other.convertToRational();
        if (!rationalMoney.equals(currency))
            throw new IllegalArgumentException("Currency mismatch");
        return value.compareTo(rationalMoney.value);
    }

    private final Rational value;
    private final Currency currency;

    RationalMoney(Rational value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return value.toDouble() + " " + currency.name();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        //if (!(o instanceof RationalMoney)) return false;

        RationalMoney money;

        if (o instanceof IntegerMoney){
            IntegerMoney objAsInteger = (IntegerMoney) o;
            money = objAsInteger.convertToRational();
        } else {
            money = (RationalMoney) o;
        }

        if (!value.equals(money.value)) return false;
        return currency == money.currency;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public RationalMoney convertToRational() {
        return this;
    }

    @Override
    public IntegerMoney convertToInteger() {
        long cents = value.getNumerator() * 100 / value.getDenomonator();
        return new IntegerMoney(cents, currency);
    }

}
