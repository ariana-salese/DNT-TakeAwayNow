package buffet

class Buffet {

    static constraints = {
    }

    static embedded = ['almacen']

    Almacen almacen = new Almacen()
    List<Compra> comprasRegistradas = []
    int ids_compras = 0

    void registrarProducto(Producto producto) {
        if (producto.cantidad == 0) throw new IllegalStateException("No se puede registrar un producto sin stock.")
        this.almacen.agregar(producto)
    }

    void actualizarPrecio(String nombreDelProducto, Dinero nuevoPrecio) {
        if (nuevoPrecio <= new Dinero(0)) throw new IllegalStateException("No se puede actualizar el precio a un precio menor o igual a cero.") 
        this.almacen.actualizarPrecio(nombreDelProducto, nuevoPrecio)
    }

    void ingresarStock(String nombreDelProducto, int nuevoStock) {
        if (nuevoStock <= 0) throw new IllegalStateException("No se puede ingresar un stock menor o igual a cero.")
        this.almacen.actulizarStock(nombreDelProducto, nuevoStock)
    }

    boolean hayStock(String nombreDelProducto) {
        this.almacen.hayStock(nombreDelProducto)
    }

    boolean agregarAlPedido(String nombreProducto, int cantidad, Pedido pedido) {
        this.almacen.retirarProducto(nombreProducto, cantidad, pedido) 
    }

    void quitarDelPedido(String nombreProducto, int cantidad, Pedido pedido) {
        this.almacen.actualizarStock(nombreProducto, cantidad) 
    }

    Compra registrarCompra(Pedido pedido) {
        Compra compra = new Compra(pedido, ids_compras++)
        this.comprasRegistradas.add(compra)
        compra
    }
}
