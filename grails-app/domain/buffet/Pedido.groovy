package buffet

class Pedido {

    static constraints = {
    }
    
    static embedded = ['productos']

    Map<String, Producto> productos = [:]

    void agregar(Producto producto) {
        this.productos[producto.nombre] = producto
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
