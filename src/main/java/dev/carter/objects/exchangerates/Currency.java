package dev.carter.objects.exchangerates;

public enum Currency {
    GBP(0, "£"),
    USD(0, "$"),
    CAD(0, "$"),
    EUR(0, "€"),
    INR(0, "₹");

    private double value;

    private final String symbol;
    private final String name;

    private Currency(double value, String symbol) {
        this.value = value;
        this.symbol = symbol;
        this.name = toString();
    }

    //Getters
    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getValue() {
        return value;
    }

    //Setter
    public void setValue(double value) {
        this.value = value;
    }
}