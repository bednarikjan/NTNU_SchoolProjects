package gps;

/**
 * Class for storing the pair of integers.
 */
public class Pair {

    /* Properties */
    protected int a;
    protected int b;

    /* Methods */
    // constructor
    public Pair(int a, int b) {
        this.a = a;
        this.b = b;
    }

    // getter
    public int geta() {
        return this.a;
    }

    public int getb() {
        return this.b;
    }

    public void swap() {
        int aux = a;
        a = b;
        b = aux;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }

        Pair other = (Pair) o;

        return this.a == other.a && this.b == other.b;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.a;
        hash = 97 * hash + this.b;
        return hash;
    }
}
