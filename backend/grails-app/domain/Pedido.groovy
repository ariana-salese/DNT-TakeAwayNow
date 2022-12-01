package buffet

class Pedido {

    static constraints = {
    }
    
    static embedded = ['productos']

    Map<String, Producto> productos = [:]

    void agregar(Producto producto) {
        this.productos[producto.nombre] = producto
    }

    void quitar(String nombreProducto, int cantidadPorQuitar) {
        if (cantidadPorQuitar > productos[nombreProducto].cantidad) {
            throw new IllegalStateException("No se pueden quitar más ${nombreProducto}s de los que hay en el pedido.")
        } else if (cantidadPorQuitar == productos[nombreProducto].cantidad) {
            this.productos.remove(nombreProducto)
        } else {
            productos[nombreProducto].cantidad -= cantidadPorQuitar
        }
    }

    Dinero precio() {
        Dinero precioTotal = new Dinero(0)
        this.productos.each{ _, producto -> precioTotal = precioTotal + producto.precioSegunCantidad() }
        precioTotal
    }
    
    int cantidadDeProductos() {
        int cantidad = 0
        this.productos.each{ _, producto -> cantidad = cantidad + producto.cantidad }
        cantidad
    }

}
