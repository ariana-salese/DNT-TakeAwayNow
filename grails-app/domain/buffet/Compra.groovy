package buffet

import java.time.LocalDateTime

class Compra {

    static constraints = {
    }

    Pedido pedido
    LocalDateTime fecha

    Compra (Pedido pedido) {
        this.pedido = pedido
        this.fecha = LocalDateTime.now()
    }

    LocalDateTime fecha() {
        this.fecha
    }

    Pedido pedido() {
        this.pedido
    }

}
