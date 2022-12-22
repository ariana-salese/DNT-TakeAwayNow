package takeawaynow

/**
 * 
 * El cliente es el cual ingresa a un negocio para formar un pedido y formalizar
 * una compra
 * 
 */
class Cliente {

    static constraints = {
    }

    // static embedded = ['saldo', 'pedido', 'negocioIngresado']

    Dinero saldo = new Dinero(0)
    Pedido pedido = new Pedido()
    Negocio negocioIngresado
    Map<Integer, Compra> comprasRealizadas = [:]
    Set<Integer> comprasRetiradas = []
    int puntosDeConfianza = 0

    /**
     * 
     * Carga saldo al cliente. El nuevo saldo sera el actual aumentado el monto 
     * recibido
     * 
     */
    void cargarSaldo(Dinero monto) {
        this.setSaldo(this.saldo + monto)
    }

    /**
     * 
     * Guarda el negocio al que el cliente ingreso
     * 
     * TODO mockear dia para no recibirlo por parametro?
     */
    void ingresarNegocio(Negocio negocio, Date dia) {
        if (!negocio.estaAbierto(dia)) throw new IllegalStateException("El negocio al que se quiere ingresar no esta abierto")
        this.setNegocioIngresado(negocio)
    }

    /* MÉTODOS REFERIDOS AL ARMADO Y LA COMPRA DE UN PEDIDO */

    /**
     * 
     * TODO
     * 
     * TODO no se puede agregar nada si no ingreso a un negocio
     * 
     */
    void agregarAlPedido(String nombreProducto, int cantidad) {
        negocioIngresado.agregarAlPedido(nombreProducto, cantidad, this.pedido)
    }

    /**
     * 
     * TODO
     * 
     */
    void quitarDelPedido(String nombreProducto, int cantidad) {
        if (cantidadDeProductosEnElPedido() == 0) throw new IllegalStateException("No se pueden quitar ${cantidad} ${nombreProducto}s del pedido ya que no hay productos en el mismo.")
        pedido.quitar(nombreProducto, cantidad)
        negocioIngresado.ingresarStock(nombreProducto, cantidad)
    }

    /**
     * 
     * TODO
     * 
     */
    int cantidadDeProductosEnElPedido() {
        pedido.cantidadDeProductos()
    }

    /**
     * 
     * TODO
     * 
     */
    Dinero valorDelPedido(){
        pedido.precio()
    }

    /**
     * 
     * TODO
     * 
     */
    void confirmarCompraDelPedido() {
        if (pedido.cantidadDeProductos() == 0) throw new IllegalStateException("No se puede confirmar la compra del pedido ya que el mismo no tiene productos agregados.") 
        Dinero precioPedido = pedido.precio()
        if (this.saldo < precioPedido) throw new IllegalStateException("No se puede confirmar la compra del pedido ya que el saldo es insuficiente.") 
        Compra compraRealizada = negocioIngresado.registrarCompra(this.pedido)
        this.setSaldo(this.saldo - precioPedido)
        this.comprasRealizadas[compraRealizada.getId()] = compraRealizada
        this.pedido = new Pedido()
    }

    /**
     * 
     * TODO
     * 
     */
    void retirarCompra(int id) {
        if (!this.comprasRealizadas[id]) throw new Exception("No se encuentra una compra realizada con el ID indicado.")
        if (this.comprasRetiradas.contains(id)) throw new Exception("Dicha compra ya fue retirada previamente.")
        if (this.negocioIngresado.estadoDeCompra(id) != Compra.EstadoDeCompra.LISTA_PARA_RETIRAR) throw new Exception("La compra aún no está lista para retirar, su estado actual es ${this.negocioIngresado.estadoDeCompra(id)}.")

        this.negocioIngresado.marcarCompraRetirada(id)
        this.comprasRetiradas.add(id)
        this.setPuntosDeConfianza(this.puntosDeConfianza + this.comprasRealizadas[id].getPedido().cantidadDeProductos())
    }

    /**
     * 
     * TODO
     * 
     */
    void cancelarCompra(int id) {
        if (!this.comprasRealizadas[id]) throw new Exception("No se encuentra una compra realizada con el ID indicado.")

        Compra.EstadoDeCompra estadoDeCompra = this.negocioIngresado.estadoDeCompra(id)
        switch(estadoDeCompra) {
            case Compra.EstadoDeCompra.AGUARDANDO_PREPARACION:
                this.setPuntosDeConfianza(this.puntosDeConfianza - 1)
                this.setSaldo(this.saldo + this.comprasRealizadas[id].getPedido().precio())
                this.negocioIngresado.reingresarStockDelPedido(id)
                break

            case Compra.EstadoDeCompra.EN_PREPARACION:
                this.setPuntosDeConfianza(this.puntosDeConfianza - this.comprasRealizadas[id].getPedido().cantidadDeProductos())
                this.negocioIngresado.reingresarStockDelPedido(id)
                break

            case Compra.EstadoDeCompra.LISTA_PARA_RETIRAR:
                this.setPuntosDeConfianza(0)
                this.negocioIngresado.reingresarStockDelPedido(id)
                break
        }
        
        // if (estadoDeCompra ==) {
        //     throw new Exception("La compra aún no fue retirada, ya fue cancelada o devuelta, su estado actual es ${this.negocioIngresado.estadoDeCompra(id)}.")            
        // } 
        // if (estadoDeCompra) {
        //     throw new Exception("La compra aún no fue retirada, ya fue cancelada o devuelta, su estado actual es ${this.negocioIngresado.estadoDeCompra(id)}.")
        // } 
        // if (estadoDeCompra) {
        //     throw new Exception("La compra aún no fue retirada, ya fue cancelada o devuelta, su estado actual es ${this.negocioIngresado.estadoDeCompra(id)}.")
        // } 
    }

    void solicitarDevoluciónDelPedido(int id) {
        if (!this.comprasRealizadas[id]) throw new Exception("No se encuentra una compra realizada con el ID indicado.")

        this.negocioIngresado.devolucionDelPedido(id)
        this.setPuntosDeConfianza(this.puntosDeConfianza - this.comprasRealizadas[id].getPedido().cantidadDeProductos())
        this.setSaldo(this.saldo + this.comprasRealizadas[id].getPedido().precio())
    }
}
