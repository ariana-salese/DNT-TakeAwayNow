package buffet

class Dinero implements Comparable<Dinero> {

    BigDecimal monto

    Dinero(BigDecimal monto) {
        if (monto < 0) throw new IllegalStateException("No puede existir dinero negativo.")
        this.monto = monto
    }

    Dinero plus(Dinero otro) {
        new Dinero(this.monto + otro.monto)
    }

    Dinero minus(Dinero otro) {
        new Dinero(this.monto - otro.monto)
    }

    int compareTo(Dinero otro) {
        this.monto <=> otro.monto
    }

    Dinero multiply(int cantidad) {
        if (cantidad <= 0) throw new IllegalStateException()
        new Dinero(this.monto * cantidad)
    }
}