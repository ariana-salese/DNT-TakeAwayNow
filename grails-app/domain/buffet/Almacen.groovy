package buffet

class Almacen {

    static constraints = {
    }
    
    Map<String, Producto> productos = []

    boolean retirarProducto(String nombreProducto, int cantidadARetirar, Pedido pedido) {
        Producto producto = productos[nombreProducto]

        if (producto.cantidad >= cantidadARetirar) {
            pedido.agregar(new Producto(producto, cantidadARetirar, producto.nombreDelProducto))
            producto.setCantidad(producto.cantidad - cantidadARetirar)
            return true
        }
        false
    }

    void agregar(Producto producto) {
        this.productos.add(producto.nombre, producto)
    }
}
