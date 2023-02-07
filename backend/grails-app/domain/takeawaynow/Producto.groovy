package takeawaynow

/**
 * 
 * TODO
 * 
 */
class Producto {

    static constraints = {
        nombre nullable: false, blank: false
        precio nullable: false, blank: false
        cantidad nullable: false, min: 1, max: 1000
        puntosDeConfianza display: false, nullable: true
        almacen display: false, nullable: true
    }

    static belongsTo = [almacen: Almacen]

    static embedded = ['precio']

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
     * 
     * TODO
     * 
     */
    Dinero precioSegunCantidad() {
        this.precio * this.cantidad
    }

    /**
     * 
     * TODO
     * 
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
     * 
     * TODO
     * 
     */
    boolean esCanjeablePorPuntosDeConfianza() {
        this.puntosDeConfianza != new PuntosDeConfianza(0) //TODO usar null?
    }
}
