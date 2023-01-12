package takeawaynow

import java.time.LocalDateTime

/**
 * 
 * TODO
 * 
 */
class PlanPrime extends PlanDeCliente {

    final Dinero PRECIO_PRIME = new Dinero(500)
    final float DESCUENTO = 0.25
    final float MULTIPLICADOR_PUNTOS = 2

    LocalDateTime diaFinPrime 

    PlanPrime(Dinero saldoCliente) {
        if (this.PRECIO_PRIME > saldoCliente) throw new IllegalStateException("Saldo insufuciente para subscribirse al plan prime.")

        this.diaFinPrime = new LocalDateTime() + 30
    }

    /**
     * 
     * TODO
     * 
    */
    @Override
    Dinero obtenerSaldoActualizadoPorCompra(Compra compra, Dinero saldoCliente) {
        saldoCliente - (compra.precio() * (1 - this.DESCUENTO))
    }

    /**
     * 
     * TODO
     * 
    */
    @Override
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente - compra.puntos()
    }

    /**
     * 
     * TODO
     * 
    */
    @Override
    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente) {
        throw new IllegalStateException("El cliente ya esta subscribido al plan prime.")
    }

    /**
     * 
     * TODO
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
    @Override
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.agregarPuntosDeConfianzaPorCompra(this.MULTIPLICADOR_PUNTOS)
    }

    /**
     * 
     * TODO
     * 
    */
    @Override
    PuntosDeConfianza eliminarPuntosPorCompra(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.eliminarPuntosPorCompra(compra, this.DESCUENTO)
    }

}
