package takeawaynow

/**
* 
* El negocio provee productos para ser comprados por clientes 
* 
*/
class Negocio {

    static constraints = {
        nombre blank: false, nullable: false, unique: true
        password blank: false, nullable: false, password: true
        almacen display: false, nullable: true
        comprasRegistradas display: false, nullable: true
        ids_compras display: false, nullable: true
        horario_apertura blank: false, display: false, nullable: true
        horario_cierre blank: false, display: false, nullable: true
        comprasRegistradas display: false, nullable: true
    }

    static hasOne = [almacen: Almacen, horario_apertura: Horario, horario_cierre: Horario]
    static hasMany = [comprasRegistradas: Compra] // Agregar clientes quizás ?

    String nombre
    String password
    Almacen almacen
    Map<Integer, Compra> comprasRegistradas = [:]
    int ids_compras = 0
    Horario horario_apertura
    Horario horario_cierre

    /**
    * 
    * Crea un negocio. Si el horario de apertura es mayor al de cierre se lanza un error.
    * 
    */
    Negocio(String nombreDelNegocio, Horario horario_apertura, Horario horario_cierre) {
        if (horario_apertura > horario_cierre) throw new IllegalStateException("El horario de apertura debe ser menor al de cierre.")

        this.nombre = nombreDelNegocio
        this.horario_apertura = new Horario(9,0)
        this.horario_cierre = new Horario(18,0)

        this.almacen = new Almacen() 
    }

    /**
     * 
     * Registra el producto recibido en el almancen. Si la cantidad del producto recibido es 0 entonces
     * se lanza un error.
     * 
     */
    void registrarProducto(Producto producto) {
        if (producto.cantidad == 0) throw new IllegalStateException("No se puede registrar un producto sin stock.")
        this.almacen.agregar(producto)
    }

    /**
     * 
     * Indica si el negocio esta abierto segun el horario.
     * 
     */
    boolean estaAbierto(Date dia) {
        int hora = dia.hours
        int minutos = dia.minutes

        Horario hora_actual = new Horario(hora, minutos)

        !(hora_actual > horario_cierre || hora_actual < horario_apertura)
    }

    /**
     * 
     * Se reemplaza el precio del producto con el nombre recibido por el nuevo precio. Si el nuevo precio
     * es 0 entonces se lanza un error.
     * 
     */
    void actualizarPrecio(String nombreDelProducto, Dinero nuevoPrecio) {
        if (nuevoPrecio <= new Dinero(0)) throw new IllegalStateException("No se puede actualizar el precio a un precio menor o igual a cero.") 
        this.almacen.actualizarPrecio(nombreDelProducto, nuevoPrecio)
    }

    /**
     * 
     * Aumenta el stock del producto con el nombre recibido. El nuevo stock sera al actual aumentado el recibido.
     * Si el stock es menor o igual a cer entonces se lanza un error. 
     * 
     */
    void ingresarStock(String nombreDelProducto, int nuevoStock) {
        if (nuevoStock <= 0) throw new IllegalStateException("No se puede ingresar un stock menor o igual a cero.")
        this.almacen.actulizarStock(nombreDelProducto, nuevoStock)
    }

    /**
     * 
     * Verifica si hay stock del producto con el nombre recibido.
     * 
     */
    boolean hayStock(String nombreDelProducto) {
        this.almacen.hayStock(nombreDelProducto)
    }

    /**
     * 
     * Agrega la cantidad indicada del producto con el nombre recibido al pedido. En caso 
     * de que no se pueda agregar al pedido se devolvera false.
     * 
     */
    boolean agregarAlPedido(String nombreProducto, int cantidad, Pedido pedido) {
        this.almacen.retirarProducto(nombreProducto, cantidad, pedido) 
    }


    /**
    * 
    * Agrega un producto al pedido indicado a cambio de puntos de confianza. Si no es
    * posible agregar el producto al pedido se retorna false.
    * 
    */
    boolean agregarAlPedidoPorPuntoDeConfianza(String nombreProducto, int cantidad, Pedido pedido) {
        this.almacen.retirarProductoPorPuntosDeConfianza(nombreProducto, cantidad, pedido) 
    }

    /**
     * 
     * TODO
     * 
     */
    void reingresarStockDelPedido(int id) {
        Map<String, Producto> productos = this.comprasRegistradas[id].getPedido().getProductos()
        productos.each{ _, producto -> this.ingresarStock(producto.getNombre(), producto.getCantidad()) }
    }


    /* MÉTODOS REFERIDOS A LA ETAPA DE REGISTRO Y RETIRO DE COMPRAS */
    
    /**
     * 
     * TODO
     * 
     */
    Compra registrarCompra(Pedido pedidoConfirmado) {
        int idCompraRegistrada = ids_compras++
        Compra compraRegistrada = new Compra(pedidoConfirmado, idCompraRegistrada)
        this.comprasRegistradas[idCompraRegistrada] = compraRegistrada
        compraRegistrada
    }

    /**
     * 
     * TODO
     * 
     */
    void prepararCompra(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.AGUARDANDO_PREPARACION) throw new Exception("No se puede preparar dicha compra ya que la misma no se encontraba AGUARDANDO_PREPARACION.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.EN_PREPARACION
    }

    /**
     * 
     * TODO
     * 
     */
    void marcarCompraListaParaRetirar(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.EN_PREPARACION) throw new Exception("No se puede marcar dicha compra como LISTA_PARA_RETIRAR ya que la misma no se encontraba EN_PREPARACION.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.LISTA_PARA_RETIRAR
    }

    /**
     * 
     * TODO
     * 
     */
    void marcarCompraRetirada(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.LISTA_PARA_RETIRAR) throw new Exception("No se puede marcar dicha compra como RETIRADA ya que la misma no se encontraba LISTA_PARA_RETIRAR.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.RETIRADA
    }

    /**
     * 
     * TODO
     * 
     */
    void marcarCompraCancelada(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.RETIRADA) throw new Exception("No se puede marcar dicha compra como CANCELADA ya que la misma no se encontraba RETIRADA.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.CANCELADA
    }

    /**
     * 
     * TODO
     * 
     */
    void marcarCompraDevuelta(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.RETIRADA) throw new Exception("No se puede marcar dicha compra como DEVUELTA ya que la misma no se encontraba RETIRADA.")
        comprasRegistradas[id].estado = Compra.EstadoDeCompra.DEVUELTA
    }

    /**
     * 
     * TODO
     * 
     */
    Compra.EstadoDeCompra estadoDeCompra(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        comprasRegistradas[id].getEstado()
    }

    /**
     * 
     * TODO
     * 
     */
    void devolucionDelPedido(int id) {
        if (!comprasRegistradas[id]) throw new Exception("No se encuentra registrada una compra con el ID indicado.")
        if (comprasRegistradas[id].estado != Compra.EstadoDeCompra.RETIRADA) throw new Exception("No se puede devolver el pedido ya que la compra no se encontraba RETIRADA.")
        this.reingresarStockDelPedido(id)
        this.marcarCompraDevuelta(id)
    }
}
