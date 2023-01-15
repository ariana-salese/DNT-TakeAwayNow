package takeawaynow

import java.time.LocalDateTime

/**
 * 
 * Plan prime es un plan al cual pueden subscribirse un cliente a cambio de dinero por mes,
 * le dara ciertos privilegios
 * 
 */
class PlanPrime implements PlanDeCliente {

    final Dinero PRECIO_PRIME = new Dinero(500)
    final float DESCUENTO = 0.25
    final float MULTIPLICADOR_PUNTOS = 2
    LocalDateTime diaInicioPrime = LocalDateTime.now()

    PlanPrime(Dinero saldoCliente) {
        if (this.PRECIO_PRIME > saldoCliente) throw new IllegalStateException("Saldo insufuciente para subscribirse al plan prime.")
    }

    /**
     * 
     * Retorna el saldo indicado actualizado segun el precio de la compra. Al saldo
     * original se le restara el precio de la compra con un descuento.
     * 
    */
    Dinero obtenerSaldoActualizadoPorCompra(Compra compra, Dinero saldoCliente) {
        saldoCliente - (compra.precio() * (1 - this.DESCUENTO))
    }

    /**
     * 
     * Retorna los puntos de confianza indicados actualizados segun los puntos utilizados en
     * la compra confirmada.
     * 
    */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente - compra.puntos()
    }

    /**
     * 
     * Retorna un error ya que un cliente con plan prime quiere subscribirse nuevamente.
     * 
    */
    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente) {
        throw new IllegalStateException("El cliente ya esta subscribido al plan prime.")
    }

    /**
     * 
     * Obtiene el saldo indicado actualizado por subscribirse al plan prime. EL saldo final
     * sera el inicla disminuido el precio del plan prime. 
     * 
    */
    Dinero obtenerSaldoActualizadoPorSubscripcion(Dinero saldoCliente) {
        saldoCliente - this.PRECIO_PRIME
    }

    /**
     * 
     * TODO
     * 
    */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.agregarPuntosDeConfianzaPorCompra(this.MULTIPLICADOR_PUNTOS)
    }

    /**
     * 
     * TODO
     * 
    */
    PuntosDeConfianza eliminarPuntosPorCompra(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.eliminarPuntosPorCompra(compra, this.DESCUENTO)
    }

    /**
     * 
     * TODO
     * 
     */
    int diasRestantesDePlanPrime() {
        LocalDateTime dia = LocalDateTime.now()

        //print "dia fin ${this.diaInicioPrime.getDayOfMonth()}\n"
        //print "dia ahora ${dia.getDayOfMonth()}\n"
        this.diaInicioPrime.getDayOfMonth() + 30 - dia.getDayOfMonth()
    }

}
