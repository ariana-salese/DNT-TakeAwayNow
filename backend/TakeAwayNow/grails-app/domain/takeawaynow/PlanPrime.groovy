package takeawaynow

/**
 * 
 * TODO
 * 
 */
class PlanPrime implements PlanDeCliente {

    final Dinero PRECIO_PRIME = new Dinero(500)
    final float DESCUENTO = 0.25
    final float MULTIPLICADOR_PUNTOS = 2

    Date diaFinPrime 

    PlanPrime(Dinero saldoCliente) {
        if (this.PRECIO_PRIME > saldoCliente) throw new IllegalStateException("Saldo insufuciente para subscribirse al plan prime.")

        this.diaFinPrime = new Date() + 30
    }

    /**
     * 
     * TODO
     * 
     */
    PlanPrime subscribirseAPlanPrime() {
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
    Dinero obtenerSaldoActualizadoPorCompra(Compra compra, Dinero saldoCliente) {
        saldoCliente - (compra.precio() * (1 - this.DESCUENTO))
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompra(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente - compra.puntos()
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.agregarPuntosDeConfianzaPorCompra(this.MULTIPLICADOR_PUNTOS)
    }

}
