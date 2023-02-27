package takeawaynow

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit;

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
        if (saldoCliente < this.PRECIO_PRIME) throw new IllegalStateException("Saldo insufuciente para subscribirse al plan prime.")
    }

    /**
     * 
     * Retorna un error ya que un cliente con plan prime quiere subscribirse nuevamente.
     * 
    */
    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente) {
        throw new IllegalStateException("El cliente ya esta suscripto al plan prime.")
    }

    /**
     * 
     * Retorna el saldo indicado actualizado segun el precio de la compra. Al saldo
     * original se le restara el precio de la compra con un descuento.
     * 
    */
    Dinero obtenerSaldoActualizadoPorCompra(Dinero precio, Dinero saldoCliente) {
        saldoCliente - (precio * new Dinero(1 - this.DESCUENTO))
    }

    /**
     * 
     * Retorna los puntos de confianza indicados actualizados segun los puntos utilizados en
     * la compra confirmada.
     * 
    */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente - compra.getPedido().puntos()
    }


    /**
     * 
     * Obtiene el saldo indicado actualizado por subscribirse al plan prime. EL saldo final
     * sera el incial disminuido el precio del plan prime. 
     * 
    */
    Dinero obtenerSaldoActualizadoPorSubscripcion(Dinero saldoCliente) {
        saldoCliente - this.PRECIO_PRIME
    }

    /**
     * 
     * Obtiene los puntos de confianza actualizados por compra. Se aplica un multiplicador de puntos por
     * el plan prime.
     * 
    */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.agregarPuntosPorCompra(compra, this.MULTIPLICADOR_PUNTOS)
    }

    /**
     * 
     * Resta los puntos de confianza obtenidos por la compra a los puntos de confianza recibidos,
     * teninedo en cuenta el multiplicados aplicado por el prime.
     * 
    */
    PuntosDeConfianza eliminarPuntosPorCompra(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.eliminarPuntosPorCompra(compra, this.MULTIPLICADOR_PUNTOS)
    }

    /**
     * 
     * Retorna si el plan prime esta vigente. No esta vigente si pasaron mas de 30 dias
     * desde el dia en el que se subscribio al plan prime.
     * 
     */
    boolean planPrimeVigente() {
        LocalDateTime dia = LocalDateTime.now()

        this.diaInicioPrime >= dia.minus(30, ChronoUnit.DAYS)
    }

}
