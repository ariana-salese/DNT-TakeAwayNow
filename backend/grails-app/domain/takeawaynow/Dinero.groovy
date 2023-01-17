package takeawaynow

/**
 * 
 * TODO
 * 
 */
class Dinero implements Comparable<Dinero> {

    BigDecimal monto

    Dinero(BigDecimal monto) {
        if (monto < 0) throw new IllegalStateException("No puede existir dinero negativo.")
        this.monto = monto
    }

    /**
     * 
     * TODO
     * 
     */
    Dinero plus(Dinero otro) {
        new Dinero(this.monto + otro.monto)
    }

    /**
     * 
     * TODO
     * 
     */
    Dinero minus(Dinero otro) {
        new Dinero(this.monto - otro.monto)
    }

    /**
     * 
     * TODO
     * 
     */
    int compareTo(Dinero otro) {
        this.monto <=> otro.monto
    }

    /**
     * 
     * TODO
     * 
     */
    Dinero multiply(int cantidad) {
        if (cantidad <= 0) throw new IllegalStateException("No se puede multiplicar el dinero por un numero negativo.")
        new Dinero(this.monto * cantidad)
    }
}
