package takeawaynow

import java.time.LocalDateTime

/**
 * 
 * TODO
 * 
 */
class Compra {

    static constraints = {
    }

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
     * TODO
     * 
     */
    Compra (Pedido pedido, int id) {
        this.pedido = pedido
        this.estado = EstadoDeCompra.AGUARDANDO_PREPARACION
        this.fecha = LocalDateTime.now()
        this.id = id
    }

    /**
     * 
     * TODO
     * 
     */
    int cantidadDeProductos() {
        this.pedido.cantidadDeProductos()
    }

    /**
     * 
     * TODO
     * 
     */
    int cantidadDeProductosPorDinero() {
        this.pedido.cantidadDeProductosPorDinero()
    }
}