package takeawaynow

import java.time.LocalDateTime

/**
 * 
 * La compra representa el pedido confirmado.
 * 
 */
class Compra {

    static constraints = {
    }

    static belongsTo = [cliente: Cliente, negocio: Negocio]

    enum EstadoDeCompra {
        AGUARDANDO_PREPARACION,
        EN_PREPARACION,
        LISTA_PARA_RETIRAR,
        RETIRADA,
        CANCELADA,
        DEVUELTA
    }

    Pedido pedido
    EstadoDeCompra estado
    LocalDateTime fecha
    int id

    /**
     * 
     * Crea una compra con el pedido y id dado. Se guarda la fecha en la que se crea y se
     * setea su estado como "aguardando preparacion".
     * 
     */
    Compra(Pedido pedido, int id) {
        this.pedido = pedido
        this.estado = EstadoDeCompra.AGUARDANDO_PREPARACION
        this.fecha = LocalDateTime.now()
        this.id = id
    }

    /**
     * 
     * Indica la cantidad de productos totales en el pedido.
     * 
     */
    int cantidadDeProductos() {
        this.pedido.cantidadDeProductos()
    }

    /**
     * 
     * Indica la cantidad de productos a cambio de dinero en el pedido.
     * 
     */
    int cantidadDeProductosPorDinero() {
        this.pedido.cantidadDeProductosPorDinero()
    }
}
