package buffet

class Buffet {

    static constraints = {
    }

    static embedded = ['almacen']

    Almacen almacen = new Almacen()
    Map<String, BigDecimal> listadoDePrecios = [:]

    void registrarProducto(Producto producto, BigDecimal precio, int stock) {
        actualizarStock(producto, stock)
        actualizarPrecio(producto, precio)
    }

    void actualizarPrecio(Producto producto, BigDecimal nuevoPrecio) {
        if (nuevoPrecio <= 0) {
            throw new IllegalStateException()
        }
        listadoDePrecios[producto.getNombreDelProducto()] = nuevoPrecio
    }

    void actualizarStock(Producto producto, int nuevoStock) {
        if (nuevoStock <= 0) {
            throw new IllegalStateException()
        }
        this.almacen.agregar(producto, nuevoStock)
    }

    boolean hayStock(String nombreDelProducto) {
        this.almacen.hayStock(nombreDelProducto)
    }

    boolean agregarAlPedido(String nombreProducto, int cantidad, Pedido pedido) {
        this.almacen.retirarProducto(nombreProducto, cantidad, pedido)
    }
    
}
