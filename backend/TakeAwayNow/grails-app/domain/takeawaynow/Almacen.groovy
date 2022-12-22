package takeawaynow

/**
 * 
 * TODO
 * 
 */
class Almacen {

    static constraints = {
    }
    
    Map<String, Producto> inventario = [:]

    /**
     * 
     * TODO
     * 
     */
    void agregar(Producto producto) {
        this.inventario[producto.getNombre()] = producto
    }

    /**
     * 
     * TODO
     * 
     */
    boolean hayStock(String nombreDelProducto) {
        if (!this.estaRegistrado(nombreDelProducto)) return false
        def producto = this.inventario[nombreDelProducto]
        this.inventario[nombreDelProducto].cantidad > 0
    }

    /**
     * 
     * TODO
     * 
     */
    boolean estaRegistrado(String nombreDelProducto) {
        this.inventario[nombreDelProducto] != null
    }

    /**
     * 
     * TODO
     * 
     */
    void retirarProducto(String nombreDelProducto, int cantidadARetirar, Pedido pedido) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto que se busca retirar no se encuentra registrado.")
        Producto productoARetirar = this.inventario[nombreDelProducto]
        pedido.agregar(productoARetirar.retirar(cantidadARetirar))
    }

    /**
     * 
     * TODO
     * 
     */
    void actualizarPrecio(String nombreDelProducto, Dinero nuevoPrecio) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto al cual se busca actualizar el precio no se encuentra registrado.") 
        this.inventario[nombreDelProducto].precio = nuevoPrecio
    }

    /**
     * 
     * TODO
     * 
     */
    void actulizarStock(String nombreDelProducto, int nuevoStock) {
        if (!this.estaRegistrado(nombreDelProducto)) throw new IllegalStateException("El producto al cual se busca actualizar el stock no se encuentra registrado.") 
        def producto = this.inventario[nombreDelProducto].cantidad = this.inventario[nombreDelProducto].cantidad + nuevoStock
    }

}
