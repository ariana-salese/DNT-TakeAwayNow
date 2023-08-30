package takeawaynow

/**
 * 
 * El almacen mantiene el control del inventario con todos los productos disponibles
 * 
 */
class Almacen {

    static constraints = {}

    static hasMany = [productos: Producto]
    static belongsTo = [negocio: Negocio]

    Set<Producto> inventario = []

    /**
     * Agrega el Producto pasado por parámetro al inventario
     */
    void agregar(Producto producto) {
        this.inventario.add(producto)
    }

    /**
     * Verifica si hay registrado un producto con el nombre recibido
     */
    boolean estaRegistrado(String nombreDelProducto) {
        for (producto in inventario) {
            if (producto.nombre == nombreDelProducto) {
                return true
            }
        }
        return false
    }

    /**
     * Verifica si hay stock de un producto con el mismo nombre del recibido
     */
    boolean hayStock(String nombreDelProducto) {
        if (!this.estaRegistrado(nombreDelProducto)) {
            return false
        }
        for (producto in inventario) {
            if (producto.nombre == nombreDelProducto) {
                return producto.cantidad > 0
            }
        }
    }

    /**
     * Retira del inventario la cantidad indicada del producto con el nombre recibido y lo 
     * agrega al pedido a cambio de dinero. Si el producto que se quiere retirar no esta registrado
     * se lanza una excepcion.
     */
    void retirarProducto(String nombreDelProducto, int cantidadARetirar, Pedido pedido) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto que se busca retirar no se encuentra registrado.")
        Producto productoARetirar
        for (producto in inventario) {
            if (producto.nombre == nombreDelProducto) {
                productoARetirar = producto
                pedido.agregar(productoARetirar.retirar(cantidadARetirar))
            }
        }
    }
    
    /**
     * 
     * Retira del inventario la cantidad indicada del producto con el nombre recibido y lo 
     * agrega al pedido a cambio de puntos de confianza. Se lanza error si:
     * - El producto que se quiere retirar no esta registrado se lanza una excepcion.
     * - El producto a retirar a cambio de puntos no es canjeable por puntos. 
     * 
     */
    void retirarProductoPorPuntosDeConfianza(String nombreDelProducto, int cantidadARetirar, Pedido pedido) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto que se busca retirar no se encuentra registrado.")

        Producto productoARetirar
        for (producto in inventario) {
            if (producto.nombre == nombreDelProducto) {
                productoARetirar = producto
                if (productoARetirar.puntosDeConfianza == null) throw new IllegalStateException("El producto no es canjeable por puntos.")
                pedido.agregarPorPuntosDeConfianza(productoARetirar.retirar(cantidadARetirar))
            }
        }
    }

    /**
     * 
     * Reemplaza el precio del producto con el nombre recibido por el nuevo precio
     * 
     */
    void actualizarPrecio(String nombreDelProducto, Dinero nuevoPrecio) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto al cual se busca actualizar el precio no se encuentra registrado.")
        for (producto in inventario) {
            if (producto.nombre == nombreDelProducto) {
                producto.precio = nuevoPrecio
            }
        }
    }

    /**
     * 
     * Actualiza el stock del producto con el nombre recibido. El nuevo stock del producto sera
     * la cantidad actual aumentado el nuevo stock indicado. Si no hay un producto registrado con 
     * el nombre indicado se lanza una excepcion.
     * 
     */
    void actualizarStock(String nombreDelProducto, int nuevoStock) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto al cual se busca actualizar el stock no se encuentra registrado.")
        for (producto in inventario) {
            if (producto.nombre == nombreDelProducto) {
                producto.cantidad = producto.cantidad + nuevoStock
            }
        }
    }

    /**
    * Obtiene el precio del Producto, lanza una excepción en
    * caso de que el mismo no se encuentre en el inventario
    */
    Dinero obtenerPrecioDe(String nombreDelProducto) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto del cual se busca conocer su precio no se encuentra registrado.")
        for (producto in inventario) {
            if (producto.nombre == nombreDelProducto) {
                return producto.precio
            }
        }
    }

    /**
     * Obtiene la cantidad del Producto, lanza una excepción en
     * caso de que el mismo no se encuentre en el inventario
     */
    int obtenerCantidadDe(String nombreDelProducto) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto del cual se busca conocer su cantidad no se encuentra registrado.")
        for (producto in inventario) {
            if (producto.nombre == nombreDelProducto) {
                return producto.cantidad
            }
        }
    }
}
