package takeawaynow

/**
 * 
 * El dinero representa pesos argentinos utilizados para dar valor a compras, productos y
 * al saldo disponible de los clientes.
 * 
 */
class Dinero implements Comparable<Dinero> {

    static constraints = {
        monto nullable: false, blank: false
        cliente display:false, nullable: true
    }

    static belongsTo = [cliente: Cliente]
    BigDecimal monto
    

    /**
     * 
     * Crea un dinero con el monto indicado. Si el monto es negativo se lanza un error.
     * 
     */
    Dinero(BigDecimal monto) {
        if (monto < 0) throw new IllegalStateException("No puede existir dinero negativo.")
        this.monto = monto
    }

    /**
     * 
     * Retorna un nuevo dinero resultante de sumar los montos.
     * 
     */
    Dinero plus(Dinero otro) {
        new Dinero(this.monto + otro.monto)
    }

    /**
     * 
     * Retorna un nuevo dinero resultante de restar los montos. Si el monto resultante
     * es negativo se lanza un error.
     * 
     */
    Dinero minus(Dinero otro) {
        new Dinero(this.monto - otro.monto)
    }

    /**
     * 
     * Compara dinero segun el monto. 
     * 
     */
    int compareTo(Dinero otro) {
        this.monto <=> otro.monto
    }

    /**
     * 
     * Multiplica el monto actual y la cantidad indicada. Si la cantidad es negativa se lanza 
     * un error.
     * 
     */
    Dinero multiply(int cantidad) {
        if (cantidad <= 0) throw new IllegalStateException("No se puede multiplicar el dinero por un numero negativo.")
        new Dinero(this.monto * cantidad)
    }

    /**
     * 
     * Multiplica el monto actual y la cantidad indicada. Si la cantidad es negativa se lanza 
     * un error.
     * 
     */
    Dinero multiply(Dinero otro) {
        if (otro.monto <= 0) throw new IllegalStateException("No se puede multiplicar el dinero por un numero negativo.")
        new Dinero(this.monto * otro.monto)
    }
}
