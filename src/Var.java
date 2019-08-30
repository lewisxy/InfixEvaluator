public class Var implements Token {
    private String name;
    public Var(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "(Var " + name + ")";
    }
}
