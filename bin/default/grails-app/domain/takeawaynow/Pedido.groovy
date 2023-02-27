package takeawaynow

/**
 * 
 * TODO
 * 
 */
class Pedido {

    static constraints = {
    }
    
    static belogsTo = [cliente: Cliente]

    // static embedded = ['productos']

    Map<String, Producto> productos = [:]
    Map<String, Producto> productosPorPuntosDeConfianza = [:]

    /**
     * 
     * TODO
     * 
     */
    void agregar(Producto producto) {
        this.productos[producto.nombre] = producto
    }

    /**
     * 
     * TODO
     * 
     */
    void agregarPorPuntosDeConfianza(Producto producto) {
        if (!producto.esCanjeablePorPuntosDeConfianza()) throw new IllegalStateException("No se puede canjear ${producto.nombre} por puntos de confianza.")
        this.productosPorPuntosDeConfianza[producto.nombre] = producto
    }

    /**
     * 
     * TODO
     * 
     */
    void quitar(String nombreProducto, int cantidadPorQuitar) {
        if (cantidadPorQuitar > productos[nombreProducto].cantidad) {
            throw new IllegalStateException("No se pueden quitar más ${nombreProducto}s de los que hay en el pedido.")
        } else if (cantidadPorQuitar == productos[nombreProducto].cantidad) {
            this.productos.remove(nombreProducto)
        } else {
            productos[nombreProducto].cantidad -= cantidadPorQuitar
        }
    }

    /**
     * 
     * TODO
     * 
     */
    Dinero precio() {
        Dinero precioTotal = new Dinero(0)
        this.productos.each{ _, producto -> precioTotal = precioTotal + producto.precioSegunCantidad() }
        precioTotal
    }

    /**
     * 
     * TODO
     * 
     * TODO test
     * 
     */
    Dinero precioDescontandoProductoDeMenorValor() {
        Dinero precio = this.precio()

        if (this.productos.isEmpty()) return precio

        print "set ${this.productos.keySet()}\n"
        print "set 0 ${this.productos.keySet()[0]}\n"
        print "producto ${this.productos.get(this.productos.keySet()[0])}\n"
        Producto productoDeMenorValor = this.productos.get(this.productos.keySet()[0])
        print "producto menos valor ${productoDeMenorValor}\n"

        for (producto in this.productos) {
            print "producto ${producto.value.nombre} de precio ${producto.value.precio.monto}\n"
            if (productoDeMenorValor.precio > producto.value.precio) {
                print "entre\n"
                productoDeMenorValor = producto.value
            }
        }

        print "de menor valor ${productoDeMenorValor.nombre}\n"

        precio - productoDeMenorValor.precio
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza puntos() {
        PuntosDeConfianza puntosTotales = new PuntosDeConfianza(0)
        this.productosPorPuntosDeConfianza.each{ _, producto -> puntosTotales = puntosTotales + producto.puntosDeConfianzaSegunCantidad() }
        puntosTotales
    }
    
    /**
     * 
     * TODO
     * 
     */
    int cantidadDeProductos() {
        int cantidad = 0 //TODO no hay un len o size?
        this.productos.each{ _, producto -> cantidad = cantidad + producto.cantidad }
        this.productosPorPuntosDeConfianza.each{ _, producto -> cantidad = cantidad + producto.cantidad }
        cantidad
    }

    /**
     * 
     * TODO
     * 
     */
    int cantidadDeProductosPorDinero() {
        int cantidad = 0 //TODO no hay un len o size?
        this.productos.each{ _, producto -> cantidad = cantidad + producto.cantidad }
        cantidad
    }

}
