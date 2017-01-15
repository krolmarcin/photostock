package pl.com.bottega.photostock.sales.model.money;

public interface Money extends Comparable<Money> {

    enum Currency {CREDIT;}

    Currency DEFAULT_CURRENCY = Currency.CREDIT;

    Money ZERO = valueOf(0, DEFAULT_CURRENCY);

    Money add(Money add);

    Money subtract(Money subtrahend);

    Money multiply(long factor);

    default boolean gte(Money other) {
        return compareTo(other) >= 0;
    }

    default boolean gt(Money other) {
        return compareTo(other) > 0;
    }

    default boolean lte(Money other) {
        return compareTo(other) <= 0;
    }

    default boolean lt(Money other) {
        return compareTo(other) < 0;
    }

    Money opposite();

    RationalMoney convertToRational();

    IntegerMoney convertToInteger();

    static Money valueOf(long value, Currency currency) {
        return new IntegerMoney(value * 100L, currency);
    }

    static Money valueOf(long value) {
        return new IntegerMoney(value * 100L, DEFAULT_CURRENCY);
    }

    static Money valueOf(Rational value, Currency currency) {
        return new RationalMoney(value, currency).convertToInteger();
    }

    static Money valueOf(float value) {
        return new IntegerMoney((long) (value * 100.0), DEFAULT_CURRENCY);
    }

    //100.00 CREDIT
    static Money valueOf(String moneyString) {
        String[] moneyComponents = moneyString.split(" ");
        if (moneyComponents.length != 1 && moneyComponents.length != 2)
            throw new IllegalArgumentException("Invalid money format");
        long value = (long) (Double.parseDouble(moneyComponents[0]) * 100.0);
        if (moneyComponents.length == 2) {
            return new IntegerMoney(value, Currency.valueOf(moneyComponents[1]));
        } else {
            return new IntegerMoney(value, DEFAULT_CURRENCY);
        }
    }

}
