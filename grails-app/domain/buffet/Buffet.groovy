package buffet

class Buffet {

    static constraints = {
    }

    Almacen almacen = new Almacen()
    Map<String, Dinero> listadoDePrecios

    void registrarProducto(Producto producto, Dinero precio) {
        listadoDePrecios[producto.nombre] = precio
    }

    void actualizarStock(Producto producto) {
        almacen.agregar(producto)
    }

    boolean agregarAlPedido(String nombreProducto, int cantidad, Pedido pedido) {
        almacen.retirarProducto(producto, cantidad, pedido)
    }
    
}
