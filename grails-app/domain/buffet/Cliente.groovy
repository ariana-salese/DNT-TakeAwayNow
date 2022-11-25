package buffet

class Cliente {

    static constraints = {
    }

    static embedded = ['saldo', 'pedido', 'buffetIngresado']

    Dinero saldo = new Dinero(0)
    Pedido pedido = new Pedido()
    Buffet buffetIngresado
    List<Compra> historialDeCompras = []

    void cargarSaldo(Dinero monto) {
        this.setSaldo(this.saldo + monto)
    }

    void ingresarBuffet(Buffet buffet) {
        this.setBuffetIngresado(buffet)
    }

    /* MÉTODOS REFERIDOS AL ARMADO Y LA COMPRA DE UN PEDIDO */

    void agregarAlPedido(String nombreProducto, int cantidad) {
        buffetIngresado.agregarAlPedido(nombreProducto, cantidad, this.pedido)
    }

    void quitarDelPedido(String nombreProducto, int cantidad) {
        buffetIngresado.quitarDelPedido(nombreProducto, cantidad, this.pedido)
    }

    int cantidadDeProductosEnElPedido() {
        pedido.cantidadDeProductos()
    }

    Dinero valorDelPedido(){
        pedido.precio()
    }

    void confirmarCompraDelPedido() {
        if (pedido.cantidadDeProductos() == 0) throw new IllegalStateException("No se puede confirmar la compra del pedido ya que el mismo no tiene productos agregados.") 
        Dinero precioPedido = pedido.precio()
        if (this.saldo < precioPedido) throw new IllegalStateException("No se puede confirmar la compra del pedido ya que el saldo es insuficiente.") 
        this.setSaldo(this.saldo - precioPedido)
        Compra compra = buffetIngresado.registrarCompra(this.pedido)
        this.historialDeCompras.add(compra)
        this.pedido = new Pedido()
    }
}
