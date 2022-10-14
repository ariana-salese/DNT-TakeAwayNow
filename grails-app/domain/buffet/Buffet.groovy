package buffet

class Buffet {

    static constraints = {
    }

    static embedded = ['almacen']

    Almacen almacen = new Almacen()
    Map<String, BigDecimal> listadoDePrecios = [:]

    void registrarProducto(Producto producto, BigDecimal precio, int stock) {
        actualizarStock(producto.getNombreDelProducto(), stock)
        actualizarPrecio(producto.getNombreDelProducto(), precio)
    }

    void actualizarPrecio(Producto producto, BigDecimal nuevoPrecio) {
        listadoDePrecios[producto.getNombreDelProducto()] = nuevoPrecio
    }

    void actualizarStock(Producto producto, int nuevoStock) {
        almacen.agregar(producto, nuevoStock)
    }

    boolean agregarAlPedido(String nombreProducto, int cantidad, Pedido pedido) {
        almacen.retirarProducto(nombreProducto, cantidad, pedido)
    }
    
}
