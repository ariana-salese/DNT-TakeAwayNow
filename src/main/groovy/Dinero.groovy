package buffet

class Dinero implements Comparable<Dinero> {

    BigDecimal monto

    private Dinero() {
    }

    Dinero(BigDecimal monto) {
        if (monto < 0) throw new IllegalStateException()
        this.monto = monto
    }

    Dinero plus(Dinero otro) {
        new Dinero(this.monto + otro.monto)
    }

    Dinero minus(Dinero otro) {
        new Dinero(this.monto - otro.monto)
    }

    int compareTo(Dinero otro) {
        // one liner
        this.monto <=> otro.monto
        // this.monto.compareTo(otro.monto)
    }
}