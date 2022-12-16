package takeawaynow

class Cliente {

    static constraints = {
    }

    // static embedded = ['saldo', 'pedido', 'buffetIngresado']

    Dinero saldo = new Dinero(0)
    Pedido pedido = new Pedido()
    Buffet buffetIngresado
    Map<Integer, Compra> comprasRealizadas = [:]
    Set<Integer> comprasRetiradas = []
    int puntosDeConfianza = 0

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
        if (cantidadDeProductosEnElPedido() == 0) throw new IllegalStateException("No se pueden quitar ${cantidad} ${nombreProducto}s del pedido ya que no hay productos en el mismo.")
        pedido.quitar(nombreProducto, cantidad)
        buffetIngresado.ingresarStock(nombreProducto, cantidad)
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
        Compra compraRealizada = buffetIngresado.registrarCompra(this.pedido)
        this.setSaldo(this.saldo - precioPedido)
        this.comprasRealizadas[compraRealizada.getId()] = compraRealizada
        this.pedido = new Pedido()
    }

    void retirarCompra(int id) {
        if (this.comprasRetiradas.contains(id)) throw new Exception("Dicha compra ya fue retirada previamente.")
        if (!this.comprasRealizadas[id]) throw new Exception("No se encuentra una compra realizada con el ID indicado.")
        if (this.buffetIngresado.estadoDeCompra(id) != Compra.EstadoDeCompra.LISTA_PARA_RETIRAR) throw new Exception("La compra aún no está lista para retirar, su estado actual es ${this.buffetIngresado.estadoDeCompra(id)}.")

        this.buffetIngresado.marcarCompraRetirada(id)
        this.comprasRetiradas.add(id)
        this.setPuntosDeConfianza(this.puntosDeConfianza + this.comprasRealizadas[id].getPedido().cantidadDeProductos())
    }
}
