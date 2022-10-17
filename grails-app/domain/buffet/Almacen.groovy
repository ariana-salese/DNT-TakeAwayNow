package buffet

class Almacen {

    static constraints = {
    }
    
    def inventario = [:]

    void agregar(Producto producto) {
        this.inventario[producto.getNombre()] = producto
    }

    boolean hayStock(String nombreDelProducto) {
        def producto = this.inventario[nombreDelProducto]

        if (producto == null) return false

        this.inventario[nombreDelProducto].cantidad > 0
    }

    void retirarProducto(String nombreProducto, int cantidadARetirar, Pedido pedido) {
        Producto productoARetirar = this.inventario[nombreProducto]
        pedido.agregar(productoARetirar.retirar(cantidadARetirar))
    }

    void actualizarPrecio(String nombreDelProducto, Dinero nuevoPrecio) {
        this.inventario[nombreDelProducto].precio = nuevoPrecio
    }

    void actulizarStock(String nombreDelProducto, int nuevoStock) {
        this.inventario[nombreDelProducto].cantidad = this.inventario[nombreDelProducto].cantidad + nuevoStock
    }

}
