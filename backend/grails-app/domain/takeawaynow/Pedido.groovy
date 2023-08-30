package takeawaynow

/**
 * 
 * TODO
 * 
 */
class Pedido {

    static constraints = {}
    
    static belongsTo = [cliente: Cliente]

    static hasMany = [productos: Producto, productosPorPuntosDeConfianza: Producto]
    Set<Producto> productos = []
    Set<Producto> productosPorPuntosDeConfianza = []




    /**
     * Toma por parámetro un Producto y lo agrega al conjunto de productos
     **/
    void agregar(Producto producto) {
        //this.productos[producto.nombre] = producto
        this.productos.add(producto)
    }

    /**
     * Toma por parámetro un Producto y lo agrega al conjunto de productos canjeables por PuntosDeConfianza.
     */
    void agregarPorPuntosDeConfianza(Producto producto) {
        if (!producto.esCanjeablePorPuntosDeConfianza()) throw new IllegalStateException("No se puede canjear ${producto.nombre} por puntos de confianza.")
        this.productosPorPuntosDeConfianza.add(producto)
    }

    /**
     *  Quita del conjunto de productos en función de la cantidadPorQuitar
     *  en caso de que la cantidad sea mayor a la disponible en el conjunto
     *  se lanza una excepción.
     */
    //void quitar(String nombreProducto, int cantidadPorQuitar) {
    void quitar(String nombreProducto, int cantidadPorQuitar) {
        for (producto in this.productos) {
            if (producto.nombre == nombreProducto) {
                if (cantidadPorQuitar > producto.cantidad) {
                    throw new IllegalStateException("No se pueden quitar más ${producto.nombre}s de los que hay en el pedido.")
                } else if (cantidadPorQuitar == producto.cantidad) {
                    this.productos.remove(producto)
                    return
                } else {
                    producto.cantidad -= cantidadPorQuitar
                    return
                }
            }
        }
        throw new IllegalStateException("No se encuentra el producto: ${producto.nombre} en el pedido.")
    }

    /**
     * Devuelve el precio del pedido, sumando el precio de cada producto en el mismo
     */
    Dinero precio() {
        Dinero precioTotal = new Dinero(0 as BigDecimal)
        //this.productos.each{ _, producto -> precioTotal = precioTotal + producto.precioSegunCantidad() }
        this.productos.each{ producto -> precioTotal = precioTotal + producto.precioSegunCantidad() }
        precioTotal
    }

    /**
     * Devuelve el precio del producto de menor valor en el Pedido
     */
    Dinero precioDescontandoProductoDeMenorValor() {
        Dinero precio = this.precio()

        if (this.productos.isEmpty()) return precio

        //print "set ${this.productos.keySet()}\n"
        //print "set 0 ${this.productos.keySet()[0]}\n"
        //print "producto ${this.productos.get(this.productos.keySet()[0])}\n"
        Dinero valorDelProductoDeMenorValor = null
        //print "producto menos valor ${productoDeMenorValor}\n"

        for (producto in this.productos) {
            //print "producto ${producto.nombre} de precio ${producto.precio.monto}\n"
             if (valorDelProductoDeMenorValor == null || producto.precio < valorDelProductoDeMenorValor) {
                //print "entre\n"
                 valorDelProductoDeMenorValor = producto.precio
            }
        }

        //print "de menor valor ${productoDeMenorValor.nombre}\n"

        precio - valorDelProductoDeMenorValor
    }

    /**
     * Devuelve la cantidad de PuntosDeConfianza según los Productos que tenga el Pedido.
     */
    PuntosDeConfianza puntos() {
        PuntosDeConfianza puntosTotales = new PuntosDeConfianza(0)
        this.productosPorPuntosDeConfianza.each{ producto -> puntosTotales = puntosTotales + producto.puntosDeConfianzaSegunCantidad() }
        puntosTotales
    }
    
    /**
     * Devuelve la cantidad de productos en el pedido
     * (aquellos que fueron agregados por PuntosDeConfianza y aquellos que no).
     */
    int cantidadDeProductos() {
        int cantidad = 0
        this.productos.each{ producto -> cantidad = cantidad + producto.cantidad }
        this.productosPorPuntosDeConfianza.each{ producto -> cantidad = cantidad + producto.cantidad }
        cantidad
    }

    /**
     * Devuelve la cantidad de productos en el pedido
     * (aquellos que no fueron agregados por PuntosDeConfianza).
     */
    int cantidadDeProductosPorDinero() {
        int cantidad = 0
        this.productos.each{ producto -> cantidad = cantidad + producto.cantidad }
        cantidad
    }

}
