package takeawaynow

class Buffet {

    static constraints = {
    }

    // static embedded = ['almacen']

    String nombre
    Almacen almacen = new Almacen()
    Map<Integer, Compra> comprasRegistradas = [:]
    Set comprasRetiradas = []
    int ids_compras = 0

    Buffet(String nombreDelBuffet) {
        this.nombre = nombreDelBuffet
    }

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

    void reingresarStockDelPedido(int id) {
        Map<String, Producto> productos = this.comprasRegistradas[id].getPedido().getProductos()
        productos.each{ _, producto -> this.ingresarStock(producto.getNombre(), producto.getCantidad()) }
    }


    /* MÉTODOS REFERIDOS A LA ETAPA DE REGISTRO Y RETIRO DE COMPRAS */
    
    Compra registrarCompra(Pedido pedidoConfirmado) {
        int idCompraRegistrada = ids_compras++
        Compra compraRegistrada = new Compra(pedidoConfirmado, idCompraRegistrada)
        this.comprasRegistradas[idCompraRegistrada] = compraRegistrada
        compraRegistrada
    }

    void prepararCompra(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.AGUARDANDO_PREPARACION) throw new Exception("No se puede preparar dicha compra ya que la misma no se encontraba AGUARDANDO_PREPARACION.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.EN_PREPARACION
    }

    void marcarCompraListaParaRetirar(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.EN_PREPARACION) throw new Exception("No se puede marcar dicha compra como LISTA_PARA_RETIRAR ya que la misma no se encontraba EN_PREPARACION.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.LISTA_PARA_RETIRAR
    }

    void marcarCompraRetirada(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.LISTA_PARA_RETIRAR) throw new Exception("No se puede marcar dicha compra como RETIRADA ya que la misma no se encontraba LISTA_PARA_RETIRAR.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.RETIRADA
    }

    void marcarCompraCancelada(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.RETIRADA) throw new Exception("No se puede marcar dicha compra como CANCELADA ya que la misma no se encontraba RETIRADA.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.CANCELADA
    }

    void marcarCompraDevuelta(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.RETIRADA) throw new Exception("No se puede marcar dicha compra como DEVUELTA ya que la misma no se encontraba RETIRADA.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.DEVUELTA
    }

    Compra.EstadoDeCompra estadoDeCompra(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        comprasRegistradas[id].getEstado()
    }
}
