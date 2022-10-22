package buffet

class Buffet {

    static constraints = {
    }

    static embedded = ['almacen']

    Almacen almacen = new Almacen()

    void registrarProducto(Producto producto) {
        if (producto.cantidad == 0) throw new IllegalStateException()
        this.almacen.agregar(producto)
    }

    void actualizarPrecio(String nombreDelProducto, Dinero nuevoPrecio) {
        if (nuevoPrecio <= new Dinero(0)) throw new IllegalStateException() 
        this.almacen.actualizarPrecio(nombreDelProducto, nuevoPrecio)
    }

    void ingresarStock(String nombreDelProducto, int nuevoStock) {
        if (nuevoStock <= 0) throw new IllegalStateException()
        this.almacen.actulizarStock(nombreDelProducto, nuevoStock)
    }

    boolean hayStock(String nombreDelProducto) {
        this.almacen.hayStock(nombreDelProducto)
    }

    boolean agregarAlPedido(String nombreProducto, int cantidad, Pedido pedido) {
        this.almacen.retirarProducto(nombreProducto, cantidad, pedido) 
    }

    Compra comprar(Pedido pedido) {
        new Compra(pedido)
    }
    
}
