public class Num implements Token {
    private double value;
    public Num(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(Num " + value + ")";
    }
}
