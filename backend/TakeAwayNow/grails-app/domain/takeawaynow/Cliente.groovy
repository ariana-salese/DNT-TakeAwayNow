package takeawaynow

/**
 * 
 * El cliente es el cual ingresa a un negocio para formar un pedido y formalizar
 * una compra
 * 
 */
class Cliente {

    static constraints = {
        nombre size: 5..15, blank: false, unique: true
        password password: true, size: 5..15, blank: false
        saldo display: false
        pedido display: false
        negocioIngresado display: false, nullable: true
        comprasRealizadas display: false
        comprasRetiradas display: false
        puntosDeConfianza display: false
    }

    // static embedded = ['saldo', 'pedido', 'negocioIngresado']

    String nombre
    String password
    Dinero saldo = new Dinero(0)
    Pedido pedido = new Pedido()
    Negocio negocioIngresado
    Map<Integer, Compra> comprasRealizadas = [:]
    Set<Integer> comprasRetiradas = []
    PuntosDeConfianza puntosDeConfianza = new PuntosDeConfianza(0)

    Cliente(String nombreCliente, String pass){
        this.nombre = nombreCliente
        this.password = password
    }

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
     * TODO mockear diaen tests para no recibirlo por parametro?
     */
    void ingresarNegocio(Negocio negocio, Date dia) {
        if (!negocio.estaAbierto(dia)) throw new IllegalStateException("El negocio al que se quiere ingresar no esta abierto.")
        this.setNegocioIngresado(negocio)
    }

    /* MÉTODOS REFERIDOS AL ARMADO Y LA COMPRA DE UN PEDIDO */

    /**
     * 
     * Notifica al negocio que el cliente esta agregando un producto a su pedido.
     * Lanza error en los siguiente casos: 
     *  - No se ingreso a ningun negocio  
     *  - No se puede agregar al pedido (ej. no hay stock).
     * 
     * TODO creo que deberiamos chequear si esta abierto o no al agregar al pedido. 
     * El tema es que tendriamos que recibir el dia para poder testearlo, sino segun la
     * hora a la que se ejecuten los tests fallan. Lo mismo que pasa con el metodo ingresar
     * negocio. TODO : pasar dia y fue o encontrar una forma de mockear dia
     * 
     */
    void agregarAlPedido(String nombreProducto, int cantidad) {
        //if (!negocio.estaAbierto(dia)) throw new IllegalStateException("El negocio ya no esta abiero")
        if (!negocioIngresado) throw new IllegalStateException("No se ingreso a un negocio.")
        negocioIngresado.agregarAlPedido(nombreProducto, cantidad, this.pedido)
    }

    /**
     * 
     * Notifica al negocio que el cliente esta agregando un producto a su pedido
     * a cambio de puntos de confianza. Lanza error en los siguiente casos: 
     *  - No se ingreso a ningun negocio  
     *  - No se puede agregar al pedido (ej. no hay stock).
     * 
     * TODO lo mismo que arriba
     * 
     */
    void agregarAlPedidoPorPuntoDeConfianza(String nombreProducto, int cantidad) {
        //if (!negocio.estaAbierto(dia)) throw new IllegalStateException("El negocio ya no esta abiero")
        if (!negocioIngresado) throw new IllegalStateException("No se ingreso a un negocio.")
        negocioIngresado.agregarAlPedidoPorPuntoDeConfianza(nombreProducto, cantidad, this.pedido)
    }

    /**
     * 
     * Quita del pedido la cantidad indicada del producto con el nombre dado. Si no se puede
     * quitar del pedido se lanza un error. 
     * 
     */
    void quitarDelPedido(String nombreProducto, int cantidad) {
        if (cantidadDeProductosEnElPedido() == 0) throw new IllegalStateException("No se pueden quitar ${cantidad} ${nombreProducto}s del pedido ya que no hay productos en el mismo.")
        pedido.quitar(nombreProducto, cantidad)
        negocioIngresado.ingresarStock(nombreProducto, cantidad)
    }

    /**
     * 
     * Indica la cantidad de productos en el pedido.
     * 
     */
    int cantidadDeProductosEnElPedido() {
        pedido.cantidadDeProductos()
    }

    /**
     * 
     * Otorga los puntos de confiaza indicados.
     * 
     * TODO borrar? Lo use en los tests, quizas no hace falta el metodo
     * 
     */
    void darPuntosDeConfianza(PuntosDeConfianza puntosDeConfianza) {
        this.setPuntosDeConfianza(this.puntosDeConfianza + puntosDeConfianza)
    }

