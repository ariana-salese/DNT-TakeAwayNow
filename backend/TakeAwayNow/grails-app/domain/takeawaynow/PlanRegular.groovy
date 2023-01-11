package takeawaynow

/**
 * 
 * TODO
 * 
 */
class PlanRegular implements PlanDeCliente {

    /**
     * 
     * TODO
     * 
     */
    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente) {
        new PlanPrime(saldoCliente)
    }

    /**
     * 
     * TODO
     * 
     */
    Dinero obtenerSaldoActualizadoPorCompra(Compra compra, Dinero saldoCliente) {
        saldoCliente - compra.precio()
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente - compra.puntos()
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.agregarPuntosPorCompra(compra)
    }
}
