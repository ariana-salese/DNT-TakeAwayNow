package buffet

class Pedido {

    static constraints = {
    }

    def productos = [:]

    void agregar(Producto producto) {
        this.productos[producto.nombre] = producto
    }

    int cantidadDeProductos() {
        this.productos.size()
    }

    Dinero precio() {
        Dinero precioTotal = new Dinero(0)
    
        this.productos.each{ _, producto -> precioTotal = precioTotal + producto.precioSegunCantidad() }

        precioTotal
    }
}