    /**
     * 
     * Indica el valor del pedido en dinero.
     * 
     */
    Dinero valorDelPedidoEnDinero() {
        pedido.precio()
    }

    /**
     * 
     * Indica el valor del pedido en puntos.
     * 
     */
    PuntosDeConfianza valorDelPedidoEnPuntos() {
        pedido.puntos()
    }

    /**
     * 
     * Le indica al negocio que el pedido realizado fue comprado. Se actualizara el saldo y los
     * puntos de confianza y se guardara la compra realizada. Lanza error en los siguientes casos:
     *  - El pedido no tiene productos.
     *  - El saldo o puntos de confianza actuales es insuficiente para pagar el pedido.
     * 
     */
    void confirmarCompraDelPedido() {
        if (pedido.cantidadDeProductos() == 0) throw new IllegalStateException("No se puede confirmar la compra del pedido ya que el mismo no tiene productos agregados.") 
        
        Dinero precioPedido = pedido.precio()
        PuntosDeConfianza puntosPedido = pedido.puntos()
        
        if (this.saldo < precioPedido) throw new IllegalStateException("No se puede confirmar la compra del pedido ya que el saldo es insuficiente.")
        if (this.puntosDeConfianza < puntosPedido) throw new IllegalStateException("No se puede confirmar la compra del pedido ya que los puntos de confianza son insuficientes.")  
        
        Compra compraRealizada = negocioIngresado.registrarCompra(this.pedido)
        this.setSaldo(this.saldo - precioPedido)
        this.setPuntosDeConfianza(this.puntosDeConfianza - puntosPedido)
        this.comprasRealizadas[compraRealizada.getId()] = compraRealizada
        this.pedido = new Pedido()
    }

    /**
     * 
     * Retira la compra con el id indicado. Lanza error en los siguientes casos:
     *  - No existe un compra con el ID indicado.
     *  - La compra ya fue retirada.
     *  - La compra no esta lista para retirar.
     * 
     */
    void retirarCompra(int id) {
        if (!this.comprasRealizadas[id]) throw new Exception("No se encuentra una compra realizada con el ID indicado.")
        if (this.comprasRetiradas.contains(id)) throw new Exception("Dicha compra ya fue retirada previamente.")
        if (this.negocioIngresado.estadoDeCompra(id) != Compra.EstadoDeCompra.LISTA_PARA_RETIRAR) throw new Exception("La compra aún no está lista para retirar, su estado actual es ${this.negocioIngresado.estadoDeCompra(id)}.")

        this.negocioIngresado.marcarCompraRetirada(id)
        this.comprasRetiradas.add(id)
        this.setPuntosDeConfianza(this.puntosDeConfianza.agregarPuntosPorCompra(this.comprasRealizadas[id]))
    }

    /**
     * 
     * Cancela la compra con el ID indicado. Dependiendo el estado de la compra se actualizaran
     * los puntos de confianza y el saldo:
     *  - Aguardando preparacion: se restara un punto de confianza y se devolvera el dinero gastado.
     *  - En preparacion: se retaran los puntos de confianza que la compra hubiese otorgado y no se 
     *    devolvera el dinero gastado.
     *  - Lista para retirar: los puntos de confiaza se reestablecen como cero y no se devolvera el 
     *    dinero gastado.
     *  Si no existe una compra con el ID indicado de lanzara un error.
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
                this.setPuntosDeConfianza(this.puntosDeConfianza.eliminarPuntosPorCompra(this.comprasRealizadas[id]))
                this.negocioIngresado.reingresarStockDelPedido(id)
                break

            case Compra.EstadoDeCompra.LISTA_PARA_RETIRAR:
                this.setPuntosDeConfianza(new PuntosDeConfianza(0))
                this.negocioIngresado.reingresarStockDelPedido(id)
                break
        }
    }

    /**
     * 
     * Realiza la devolucion de la compra con el ID indicado. Se notifica al negocio, se devuelve el
     * dinero gastado al saldo y se restan los puntos otorgados por la compra.
     * 
     * TODO cambiar nombre a devolverCompra? 
     * 
     */
    void solicitarDevoluciónDelPedido(int id) {
        if (!this.comprasRealizadas[id]) throw new Exception("No se encuentra una compra realizada con el ID indicado.")
        
        this.negocioIngresado.devolucionDelPedido(id)
        this.setPuntosDeConfianza(this.puntosDeConfianza.eliminarPuntosPorCompra(this.comprasRealizadas[id]))
        this.setSaldo(this.saldo + this.comprasRealizadas[id].getPedido().precio())
    }
}
