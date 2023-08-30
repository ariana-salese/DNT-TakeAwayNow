package takeawaynow

/**
 * 
 * TODO
 * 
 */
class Producto {

    static constraints = {
    }

    static belongsTo = [almacen: Almacen, pedido: Pedido]

    //static embedded = ['precio']

    String nombre
    Dinero precio
    int cantidad
    PuntosDeConfianza puntosDeConfianza

    /**
     * 
     * TODO
     * 
     */
    Producto(String nombre, int cantidad, Dinero precio, PuntosDeConfianza puntosDeConfianza = new PuntosDeConfianza(0)) {
        if (cantidad <= 0) throw new IllegalStateException("No se puede inicializar un producto con cantidad igual a cero.") 
        if ((int)precio.monto <= 0) throw new IllegalStateException("No se puede inicializar un producto con precio menor o igual a cero.") 

        this.nombre = nombre
        this.precio = precio
        this.cantidad = cantidad
        this.puntosDeConfianza = puntosDeConfianza
    }

    /**
     * Devuelve el precio del Producto en función de la cantidad del mismo.
     */
    Dinero precioSegunCantidad() {
        this.precio * this.cantidad
    }

    /**
     * Devuelve los PuntosDeConfianza del Producto en función de la cantidad del mismo.
     */
    PuntosDeConfianza puntosDeConfianzaSegunCantidad() {
        this.puntosDeConfianza * this.cantidad
    }

    /**
     * 
     * TODO
     * 
     */
    Producto retirar(int cantidad) {
        if (this.cantidad < cantidad) throw new IllegalStateException("La cantidad que se desea retirar es mayor al stock actual del producto")
        this.cantidad -= cantidad
        new Producto(this.nombre, cantidad, this.precio, this.puntosDeConfianza)
    }

    /**
     * Devuelve un booleano indicando si es canjeable por PuntosDeConfianza
     */
    boolean esCanjeablePorPuntosDeConfianza() {
        this.puntosDeConfianza != new PuntosDeConfianza(0)
    }
}
