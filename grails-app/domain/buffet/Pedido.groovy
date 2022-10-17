package buffet

class Pedido {

    static constraints = {
    }

    def productos = [:]

    void agregar(Producto producto) {
        this.productos[producto.nombre] = producto
    }

    int cantidadDeProductos() {
        int cantidad = 0

        this.productos.each{ _, producto -> cantidad = cantidad + producto.cantidad }

        cantidad
    }

    Dinero precio() {
        Dinero precioTotal = new Dinero(0)
    
        this.productos.each{ _, producto -> precioTotal = precioTotal + producto.precioSegunCantidad() }

        precioTotal
    }
}
