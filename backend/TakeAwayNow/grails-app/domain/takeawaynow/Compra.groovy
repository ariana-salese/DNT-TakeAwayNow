package takeawaynow

import java.time.LocalDateTime

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

    Compra (Pedido pedido, int id) {
        this.pedido = pedido
        this.estado = EstadoDeCompra.AGUARDANDO_PREPARACION
        this.fecha = LocalDateTime.now()
        this.id = id
    }
}