package takeawaynow

/**
 * 
 * El almacen mantiene el control del inventario con todos los productos disponibles
 * 
 */
class Almacen {

    static constraints = {
        negocio display: false, nullable: true, blank: true
    }

    static hasMany = [productos: Producto]
    static belongsTo = [negocio: Negocio]
    
    Map<String, Producto> inventario = [:]

    /**
     * 
     * Agrega el producto recibido al inventario
     * 
     */
    void agregar(Producto producto) {
        this.inventario[producto.getNombre()] = producto
    }

    /**
     * 
     * Verifica si hay stock de un producto con el mismo nombre del recibido
     * 
     */
    boolean hayStock(String nombreDelProducto) {
        if (!this.estaRegistrado(nombreDelProducto)) return false
        def producto = this.inventario[nombreDelProducto]
        this.inventario[nombreDelProducto].cantidad > 0
    }

    /**
     * 
     * Verifica si hay registrado un producto con el nombre recibido 
     * 
     */
    boolean estaRegistrado(String nombreDelProducto) {
        this.inventario[nombreDelProducto] != null
    }

    /**
     * 
     * Retira del inventario la cantidad indicada del producto con el nombre recibido y lo 
     * agrega al pedido a cambio de dinero. Si el producto que se quiere retirar no esta registrado
     * se lanza una excepcion.
     * 
     */
    void retirarProducto(String nombreDelProducto, int cantidadARetirar, Pedido pedido) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto que se busca retirar no se encuentra registrado.")
        Producto productoARetirar = this.inventario[nombreDelProducto]
        pedido.agregar(productoARetirar.retirar(cantidadARetirar))
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
        
        Producto productoARetirar = this.inventario[nombreDelProducto]
        if (productoARetirar.puntosDeConfianza == null) throw new IllegalStateException("El producto no es canjeable por puntos.")
        
        pedido.agregarPorPuntosDeConfianza(productoARetirar.retirar(cantidadARetirar))
    }

    /**
     * 
     * Reeplaza el precio del producto con el nombre recibido por el nuevo precio
     * 
     */
    void actualizarPrecio(String nombreDelProducto, Dinero nuevoPrecio) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto al cual se busca actualizar el precio no se encuentra registrado.") 
        this.inventario[nombreDelProducto].precio = nuevoPrecio
    }

    /**
     * 
     * Actualiza el stock del producto con el nombre recibido. El nuevo stock del producto sera
     * la cantidad actual aumentado el nuevo stock indicado. Si no hay un producto registrado con 
     * el nombre indicado se lanza una excepcion.
     * 
     */
    void actulizarStock(String nombreDelProducto, int nuevoStock) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto al cual se busca actualizar el stock no se encuentra registrado.") 
        def producto = this.inventario[nombreDelProducto].cantidad = this.inventario[nombreDelProducto].cantidad + nuevoStock
    }

}
