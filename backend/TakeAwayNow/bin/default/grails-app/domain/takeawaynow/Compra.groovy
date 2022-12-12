package takeawaynow

import java.time.LocalDateTime

class Compra {

    static constraints = {
    }

    Pedido pedido
    LocalDateTime fecha
    int id_compra

    Compra (Pedido pedido, id_compra) {
        this.pedido = pedido
        this.fecha = LocalDateTime.now()
        this.id_compra = id_compra
    }

    LocalDateTime fecha() {
        this.fecha
    }

    Pedido pedido() {
        this.pedido
    }

}